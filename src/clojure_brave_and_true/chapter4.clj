(ns clojure-brave-and-true.chapter4 (:gen-class))

(use 'clojure.pprint)

(def filename "chapter4-suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int [str] (Integer. str))

(def conversions {:name identity, :glitter-index str->int})
(def validators {:name string?, :glitter-index number?})

(defn convert [vamp-key value] ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn
 mapify
 "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
 [rows]
 (map
  (fn
   [unmapped-row]
   (reduce
    (fn [row-map [vamp-key value]] (assoc row-map vamp-key (convert vamp-key value)))
    {}
    (map vector vamp-keys unmapped-row)))
  rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(defn
 map-from-reduce
 "An implementation of map using reduce"
 [f coll]
 (reduce #((conj %1 (f %2 [] coll)))))

(def sum #((reduce + %)))

(def avg #((/ (sum %) (count %))))

(defn stats [numbers] (map-from-reduce #((% numbers)) [sum count avg]))

(defn exercise1 [people] (map #((:name %) people)))

(defn append "The answer for exercise 2" [suspects suspect] (conj suspects suspect))

(defn not-nil?
  [val] ((complement nil?) val))

; validators is a map of fieldnames to validation functions
; record is the record that needs validation
(defn
 validate
 "The answer for exercise 3"
 [validators record]
 (reduce
  (fn
   [is-valid vamp-key] (and is-valid (not-nil? (get record vamp-key)) ((get validators vamp-key) (get record vamp-key))))
  true
  vamp-keys))

(defn to-csv
  "The answer for exercise 4"
  [folks]
  (clojure.string/join "\n" (map #(clojure.string/join "," [(:name %) (:glitter-index %)]) folks)))

(defn -main [& args] (clojure.pprint/pprint (mapify (parse (slurp filename)))))

(mapify (parse (slurp filename)))
