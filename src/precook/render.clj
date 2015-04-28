(ns precook.render
  (:require
    [clojure.java.io :as io]
    [cognitect.transit :as transit]
    [bidi.bidi :refer [match-route]]
    [precook.service])
  (:import
    [java.io ByteArrayInputStream ByteArrayOutputStream]
    [javax.script ScriptEngineManager ScriptEngine Invocable]))

(defn serialize [v]
  (let [out (ByteArrayOutputStream. 4096)
        writer (transit/writer out :json)]
    (transit/write writer v)
    (.toString out)))
(defn deserialize [s]
  (let [in (ByteArrayInputStream. (.getBytes s))
        reader (transit/reader in :json)]
    (transit/read reader)))

(defn nashorn-engine []
  ^ScriptEngine (.getEngineByName (ScriptEngineManager.) "nashorn"))
(defn env [ng src]
  ^Invocable (doto ng (.eval "var global=this;") (.eval src)))

(defn renderer
  ([symb] (renderer (env (nashorn-engine) (io/reader (io/resource "precook/main.js"))) symb))
  ([env symb]
   (let [ns (.eval env "precook.render")
         params {:symbol symb}]
     (fn [state]
       (let [args (-> params (assoc :state state) serialize list object-array)]
         (deserialize (.invokeMethod env ns "render" args)))))))

(comment
  (def r (renderer 'website.core/custom-renderer))
  (r {:da :ta}))

(defn render-handler [template renderer routes {:keys [uri]}]
  (if-let [route (match-route routes uri)]
    (let [result (renderer {:route route})]
      {:status  200
       :headers {"Content-Type" "text/html; charset=UTF-8"}
       :body    (template result)})))

(defn wrap-renderer [handler template symb keepalive routes]
  (let [rnd (precook.service/create #(renderer symb) keepalive)]
    #(or (render-handler template rnd routes %) (handler %))))