(defproject chatserver "0.1.0-SNAPSHOT"
  :description "A chatserver"
  :url "http://github.com/juxt/chatserver"

  :exclusions [com.stuartsierra/component]

  :dependencies
  [
   [hiccup "1.0.5"]
      [com.stuartsierra/component "0.2.2"]
   [juxt.modular/bidi "0.9.1"]
   #_[juxt.modular/http-kit "0.5.4"]
   [juxt.modular/aleph "0.0.8"]
   [yada "0.2.3"]
   [juxt.modular/maker "0.5.0"]
   [juxt.modular/wire-up "0.5.0"]
   [org.clojure/clojure "1.7.0-alpha4"]
   [org.clojure/core.async "0.1.346.0-17112a-alpha"]
   [org.clojure/tools.logging "0.2.6"]
   [org.clojure/tools.reader "0.8.13"]
   [org.slf4j/jcl-over-slf4j "1.7.2"]
   [org.slf4j/jul-to-slf4j "1.7.2"]
   [org.slf4j/log4j-over-slf4j "1.7.2"]
   [org.webjars/jquery "2.1.0"]
   [prismatic/plumbing "0.3.5"]
   [prismatic/schema "0.3.5"]
   [ch.qos.logback/logback-classic "1.0.7" :exclusions [org.slf4j/slf4j-api]]
   ]

  :main chatserver.main

  :repl-options {:init-ns user
                 :welcome (println "Type (dev) to start")}


  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.5"]]
                   :source-paths ["dev"
                                  ]}})
