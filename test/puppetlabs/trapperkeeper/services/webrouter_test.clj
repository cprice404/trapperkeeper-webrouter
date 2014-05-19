(ns puppetlabs.trapperkeeper.services.webrouter-test
  (:require [clojure.test :refer :all]
            [puppetlabs.http.client.sync :as http-client]
            [puppetlabs.trapperkeeper.core :as tk]
            [puppetlabs.trapperkeeper.testutils.bootstrap :refer [with-app-with-config]]))

(defn http-get
  [url]
  (http-client/get url {:as :text}))

(defn ring-handler
  [body]
  (fn [req]
    {:status 200
     :body body}))

(def foo1 (ring-handler "foo1"))
(def foo2 (ring-handler "foo2"))
(def bar (ring-handler "bar"))

(tk/defservice foo-web-service
  [[:WebRouterService add-ring-handler]]
  (init [this context]
        (add-ring-handler foo1 this :foo1)
        (add-ring-handler foo2 this :foo2)))

(tk/defservice bar-web-service
  [[:webRouterService add-ring-handler]]
  (init [this context]
        (add-ring-handler bar this)))

(deftest test-webrouter-service
  (testing "router config properly configures web context paths"
    (with-app-with-config app
      [foo-web-service bar-web-service webrouter-service]
      {:webserver {:port        8080}
       :webrouter {"puppetlabs.trapperkeeper.services.webrouter-test/foo-web-service"
                    {:foo1 "/foo1"
                     :foo2 "/foo2"}
                   "puppetlabs.trapperkeeper.services.webrouter-test/bar-web-service"
                    "/bar"}}
      (let [response (http-get "http://localhost:8080/foo1")]
        (is (= (:status response) 200))
        (is (= (:body response) "foo1")))
      (let [response (http-get "http://localhost:8080/foo2")]
        (is (= (:status response) 200))
        (is (= (:body response) "foo2")))
      (let [response (http-get "http://localhost:8080/bar")]
        (is (= (:status response) 200))
        (is (= (:body response) "bar"))))))
