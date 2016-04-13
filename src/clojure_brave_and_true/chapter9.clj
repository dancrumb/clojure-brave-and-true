(ns clojure-brave-and-true.chapter9 (:gen-class))

(use 'clojure.pprint)

(def default-search-engines ["https://google.com/search?q%3D" "https://www.bing.com/search?q%3D"])

(defn search
  "Exercises 1 & 2"
  ([query] (search query default-search-engines))
  ([query search-engines]
  (let [result-promise (promise)]
    (doseq [engine search-engines]
      (future (deliver result-promise (slurp (str engine query))
                       )
        )
      )
    @result-promise
    )
  )
  )

(re-seq #"https?://[^\"]*" (search "clojure"))

(defn promised-request
  [term search-engine]
  (let [url (str search-engine term)
        request-promise (promise)]
    (future (deliver request-promise (slurp url)))
    request-promise
    )
  )

(defn get-urls
  [source]
  (re-seq #"https?://[^\"]*" source)
)

(deref (promised-request "clojure" "https://www.bing.com/search?q%3D"))

(defn search
  "Exercise 3"
  [term search-engines]
  (vec (flatten (map #(get-urls (deref %)) (map #(promised-request term %) search-engines))))
)


(search "clojure" default-search-engines)

(defn -main [& args] (println :chapter9))


