(ns clojure-brave-and-true.chapter5 (:gen-class))

(use 'clojure.pprint)

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(c-int character)

(defn attr
  "Exercise 1"
  [attr-name]
  (comp attr-name :attributes)
  )

((attr :farts) character)


(defn my-comp
  "Exercise 2: Implementing comp"
  ([f] f)
  ([f & fs]
  (fn [& args] (f (apply (apply my-comp fs) args))))

)


((my-comp :dexterity :attributes) character)

((my-comp +) 2 3 4 5)


(assoc {} :hello :world)
(my-assoc-in {:that :right} [:is :this :hello :world] :yes)
(defn my-assoc-in
  "Exercise 3"
  [m [k & ks] v]
  (loop
    [map m
     key k
     keys ks
     val v]
  (if (empty? keys)
    (assoc map key val)
    (assoc map key (my-assoc-in (get map key {}) keys val))
    )
  )
)

(my-update-in {:a {:b {:c 2}}} [:a :b :c] inc)



(defn my-update-in
  "Exercise 5"
  [m [k & ks] f & args]
  (let
    [map m
     key k
     keys ks
     fn f
     args args ]
  (if (empty? keys)
    (let
      [converter (partial fn (get map key))]
        (assoc map key (apply converter args))
    )
    (let
      [updater (partial my-update-in (get map key {}) keys fn)]
      (assoc map key (apply updater args))
      )
    )
  )
)




((fn [m [k & ks] f & args] (apply (partial f 2) args)) {:a {:b {:c 2}}} [:a :b :c] + 3)

(apply (partial + 3) '(5 4))
(partial + 3)
(seq? '(4))



(defn -main [& args] (clojure.pprint/pprint '(nil)))
