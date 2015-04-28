(defproject

  precook
  "0.1.0-SNAPSHOT"

  :description
  "Utilities for pre-rendering ClojureScript-based pages in JVM"

  :url
  "https://github.com/sander/precook"

  :license
  {:name "Eclipse Public License"
   :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies
  [[org.clojure/clojure "1.7.0-beta2"]
   [org.clojure/clojurescript "0.0-3211"]
   [org.clojure/core.async "0.1.346.0-17112a-alpha"]
   [com.cognitect/transit-clj "0.8.271"]
   [com.cognitect/transit-cljs "0.8.207"]
   [bidi "1.18.10"]
   [org.omcljs/om "0.8.8"]]

  :repl-options
  {:init-ns precook.core}

  :profiles
  {:example
   {:source-paths   ["example/src"]
    :resource-paths ["example/resources"]
    :dependencies   [[ring "1.3.2"]
                     [compojure "1.3.3"]
                     [enlive "1.1.5"]]
    :plugins        [[lein-cljsbuild "1.0.5"]
                     [lein-ring "0.9.3"]]
    :ring           {:handler website.server/app}
    :hooks          [leiningen.cljsbuild]
    :cljsbuild      {:builds
                     {:precook
                      {:source-paths ["example/src/website"]
                       :compiler     {:output-to     "example/resources/precook/main.js"
                                      :optimizations :simple
                                      :jar           true}}

                      :deploy
                      {:source-paths ["example/src/website"]
                       :compiler     {:output-to       "example/resources/public/js/main.js"
                                      :output-dir      "example/resources/public/js/out"
                                      :asset-path      "/js/out"
                                      :optimizations   :advanced
                                      :main            website.main
                                      :jar             true}}}}}})
