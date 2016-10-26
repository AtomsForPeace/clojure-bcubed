(ns clojure-bcubed.core-test
  (:require [clojure.test :refer :all]
            [clojure-bcubed.core :refer :all]))

(deftest test-average
  (is (= 2 (average [2 2])))
  (is (= 5/2 (average [1 2 3 4])))
)

(deftest test-multi-recall)

(deftest test-recall)

(deftest test-recall-sensible)

(deftest test-multi-precision)

(deftest test-precision)

(deftest test-precision-sensible)

(deftest test-vals-to-sets
  (is (= { :a #{}, :b #{}, :c #{}, :d #{1}, :e #{1 2}, :f #{3 4} }
         (vals-to-sets { :a (), :b [], :c nil, :d '(1) :e '(1 2), :f [3 4] })
  ))
  (is (= {} (vals-to-sets {})))
)
