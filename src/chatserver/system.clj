(ns chatserver.system
  "Components and their dependency relationships"
  (:refer-clojure :exclude (read))
  (:require
   [clojure.java.io :as io]
   [clojure.tools.reader :refer (read)]
   [clojure.string :as str]
   [clojure.tools.reader.reader-types :refer (indexing-push-back-reader)]
   [com.stuartsierra.component :refer (system-map system-using using)]
   [modular.maker :refer (make)]
   [modular.bidi :refer (new-router new-web-resources)]
   [modular.aleph :refer (new-webserver)]
   [chatserver.events :refer (new-events-website)]
   [chatserver.website :refer (new-website)]))

(defn ^:private read-file
  [f]
  (read
   ;; This indexing-push-back-reader gives better information if the
   ;; file is misconfigured.
   (indexing-push-back-reader
    (java.io.PushbackReader. (io/reader f)))))

(defn ^:private config-from
  [f]
  (if (.exists f)
    (read-file f)
    {}))

(defn ^:private user-config
  []
  (config-from (io/file (System/getProperty "user.home") ".chatserver.edn")))

(defn ^:private config-from-classpath
  []
  (if-let [res (io/resource "chatserver.edn")]
    (config-from (io/file res))
    {}))

(defn config
  "Return a map of the static configuration used in the component
  constructors."
  []
  (merge (config-from-classpath)
         (user-config)))

(defn http-listener-components
  [system config]
  (assoc system
    :http-listener-listener
    (new-webserver :port 3001)))

(defn modular-bidi-router-components
  [system config]
  (assoc system
    :modular-bidi-router-webrouter
    (make new-router config)))

(defn jquery-components
  "Serve JQuery resources from a web-jar."
  [system config]
  (assoc system
    :jquery-resources
    (->
      (make new-web-resources config :uri-context "/jquery" :resource-prefix "META-INF/resources/webjars/jquery/2.1.0" :key :jquery-resources)
      (using []))))

(defn sse-demo-website-components
  [system config]
  (assoc system
             :sse-demo-website-website
    (->
      (make new-website config)
      (using []))))

(defn sse-demo-events-components
  [system config]
  (assoc system
    :sse-demo-events-events
    (->
      (make new-events-website config)
      (using []))))

(defn new-system-map
  [config]
  (apply system-map
    (apply concat
      (-> {}

          (http-listener-components config)
          (modular-bidi-router-components config)
          (jquery-components config)
          (sse-demo-website-components config)
          (sse-demo-events-components config)))))

(defn new-dependency-map
  []
  {:http-listener-listener {:request-handler :modular-bidi-router-webrouter},
   :modular-bidi-router-webrouter {:jquery :jquery-resources,
                                   :website :sse-demo-website-website,
                                   :events :sse-demo-events-events}})

(defn new-co-dependency-map
  []
  {})

(defn new-production-system
  "Create the production system"
  ([opts]
   (-> (new-system-map (merge (config) opts))
     (system-using (new-dependency-map))))
  ([] (new-production-system {})))
