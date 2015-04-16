(ns dev
  (:require
   [clojure.pprint :refer (pprint)]
   [clojure.reflect :refer (reflect)]
   [clojure.repl :refer (apropos dir doc find-doc pst source)]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [clojure.java.io :as io]
   [com.stuartsierra.component :as component]
   [chatserver.system :refer (config new-system-map new-dependency-map new-co-dependency-map)]
   [modular.maker :refer (make)]
   [modular.wire-up :refer (normalize-dependency-map)]
   [clojure.core.async :as a]))

(def system nil)

(defn new-dev-system
  "Create a development system"
  []
  (let [config (config)
        s-map (->
               (new-system-map config)
               #_(assoc
                 ))]
    (-> s-map
        (component/system-using (new-dependency-map))
        )))

(defn init
  "Constructs the current development system."
  []
  (alter-var-root #'system
    (constantly (new-dev-system))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root
   #'system
   component/start
))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start)
  :ok
  )

(defn reset []
  (stop)
  (refresh :after 'dev/go))

;; REPL Convenience helpers

(defn routes []
  (-> system :modular-bidi-router-webrouter :routes))

(defn match-route [path]
  (bidi.bidi/match-route (routes) path))

(defn path-for [path & args]
  (apply modular.bidi/path-for
         (-> system :modular-bidi-router-webrouter) path args))

;; ~/chatserver/dev/dev.clj
(defn get-channel [] (-> system :sse-demo-events-events :channel))
