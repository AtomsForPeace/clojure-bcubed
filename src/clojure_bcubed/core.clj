(ns clojure-bcubed.core
    (:require
        [clojure.set :as set]
        [clojure.tools.logging :as log]
        [clojure.data.json :as json]
        [clojure.math.combinatorics :as combo])
    (:gen-class))

; (def original {:hi [2 3 4] :bye [6 7 8 9]})
; (def modified {:hi [1 2 3] :bye [4 5 6]})

(defn average [numbers] (/ (apply + numbers) (count numbers)))

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

(defn recall [categories clusters]
  (log/info "Starting recall")
  (average
    (map
      (partial apply (partial multi-recall categories clusters))
      (filter
        (partial apply (fn [el1 el2] (not= #{} (set/intersection (categories el1) (categories el2)))))
        (combo/selections (keys clusters) 2)
      )
    )
  )
)

(defn recall-sensible [categories clusters]
  (log/info "Starting recall-sensible")
  (average
    (map
      (partial apply (partial multi-recall categories clusters))
      (filter
        (partial apply (fn [el1 el2] (not= #{} (set/intersection (categories el1) (categories el2)))))
        (combo/combinations (keys clusters) 2)
      )
    )
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

(defn precision [categories clusters]
  (log/info "Starting precision")
  (average
    (map
      (partial apply (partial multi-precision categories clusters))
      (filter
        (partial apply (fn [el1 el2] (not= #{} (set/intersection (clusters el1) (clusters el2)))))
        (combo/selections (keys clusters) 2)
      )
    )
  )
)

(defn precision-sensible [categories clusters]
  (log/info "Starting precision-sensible")
  (average
    (map
      (partial apply (partial multi-precision categories clusters))
      (filter
        (partial apply (fn [el1 el2] (not= #{} (set/intersection (clusters el1) (clusters el2)))))
        (combo/combinations (keys clusters) 2)
      )
    )
  )
)

(defn -main [& args]
  true
)
