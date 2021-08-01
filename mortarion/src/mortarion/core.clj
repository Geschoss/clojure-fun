(ns mortarion.core)

(require '[ring.adapter.jetty :refer [run-jetty]])
(require '[ring.middleware.params :refer [wrap-params]])
(require '[ring.middleware.keyword-params :refer [wrap-keyword-params]])
(require '[bidi.bidi :as bidi])

(defonce server (run-jetty #'app {:port 8080 :join? false}))

(def app
  (->  app-naked
       wrap-params+))

(def wrap-params+
  (comp wrap-params wrap-keyword-params))

(def app-naked (wrap-handler multi-handler))

(def routes
  ["/" {"" :page-index
        "hello" :page-hello
        true :not-found}
   ["/content/order/" :id] {"/view" {:get :page-view}
                            "/edit" {:get :page-form}}])

(defn wrap-handler [handler]
  (fn [request]
    (let [{:keys [uri]} request
          request* (bidi/match-route* routes uri request)]
      (handler request*))))

(defmulti multi-handler
  :handler)

(defmethod multi-handler :page-index
  [request]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body "Learning Clojure"})

(defmethod multi-handler :page-hello
  [request]
  (let [who (get-in request [:params :who])]
    {:status 200
     :headers {"content-type" "text/plain"}
     :body (str "Hi there " who "! Keep trying!")}))

(defmethod multi-handler :not-found
  [request]
  {:status 404
   :headers {"content-type" "text/plain"}
   :body "Page not found"})

(defmethod multi-handler :page-view
  [request]
  (let [{:keys [params route-params]} request
        {order-id :id} route-params]
    {:status 200
     :headers {"content-type" "text/plain"}
     :body (str "Your order is " order-id)}))

(defmethod multi-handler :page-form
  [request]
  (let [{:keys [params route-params]} request
        {order-id :id} route-params]
    {:status 200
     :headers {"content-type" "text/plain"}
     :body (str "Form for order " order-id)}))

(comment
  (.stop server)
  (.start server))