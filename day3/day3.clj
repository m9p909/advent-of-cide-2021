
(require '[clojure.string :as str])
(def input (str/split-lines (slurp "input.txt")))

(def row-length (count (get input 0)))
(def num-rows (count input))

(defn sum-frequencies-reducer [prev current]
  (vec (map-indexed
   (fn [index item]
     (+ (Integer/parseInt item) (get prev index)))
   (str/split current #""))))

(def emptyvector (vec (take row-length (repeat 0))))

(def totals (reduce sum-frequencies-reducer emptyvector input))

(def binary-vec (mapv (fn [total] (if (> total (/ num-rows 2)) 1 0)) totals))

(defn binary-vec-to-decimal [ binary-vec]
  (reduce (fn [prev index]
            (println "prev" prev)
            (+ prev (if
                        (= 1 (get binary-vec (- row-length (inc index))))
                      (Math/pow 2 index)
                      0)))
          0
        (reverse (take row-length (range)))))

(def gamma (binary-vec-to-decimal binary-vec))
(def epsilon (binary-vec-to-decimal (mapv #(if (= 1 %) 0 1) binary-vec)))

(print (* gamma epsilon))
