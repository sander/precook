(ns website.core
  (:require
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]
    [bidi.bidi :refer [match-route path-for]]
    [website.routes :refer [routes]]
    [precook.render]
    [precook.om-helpers]))

(defn navigate [data handler ev]
  (if ev (.preventDefault ev))
  (js/history.pushState nil "" (path-for website.routes/routes handler))
  (om/update! data :route {:handler handler}))

(defn site [data owner]
  (reify
    om/IRenderState
    (render-state [_ {:keys [hover?]}]
      (dom/main
        #js {:style       #js {:backgroundColor (if hover? "yellow" "white")}
             :onMouseOver #(om/set-state! owner :hover? true)
             :onMouseOut  #(om/set-state! owner :hover? false)}
        (dom/nav
          nil
          (dom/a #js {:href    (path-for routes :index)
                      :onClick #(navigate data :index %)}
                 "Home")
          (dom/a #js {:href    (path-for routes :article-index)
                      :onClick #(navigate data :article-index %)}
                 "Articles"))
        (dom/pre nil (pr-str data))))))

(defn site-renderer [data]
  {:title "Untitled"
   :body  (dom/render-to-str (om/build site data))})
(defn render-in-browser []
  (om/root precook.om-helpers/in-browser-site
           {:route (match-route routes js/location.pathname)}
           {:target (js/document.querySelector "body > main")
            :opts   {:view      site
                     :route-key :route
                     :routes    routes}}))