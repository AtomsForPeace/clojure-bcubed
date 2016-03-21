(ns clojure-bcubed.core
    (:require
        [clojure.set :as set]
        [clojure.tools.logging :as log]
        [clojure.data.json :as json]))

; (def original (json/read-str (slurp "/path/to/json")
;                 :key-fn keyword))
; (def modified (json/read-str (slurp "/path/to/json")
;                 :key-fn keyword))

(def original {:hi [2 3 4] :bye [6 7 8 9]})
(def modified {:hi [1 2 3] :bye [4 5 6]})

(defn average [numbers] (/ (apply + numbers) (count numbers)))

(defn NaN? [x] (false? (== x x)))

(defn recall [set1_1 set1_2 set2_1 set2_2]
    (log/info "recall" set1_1 set1_2 set2_1 set2_2)
    (float (/ (min (float (count (set/intersection set1_1 set1_2)))
                   (float (count (set/intersection set2_1 set2_2))))
              (float (count (set/intersection set2_1 set2_2))))))

(defn multi-recall [dict1 dict2]
    (for [keyval1 dict1 keyval2 dict1]
        (do (let [di1el1 (set (dict1 (key keyval1)))
                  di1el2 (set (dict1 (key keyval2)))
                  di2el1 (set (dict2 (key keyval1)))
                  di2el2 (set (dict2 (key keyval2)))]
            (if (set/intersection di2el1 di2el2)
                (recall di1el1 di1el2 di2el1 di2el2))))))

(defn precision [set1_1 set1_2 set2_1 set2_2]
    (log/info "precision" set1_1 set1_2 set2_1 set2_2)
    (float (/ (min (float (count (set/intersection set1_1 set1_2)))
                   (float (count (set/intersection set2_1 set2_2))))
              (float (count (set/intersection set1_1 set1_2))))))

(defn multi-precision [dict1 dict2]
    (for [keyval1 dict1 keyval2 dict1]
        (do (let [di1el1 (set (dict1 (key keyval1)))
                  di1el2 (set (dict1 (key keyval2)))
                  di2el1 (set (dict2 (key keyval1)))
                  di2el2 (set (dict2 (key keyval2)))]
            (if (set/intersection di1el1 di1el2)
                (precision di1el1 di1el2 di2el1 di2el2))))))

(defn -main [& args]
    (log/info "Number of keys:" (count original))
    (log/info (multi-recall original modified))
    (log/info (multi-precision original modified)))
