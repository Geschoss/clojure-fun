(ns mortarion.middleware)

(defn wrap-echo [func]
  (fn [& args]
    (apply println "the args are" args)
    (let [result (apply func args)]
      (println "The result is" result)
      result)))

(defn wrap-catch [func]
  (fn [& args]
    (try
      (apply func args)
      (catch Throwable e
        e))))