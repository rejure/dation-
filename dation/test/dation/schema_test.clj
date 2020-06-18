(ns dation.schema-test
  (:require [clojure.test :refer [deftest testing is] :as t]
            [dation.schema :as ds]))

(deftest test-read-edn
  (testing "serializes edn literals correctly"
    (testing "#attr converts shorthand vector to datomic attribute map"
      (testing "form: [ident type cardinality]"
        (is (= (ds/read-edn "[#db/attr [:user/username :db.type/string :db.cardinality/one]]")
               [{:db/ident       :user/username
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}])))
      (testing "form: [ident type cardinality unique]"
        (is (= (ds/read-edn "[#db/attr [:user/username :db.type/string :db.cardinality/one :db.unique/identity]]")
               [{:db/ident       :user/username
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}])))
      (testing "form: [ident type cardinality unique pred]"
        (is (= (ds/read-edn "[#db/attr [:user/username :db.type/string :db.cardinality/one :db.unique/identity foo]]")
               [{:db/ident       :user/username
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity
                 :db.attr/preds  'foo}]))))
    (testing "#ent converts shorthand vector to datomic attribute map"
      (testing "form: {ident attrs-map}"
        (is (= (ds/read-edn "#db/ent #:user{:username {:db/valueType  :db.type/string
                                                  :db/cardinality :db.cardinality/one}}")
               [{:db/ident       :user/username
                 :db/valueType  :db.type/string
                 :db/cardinality :db.cardinality/one}])))
      (testing "form: {ident attrs-vector}"
        (is (= (ds/read-edn "[#db/ent #:user{:username [:db.type/string :db.cardinality/one]}]")
               [[{:db/ident       :user/username
                  :db/valueType  :db.type/string
                  :db/cardinality :db.cardinality/one}]]))))))
