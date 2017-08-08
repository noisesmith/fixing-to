(ns org.noisesmith.fixing-to
  (:import (clojure.lang IFn)))

(defn make-arglist
  ([n] (make-arglist n [] nil))
  ([n init] (make-arglist n init nil))
  ([n init after]
   (into (into init
               (map #(symbol (str "arg" % "#")))
               (range 1 (inc n)))
         after)))

(defmacro def-fixing-to
  "creates a record instance which acts like a function,
   with the body of invoke being compiled as its body, with an
   implicit args field containing a variable list of all args
   provided.

   For simplicity, the doc argument is mandatory.

   The etc arg allows implementing other protocols or interfaces.

   Does not support multiple arities, instead put keys on the record."
  [f-name doc fields invoke & etc]
  (let [this (gensym "this")
        args (gensym "args")
        record-name (symbol (str 'Fixing_ (munge f-name) 'Record))]
    `(do
       (defrecord ~record-name ~(conj fields 'args)
         IFn
         (call [this#] (.invoke this#))
         (run [this#] (.invoke this#))
         (applyTo [this# args#] (.invoke (assoc this# :args args#)))
         (invoke
           ~@invoke)
         ~@(for [n (range 1 21)]
             `(invoke ~(make-arglist n [this])
                      (.invoke (assoc ~this :args ~(make-arglist n)))))
         (invoke ~(make-arglist 20 [this] [args])
           (.invoke (assoc ~this :args
                           (into ~(make-arglist 20 []) ~args))))
         ~@etc)
       (def ~f-name
         ~doc
         (~(symbol (str "map->" record-name)) {:doc ~doc})))))

(comment (def-fixing-to foo
           "foo has keys a and b and prints the values of those keys,
             along with any additional args provided."
           [a b]
           ([this]
            (apply println a b args)))
         (assoc foo :a 0 :b 1)
         ;; => #user.Fixing_fooRecord{:a 0, :b 1, :args nil, :doc "a foo"}
         ((assoc foo :a 0 :b 1) :a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :q :r :s :t :u :v :w :x :y :z)
         ;; 0 1 :a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :q :r :s :t :u :v :w :x :y :z
         ;; => nil
)

(deftype AffixedFn
         [m f]
  Object
  (toString [this]
    (str f " affixing " m)))
