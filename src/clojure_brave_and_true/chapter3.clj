(ns clojure-brave-and-true.chapter3
  (:gen-class))
(use 'clojure.pprint)

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-parts
  [part multiplier]
  (if (re-find #"^left-" (:name part))
    (multiplier
      {
        :name (clojure.string/replace (:name part) #"^left-" "")
        :size (:size part)})


    part))



(defn to-seq
  [thing]
  (if (seq? thing)
    thing
    [thing]))



(defn multiply-by
  [multiple]
  #(repeat multiple %))


(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn
           [parts part]
           (reduce (fn [parts part] (cons part parts)) parts (to-seq (matching-parts part (multiply-by 5)))))
   [] asym-body-parts))


(defn -main
  [& args]
  (clojure.pprint/pprint (symmetrize-body-parts asym-hobbit-body-parts)))
