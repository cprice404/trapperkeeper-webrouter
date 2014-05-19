(def tk-version "0.4.3-SNAPSHOT")
(def ks-version "0.7.1")

(defproject puppetlabs/trapperkeeper-webrouter "0.1.0-SNAPSHOT"
  :description "We are trapperkeeper.  We are one."
  ;; Abort when version ranges or version conflicts are detected in
  ;; dependencies. Also supports :warn to simply emit warnings.
  ;; requires lein 2.2.0+.
  :pedantic? :abort
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.logging "0.2.6"]
                 [puppetlabs/trapperkeeper ~tk-version]]

  :profiles {:dev {:dependencies [[puppetlabs/http-client "0.1.7"]
                                  [puppetlabs/kitchensink ~ks-version :classifier "test"]
                                  [puppetlabs/trapperkeeper ~tk-version :classifier "test"]
                                  [spyscope "0.1.4"]]
                   :injections [(require 'spyscope.core)]}})
