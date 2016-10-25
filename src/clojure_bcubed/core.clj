(ns clojure-bcubed.core
    (:require
        [clojure.set :as set]
        [clojure.tools.logging :as log]
        [clojure.data.json :as json]
        [clojure.math.combinatorics :as combo])
    (:use clojure.contrib.command-line)
    (:gen-class))

; (def original {:hi [2 3 4] :bye [6 7 8 9]})
; (def modified {:hi [1 2 3] :bye [4 5 6]})

(defn average [numbers] (/ (apply + numbers) (count numbers)))

(defn recall [set1_1 set1_2 set2_1 set2_2]
    (float (/ (min (float (count (set/intersection set1_1 set1_2)))
                   (float (count (set/intersection set2_1 set2_2))))
              (float (count (set/intersection set2_1 set2_2))))))

(defn multi-recall [dict1 dict2]
    (log/info "Starting recall")
    (average recall (filter set/intersection (combo/subsets (keys dict1)))
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
    (with-command-line args
      "Necessary?"
      [[orig "Ground truth data json file" 1]
       [modi "Json file to be tested for improvements" 2]]
    (def original (json/read-str (slurp orig)
        :key-fn keyword))
    (def modified (json/read-str (slurp modi)
        :key-fn keyword))
    (log/info "Number of clusters:" (count original))
    (log/info (average (multi-recall original modified)))
    (log/info (average (multi-precision original modified)))))
