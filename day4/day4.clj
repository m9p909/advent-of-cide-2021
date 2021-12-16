
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


(def list-of-boards-map (mapv (fn [board]
                               (mapv (fn [row]
                                      (mapv (fn [value]
                                             {
                                              :marked false
                                              :value value}
                                              )
                                            row))
                                     board))
                              list-of-boards))

(defn play-value [boards play]
  (mapv (fn [board]
        (mapv (fn [row]
                (mapv (fn [data]
                        (if (= (:value data) play)
                          (update data :marked  (fn [_] true))
                          data))
                      row))
              board))
        boards))


(defn check-rows-for-win [board]
  (not
   (empty?
    (filter
     (fn [row]
            (reduce
             (fn [prev current]
               (and prev (:marked current)))
             true
             row))
          board))))

(defn rotate-2d-vector [vector]
  (vec
   (for [index (range (count (first vector)))]
    (reduce (fn [prev current]
              (conj prev (get current index)))
            []
            vector))))

(defn check-for-wins [boards]
  (reduce (fn [prev current]
          (if (or
               (check-rows-for-win current)
               (check-rows-for-win (rotate-2d-vector current)))
            (conj prev current)
            prev)
            )
          []
          boards))

(defn printpass [value]
  (println value)
  value)

(def resultdata
  (loop [play-boards list-of-boards-map
       plays plays]
  (let [play-boards (play-value play-boards (first plays))
        winner (first (check-for-wins play-boards))]
    (if (or (not (nil? winner)) (empty? plays))
      {:winner winner :lastplay (first plays)}
      (recur play-boards (rest plays))))))

(defn sumUnmarked [resultdata] (as-> (:winner resultdata) data
                   (flatten data)
                   (reduce (fn [prev curr]
                             (if (not(:marked curr))
                               (+ prev (:value curr))
                               prev))
                           0
                           data)))
(defn get-result [resultdata] (* (sumUnmarked resultdata) (:lastplay resultdata)))
(println "part1 " (get-result resultdata))

(def list-of-boards-map (mapv (fn [board] {:id (rand-int 10000000)
                                          :board board}) list-of-boards-map))
(defn play-value [boards play]
  (mapv (fn [board]
          {:board
           (mapv (fn [row]
                (mapv (fn [data]
                        (if (= (:value data) play)
                          (update data :marked  (fn [_] true))
                          data))
                      row))
                 (:board board))
           :id (:id board)})
        boards))

(defn check-for-wins [boards]
  (reduce (fn [prev current]
          (if (or
               (check-rows-for-win (:board current))
               (check-rows-for-win (rotate-2d-vector (:board
                                                      current))))
            (conj prev current)
            prev))
          []
          boards))



(def resultdata2
  (loop [play-boards list-of-boards-map
         plays plays
         last-winner nil
         last-play nil]
  (let [play-boards (play-value play-boards (first plays))
        winners (check-for-wins play-boards)
        winner-not-nil (not (empty? winners))]
    (if (empty? plays)
      {:winner last-winner :lastplay last-play}
      (if winner-not-nil
        (recur (remove #(contains? (set (map :id winners)) (:id %)) play-boards)
               (rest plays)
               (:board (first winners))
               (first plays))
        (recur play-boards (rest plays) last-winner last-play))))))

(println "part 2" (get-result resultdata2))
