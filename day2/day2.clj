
(require '[clojure.string :as str])
(require '[clojure.edn :as edn])

(def input (str/split-lines (slurp "input.txt")))

(defn print-and-pass [value]
  (print value)
  value)

(def functionMap {
                  'forward (fn [data number]
                        (update data :x + number))
                  'up (fn [data number]
                         (update data :y - number))
                  'down (fn [data number]
                           (update data :y + number))})

(defn position-reducer [data current]
  (let [input (read-string (str "[" current "]"))
        function (get input 0)
        value (get input 1)]
    (println data)
    ((get functionMap function) data value)))


(let [{x :x y :y} (reduce position-reducer {:x 0 :y 0} input)]
           (println (* x y)))
