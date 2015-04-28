(ns website.server
 (:require
  [clojure.java.io :as io]
  [ring.adapter.jetty :refer [run-jetty]]
  [net.cgrand.enlive-html :as html]
  [compojure.core :refer [defroutes]]
  [compojure.route :refer [resources]]
  [website.routes :refer [routes]]
  [precook.render :refer [wrap-renderer]]))

(html/deftemplate
 page (io/resource "page.html")
 [{:keys [title body]}]
 [:head :title] (html/content title)
 [:main] (html/html-content body))

(defroutes
 compojure-routes
 (resources "/"))

(def app
 (-> compojure-routes
     (wrap-renderer page 'website.core/site-renderer 60000 routes)))

(defn start []
 (def server (run-jetty #'app {:port 3000 :join? false})))
