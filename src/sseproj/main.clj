(ns sseproj.main
  "Main entry point"
  (:require clojure.pprint)
  (:gen-class))

(defn -main [& args]
  ;; We eval so that we don't AOT anything beyond this class
  (eval '(do (require 'sseproj.system)
             (require 'sseproj.main)
             (require 'com.stuartsierra.component)

             (require 'clojure.java.browse)

             (println "Starting sseproj")

             (let [system (->
                           (sseproj.system/new-production-system)
                           com.stuartsierra.component/start)]

               (println "System started")
               (println "Ready...")

               (let [url (format "http://localhost:%d/" (-> system :http-listener-listener :port))]
                 (println (format "Browsing at %s" url))
                 (clojure.java.browse/browse-url url))))))
