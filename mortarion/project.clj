(defproject mortarion "0.1.0-SNAPSHOT"
  :plugins  [[cider/cider-nrepl "0.26.0"]]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [cljfmt "0.8.0"]
                 [ring/ring-core "1.9.4"]
                 [ring/ring-jetty-adapter "1.9.4"]
                 [bidi "2.1.6"]]
  :repl-options {:init-ns mortarion.core})
