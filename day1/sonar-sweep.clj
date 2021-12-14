(def input (slurp "input.txt"))
(require '[clojure.string :as str])
(require '[clojure.edn :as edn])

(def parsed-input
(vec (map #(edn/read-string %)
       (clojure.string/split-lines input))))

(defn count-increases [data]
  (:sum
   (reduce
  (fn [prevmap current]
    (update
      (if
        (<
          (:prev prevmap)
          current)
        (update prevmap :sum #(inc %))
        prevmap)
      :prev
      (fn [_] current)))
  {:prev 100000000000 :sum 0}
  data)
  ))

(def solution1
 (count-increases parsed-input))


(def input-len (count parsed-input))
(def windowed-input (map-indexed
                     (fn [index num]
                       (if (< (- input-len 3) index)
                            0
                            (+
                             num
                             (get parsed-input (inc index))
                             (get parsed-input (+ 2 index)))))
                      parsed-input))

(def solution2 (count-increases windowed-input))
