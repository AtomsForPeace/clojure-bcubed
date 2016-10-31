(ns clojure-bcubed.core
    (:require
        [clojure.set :as set]
        [clojure.tools.logging :as log]
        [clojure.data.json :as json]
        [clojure.math.combinatorics :as combo])
    (:gen-class))

(set! *unchecked-math* :warn-on-boxed)

(defn averager-factory []
  (fn
    (
      []
      [0 0]
    )
    (
      [state]
      state
    )
    (
      [[^double sum ^long n] ^double number]
      [ (+ sum number) (inc n) ]
    )
  )
)

(defn multi-recall [categories clusters el1 el2]
  (let
    [
      clusters-intersection (count (set/intersection (clusters el1) (clusters el2)))
      categories-intersection (count (set/intersection (categories el1) (categories el2)))
    ]
    (float (/ (min clusters-intersection categories-intersection)
              categories-intersection))
  )
)

(defn multi-precision [categories clusters el1 el2]
  (let
    [
      clusters-intersection (count (set/intersection (clusters el1) (clusters el2)))
      categories-intersection (count (set/intersection (categories el1) (categories el2)))
    ]
    (float (/ (min clusters-intersection categories-intersection)
              clusters-intersection))
  )
)

; transducer that takes pairs of elements, filters out those whose categories have non-empty
; intersection and maps those through multi-recall
(defn recall-xform [categories clusters]
  (comp
    (filter (partial apply (fn [el1 el2] (seq (set/intersection (categories el1) (categories el2))))))
    (map (partial apply (partial multi-recall categories clusters)))
  )
)

(defn precision-xform [categories clusters]
  (comp
    (filter (fn [[ el1 el2]] (seq (set/intersection (clusters el1) (clusters el2)))))
    (map (fn [[el1 el2]] (multi-precision categories clusters el1 el2)))
  )
)

; the power of Clojure: apply the recall transducer to the index pairs, using averager
; as the reducing function
(defn recall [categories clusters]
  (log/info "Starting recall")
  (let [
    partitions (partition-all 10000 (combo/selections (keys clusters) 2))
    transducer (partial transduce (recall-xform categories clusters) (averager-factory))
    [^double sum ^long n] (apply mapv + (pmap transducer partitions))
    ]
    (/ sum n)
  )
)

; (defn recall-sensible [categories clusters]
;   (log/info "Starting recall-sensible")
;   (transduce (recall-xform categories clusters) averager (combo/combinations (keys clusters) 2))
; )
;
; (defn precision [categories clusters]
;   (log/info "Starting precision")
;   (transduce (precision-xform categories clusters) averager (combo/selections (keys clusters) 2))
; )
;
; (defn precision-sensible [categories clusters]
;   (log/info "Starting precision-sensible")
;   (transduce (precision-xform categories clusters) averager (combo/combinations (keys clusters) 2))
; )

(defn vals-to-sets [dict]
  (zipmap (keys dict) (map set (vals dict)))
)

(defn -main [& args]
  (log/info "Starting up")
  (def original (vals-to-sets (json/read-str (slurp (first args)))))
  (def modified (vals-to-sets (json/read-str (slurp (last args)))))
  (log/info "Number of clusters: " (count original))
  (log/info (time (recall original modified)))
  (log/info "Finished recall")
  ; (log/info (precision original modified))
  (shutdown-agents)
)
