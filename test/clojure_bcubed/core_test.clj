(ns clojure-bcubed.core-test
  (:require [clojure.test :refer :all]
            [clojure-bcubed.core :refer :all]))

(def categories {
  :item1 #{"black"}
  :item2 #{"gray"}
  :item3 #{"gray"}
  :item4 #{"black"}
  :item5 #{"black"}
  :item6 #{"dashed"}
  :item7 #{"dashed"}
  }
)
(def clusters {
  :item1 #{"A" "B"}
  :item2 #{"A" "B"}
  :item3 #{"A"}
  :item4 #{"B"}
  :item5 #{"B"}
  :item6 #{"C"}
  :item7 #{"C"}
  }
)

(deftest test-averager
  (is (= 2  (transduce identity averager (range 5))))
  (is (= 9/2 (transduce identity averager (range 10))))
  (is (= 6172  (transduce identity averager (range 12345))))
)

(deftest test-multi-recall
  (is (= 1.0 (multi-recall categories clusters :item2 :item3)))
  (is (= 1.0 (multi-recall categories clusters :item1 :item4)))
)

(deftest test-recall
  (is (= 1.0 (recall categories clusters)))
)

(deftest test-recall-sensible
  (is (= 1.0 (recall-sensible categories clusters)))
)

(deftest test-multi-precision
  (is (= 1.0 (multi-precision categories clusters :item2 :item3)))
  (is (= 0.0 (multi-precision categories clusters :item2 :item5)))
)

(deftest test-precision
  (is (= 0.64 (precision categories clusters)))
)

(deftest test-precision-sensible
  (is (= 0.5555555555555556 (precision-sensible categories clusters)))
)

(deftest test-vals-to-sets
  (is (= { :a #{}, :b #{}, :c #{}, :d #{1}, :e #{1 2}, :f #{3 4} }
         (vals-to-sets { :a (), :b [], :c nil, :d '(1) :e '(1 2), :f [3 4] })
  ))
  (is (= {} (vals-to-sets {})))
)
