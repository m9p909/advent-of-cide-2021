(def input (slurp "input.txt"))
(require '[clojure.string :as str])
(require '[clojure.edn :as edn])

(print (reduce 
  (fn [prevMap current] 
    (update 
      (if 
        (< 
          (:prev prevMap) 
          current) 
        (update prevMap :sum #(inc %))
        prevMap) 
    :prev (fn [_] current)))


  {:prev 100000000000 :sum 0} 

  (map #(edn/read-string %) 
       (clojure.string/split-lines input))))
