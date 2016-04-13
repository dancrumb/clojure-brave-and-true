(ns clojure-brave-and-true.chapter8 (:gen-class))

(use 'clojure.pprint)

(def order-details
  {:name "Mitchard Blimmons"
   :email "mitchard.blimmonsgmail.com"})

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]

   :email
   ["Please enter an email address" not-empty

    "Your email address doesn't look like an email address"
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

(defmacro if-valid
  "Handle validation more concisely"
  [to-validate validations errors-name & then-else]
  `(let [~errors-name (validate ~to-validate ~validations)]
     (if (empty? ~errors-name)
       ~@then-else)))

 (macroexpand
 '(if-valid order-details order-details-validations my-error-name
            (println :success)
            (println :failure my-error-name)))

 (defmacro when-valid
   "Exercise 1: Like 'if-valid' but with 'when'"
   [to-validate validations & body]
   `(let [errors# (validate ~to-validate ~validations)]
     (if (empty? errors#)
       (do
         ~@body)))
   )

 (defmacro and
  "Evaluates exprs one at a time, from left to right. If a form
  returns logical false (nil or false), and returns that value and
  doesn't evaluate any of the other expressions, otherwise it returns
  the value of the last expr. (and) returns true."
  {:added "1.0"}
  ([] true)
  ([x] x)
  ([x & next]
   `(let [and# ~x]
      (if and# (and ~@next) and#))))

 (defmacro or
   "Exercise 2: Implement the or macro"
   ([] nil)
   ([x] x)
   ([x & next]
    `(let [or# ~x]
       (if or# or# (or ~@next))))

   )

 (def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

 (defmacro defattrs
   ([] nil)
   ([fn-name attr]
    `(def ~fn-name (comp ~attr :attributes))
   )
   ([fn-name attr & rest]
    `(do
       (defattrs ~fn-name ~attr)
       (defattrs ~@rest))
    )
   )

(macroexpand `(defattrs c-int :intelligence
          c-str :strength
          c-dex :dexterity))
(defattrs c-int :intelligence
          c-str :strength
          c-dex :dexterity)

(c-str character)



(defn -main [& args] (when-valid order-details order-details-validations
                (println "It's a success!")
 (println :success)))


