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
  [f m doc sig t & body]
  `(deftype ~t
     ~(conj sig 'm)
     ~doc
     IFn
     (call [_#] (.invoke ~f m#))
     (run [_#] (.invoke ~f m#))
     (applyTo [_# args#] (apply ~f m# args#))
     (invoke [_#] (.invoke ~f m#))
       ~@(for [n (range 1 20)]
            `(invoke ~(make-arglist n [])
                (apply ~f  m# ~(make-arglist n))))
     (invoke ~(make-arglist 20 [])
       (apply ~f m# ~(make-arglist 20 [])))
     ;; TODO - hash-map interface goes here
     ~@body))

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
