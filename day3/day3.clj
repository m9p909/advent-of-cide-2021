
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

(defn sum-columns [input]
  (reduce sum-frequencies-reducer emptyvector input))

(def totals (sum-columns input))

(defn get-binary-vec [column-sums num-rows]
  (mapv (fn [total] (if (>= total (/ num-rows 2)) 1 0)) column-sums))

(def binary-vec (get-binary-vec totals num-rows))

(defn binary-vec-to-decimal [ binary-vec]
  (reduce (fn [prev index]
            (+ prev (if
                        (= 1 (get binary-vec (- row-length (inc index))))
                      (Math/pow 2 index)
                      0)))
          0
        (reverse (take row-length (range)))))

(defn invert-binary-vector [binary-vector]
  (mapv #(if (= 1 %) 0 1) binary-vector))

(def gamma (binary-vec-to-decimal binary-vec))
(def epsilon (binary-vec-to-decimal (invert-binary-vector binary-vec)))

(println "part 1" (* gamma epsilon)) ; part 1

(defn char-to-int [c]
  (-> c int (- 48)))

(defn filter-data [binary-vec dataset n]
  (filter (fn [str-line]
                  (= (char-to-int (get str-line n)) (get binary-vec n)))
                    dataset))

(defn print-and-continue [value]
  (println value)
  value)


(defn get-part-2 [dataset binary-modifier]
  (loop [dataset input
         index 0]
    (if (or (>= index row-length) (<= (count dataset) 1)) dataset
      (-> dataset
          sum-columns
          (get-binary-vec (count dataset))
          binary-modifier
          (filter-data dataset index)
         (recur (inc index))))))

(defn string-to-binary-vec [string-value]
  (as-> string-value data
   (str/split data #"")
   (mapv #(Integer/parseInt %) data)))

(def oxygen (get-part-2 input identity))
(def co2 (get-part-2 input invert-binary-vector))

(def oxygen-int (-> oxygen first string-to-binary-vec binary-vec-to-decimal))
(def co2-int (-> co2 first string-to-binary-vec binary-vec-to-decimal))

(println "part 2:" (* oxygen-int co2-int))
