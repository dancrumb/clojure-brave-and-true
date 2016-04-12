(ns clojure-brave-and-true.chapter7 (:gen-class))

(use 'clojure.pprint)

(defn exercise-1 [] (eval (list (read-string "print") "Dan Rumney:" "Shaun of the Dead")))

(def operators {'+ 1, '- 1, '* 2, '/ 2})

(defn operator-precedence [op] (get operators op))

(defn operator? [op] (or (= op '+) (= op '-) (= op '*) (= op '/)))
(defn operand? [token] (not (operator? token)))


(defn push [c v] (conj c v))
(defn pop [c] [(first c) (rest c)])
(defn top [c] (first c))


(defn build-operations
  [operators operands done? when-done]
  (loop [operators operators
         operands operands]
    (if (done? operators operands)
      (when-done operators operands)
      (let [[operator operators] (pop operators)
            [rightOperand operands] (pop operands)
            [leftOperand operands] (pop operands)]
        (recur operators (push operands (list operator leftOperand rightOperand)))
        )
      )
    )
  )




(defn resolve-ops-lists
  [operators operands]
  (build-operations operators operands (fn [ops _] (empty? ops)) #(top %2))
  )

(resolve-ops-lists '(/ - +) '(2 5 4 1))

(defn infix-to-list
  "Exercise 2"
  [symbols]
  (loop [operators '()
         operands  '()
         symbols symbols
         ]
    (if (empty? symbols)
      (resolve-ops-lists operators operands)
      (let
        [[token symbols] (pop symbols)]
        (cond
          (operand? token)
          (recur operators (push operands token) symbols)
          (or  (empty? operators) (> (operator-precedence token) (operator-precedence (top operators))))
          (recur (push operators token) operands symbols)
          (<= (operator-precedence token) (operator-precedence (top operators)))
          (let [[operators operands]
                (build-operations
                  operators
                  operands
                  (fn [ops _] (or (empty? ops) (> (operator-precedence token) (operator-precedence (top ops)))))
                  vector)
                ]
            (recur (push operators token) operands symbols)
            )
          )
        )
      )
    )
  )


(infix-to-list '(1 + 3 * 4 - 5))


(defn -main [& args] (exercise-1))


