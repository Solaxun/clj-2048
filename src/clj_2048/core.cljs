(ns clj-2048.core
    (:require [reagent.core :as reagent :refer [atom]]
              [clj-2048.twenty :as game]))

(enable-console-print!)

(println "This text is printed from src/clj-2048/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:grid (game/new-board)}))


(defn link []
  [:a {:href "http://www.google.com"} "Google!"])

(defn hello-world []
  [:center [:div 
            (for [row (:grid @app-state)]
              [:tr  (for [cell row]
                      [:td {:style {:padding "10px" :border "1px solid black"}} cell])])
            [:button {:on-click #(reset! app-state {:grid (game/new-board)})
                      :style {:background-color "black" :color "white"}} "New Game!"]]])

(defn process-move [e]
  (let [k (.-keyCode e)
        dir ({39 "R" 37 "L" 40 "D" 38 "U"} k)]
    (reset! app-state {:grid (game/move-board dir (:grid @app-state))})))

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app"))
                          (.addEventListener js/window "keydown" process-move))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
