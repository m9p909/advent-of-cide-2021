
(require '[clojure.string :as str])
(def raw-input (slurp "input.txt"))

(def plays (map read-string (str/split (first (str/split-lines raw-input)) #",")))
(def boards (-> raw-input
                str/split-lines
                (subvec 2)))

(defn grid-reducer [prev current]
  (let [current (read-string (str "[ " current " ]"))]
    (cond
      (and (empty? current) (= (count (:current prev)) 5))
      (update
       (update prev :boards (fn [boards]
                              (conj boards (:current prev))))
       :current (fn [_] '[]))
      (not (empty? current))
      (update prev :current (fn [current-board]
                              (conj current-board current))))))

(def list-of-boards
  (let [{:keys [current boards]} (reduce grid-reducer {:current '[] :boards '[]} boards)]
    (conj boards current)))
