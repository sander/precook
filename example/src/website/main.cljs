(ns website.main
  (:require
    [website.core :refer [render-in-browser]]))

(def in-browser? (this-as this (aget this "window")))

(when in-browser?
  (enable-console-print!)
  (render-in-browser))
