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

(defn recall [set1_1 set1_2 set2_1 set2_2]
    (float (/ (min (float (count (set/intersection set1_1 set1_2)))
                   (float (count (set/intersection set2_1 set2_2))))
              (float (count (set/intersection set2_1 set2_2))))))

(defn multi-recall [ldict cdict]
  (filter
    (fn [l] (not= #{} (set/intersection (ldict (first l)) (ldict (last l)))))
    (combo/selections (keys cdict) 2)
  )
    (average recall (filter (fn [l] (set/intersection (ldict (first l)) (ldict (last l)) (combo/selections (keys cdict)))))
    ; (for [keyval1 dict1 keyval2 dict1]
    ;     (do (let [di2el1 (set (dict2 (key keyval1)))
    ;               di2el2 (set (dict2 (key keyval2)))]
    ;         (if (set/intersection di2el1 di2el2)
    ;             (let [di1el1 (set (dict1 (key keyval1)))
    ;                   di1el2 (set (dict1 (key keyval2)))]
    ;             (recall di1el1 di1el2 di2el1 di2el2)))))))

(defn precision [set1_1 set1_2 set2_1 set2_2]
    (float (/ (min (float (count (set/intersection set1_1 set1_2)))
                   (float (count (set/intersection set2_1 set2_2))))
              (float (count (set/intersection set1_1 set1_2))))))

(defn multi-precision [dict1 dict2]
    (log/info "Starting precision")
    (for [keyval1 dict1 keyval2 dict1]
        (do (let [di1el1 (set (dict1 (key keyval1)))
                  di1el2 (set (dict1 (key keyval2)))]
            (if (set/intersection di1el1 di1el2)
                (let [di2el1 (set (dict2 (key keyval1)))
                      di2el2 (set (dict2 (key keyval2)))]
                (precision di1el1 di1el2 di2el1 di2el2)))))))

(defn -main [& args]
  true
)
