(ns clojure-brave-and-true.chapter4
  (:gen-class))
(use 'clojure.pprint)

(def filename "chapter4-suspects.csv")
(def vamp-keys [:name :glitter-index])
(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})
(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(defn map-from-reduce
  "An implementation of map using reduce"
  [f coll]
  (reduce #(conj %1 (f %2)) [] coll))



(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map-from-reduce #(% numbers) [sum count avg]))

(defn exercise1
  [people]
  (map #(:name %) people))


(defn append
  "The answer for exercise 2"
  [suspects suspect]
  (conj suspects suspect))


(defn validate
  "The answer for exercise 3"
  [validators record]
  (reduce ((fn [is-valid record-field]
            body))
    true
    record))



(defn -main
  [& args]
  (clojure.pprint/pprint (stats [80 1 44 13 6])))
