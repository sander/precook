(ns precook.render
  (:require
    [clojure.string :as str]
    [cognitect.transit :as transit]))

(defn serialize [v]
  (transit/write (transit/writer :json) v))
(defn deserialize [s]
  (transit/read (transit/reader :json) s))

(defn identifier [s]
  (str/replace s \- \_))
(defn compile-symbol [s]
  (let [[ns nm] (str/split (str s) \/)
        path (map identifier (str/split ns \.))]
    [(apply aget js/global path) (identifier nm)]))

(defn render [json]
  (let [{:keys [symbol state]} (deserialize json)
        [ns renderer] (compile-symbol symbol)
        result (.call (aget ns renderer) ns state)]
    (serialize result)))
