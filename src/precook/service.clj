(ns precook.service
  (:require [clojure.core.async :refer [chan go go-loop close! put! >! <! <!! alts! timeout]]))

(defn create [f t]
  "Takes a constructor f of a fn g that takes arbitrary
  args. Returns a fn h that takes the same args. When
  h is called, a g is constructed and kept alive for t
  ms to handle subsequent calls."

  (let [req (chan)
        res (chan)
        stop (chan)]
    (go-loop [inst nil
              keepalive nil]
      (let [[val port] (alts! (filter (complement nil?) [req stop keepalive]))]
        (condp = port
          req (let [inst (or inst (f))]
                (>! res (apply inst val))
                (recur inst (timeout t)))
          keepalive (recur nil nil)
          stop (close! res))))
    (fn [& args]
      (put! req args)
      (<!! res))))

(comment
  (defn ex []
    (println "creating example instance")
    #(inc %))
  (def s (create ex 1000))
  (do
    (println "42 >" (s 42))
    (println "43 >" (s 43))
    (go
      (<! (timeout 2000))
      (println "44 >" (s 44)))))