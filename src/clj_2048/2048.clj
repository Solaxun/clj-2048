(ns clj-2048.2048
  (:require [clojure.string :as str]))

(defn rand-square []
  (rand-nth [2 2 2 4]))

(defn empty-square-coords [board]
  (apply concat
         (map-indexed (fn [r row]
                        (map-indexed (fn [c col] (if (= " " col) [r,c] nil))
                                     row))
                      board)))

(defn place-new-square [board]
  (as-> board b
        (empty-square-coords b)
        (remove nil? b)
        (rand-nth b)
        (assoc-in board b (rand-square))))

(defn new-board []
  (let [empty-board (vec (repeat 4 (vec (repeat 4 " "))))]
    (nth (iterate place-new-square empty-board) 4)))

(defn pad-slice [slice]
  (let [padding (repeat (- 4 (count slice)) " ")]
    (vec (concat padding slice))))
       
(defn merge-right [slice]
  (loop [s (remove #(= " " %) slice),  r '()]
    (let [cur (last s), prev (last (drop-last s))]
      (cond (empty? s) (pad-slice r)
            (= " " cur) (recur (drop-last 1 s) r)
            (= cur prev) (recur (drop-last 2 s) (conj r (+ cur prev)))
            :else (recur (drop-last s) (conj r cur))))))
       
(defn merge-left [slice]
  (let [shifted (vec (reverse (merge-right (vec (reverse slice)))))]
    shifted))

(defn transpose [grid]
  (apply mapv vector grid))

(defn move-board [dir board]
  (cond (= dir "R") (mapv merge-right board)
        (= dir "L") (mapv merge-left board)
        ;; when i'm less dumb, figure out why i thought the logic for down and up was reversed
        (= dir "D") (transpose (mapv merge-right (transpose board)))
        (= dir "U") (transpose (mapv merge-left (transpose board)))
        :else (println "Error: you entered:"  dir " Choose only U, L, D , or R!"))) 

(defn move [dir board]
  (->> board
      (move-board dir)
      (place-new-square)))
     
(defn game-over? [board]
  (empty? (filter (complement nil?) 
                  (empty-square-coords board))))
