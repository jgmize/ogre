(ns clojurewerkz.ogre.util
  (:require [clojure.reflect :as r])
  (:use [clojure.pprint :only (pprint)])
  (:import (com.tinkerpop.gremlin.process.graph GraphTraversal)
           (com.tinkerpop.gremlin.structure Compare)
           (java.util.function Function Consumer Predicate)))


(defmacro query [xs & body]
  `(-> ~xs ~@body))

(defmacro subquery
  ""
  [& body]
  `(-> ~@body))

(defn ^Compare convert-symbol-to-compare [s]
  (case s
    =    Compare/eq
    not= Compare/neq
    >=   Compare/gte
    >    Compare/gt
    <=   Compare/lte
    <    Compare/lt))

(defn ^"[Ljava.lang.String;" str-array [strs]
  (into-array String strs))

(defn ^"[Ljava.lang.String;" keywords-to-strings [labels]
  (->> labels
    (filter identity)
    (map name)
    str-array))

(defn keywords-to-str-array [strs]
  (into-array String (map name strs)))

(defn prop-map-to-array [m]
  (into-array Object
    (into []
      (flatten
        (map #(let [key (first %)
                    value (second %)]
            (vector (if (keyword? key) (name key) key) value)) m)))))

(defn ^Function f-to-function [f]
  (reify Function
    (apply [this arg] (f arg))))

(defn ^"[Ljava.util.function.Function;" fs-to-function-array
  [fs]
  (into-array Function (map f-to-function fs)))

(defn ^Consumer f-to-consumer [f]
  (reify Consumer
    (accept [this arg] (f arg))))

(defn ^Predicate f-to-predicate [f]
  (reify Predicate
    (test [this arg] (f arg))))
