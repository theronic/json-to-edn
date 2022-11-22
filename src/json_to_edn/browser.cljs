(ns json-to-edn.browser
  (:require [cljs.pprint :as pprint]))

(defn json-string->edn [s]
  (try
    (-> (js/JSON.parse s)
      (js->clj :keywordize-keys true))
    (catch js/Error ex
      (js/console.error ex)
      (pr-str ex))))

(defn pretty-print [data]
  (with-out-str (pprint/pprint data)))

(defn ^:export convert []
  (let [input  (js/document.getElementById "input")
        output (js/document.getElementById "output")]
    (->> (.-value input)
      (json-string->edn)
      (pretty-print)
      (set! (.-value output)))))

(defn ^:export init []
  (-> (js/fetch "/input.json")
    (.then (fn [resp] (.json resp)))
    (.then (fn [json]
             (js/console.log json)
             (set! (.-value (js/document.getElementById "input"))
               (js/JSON.stringify json)))));(pretty-print (js->clj json))))))
  (convert))