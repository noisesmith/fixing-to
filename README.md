# fixing-to

A Clojure data type that acts like a function that's also a hash map.

## Usage

   (def-fixing-to foo
      "foo has keys a and b and prints the values of those keys,
       along with any additional args provided."
      [a b]
     ([this]
      (apply println a b args)))
   (assoc foo :a 0 :b 1)
   => #user.Fixing_fooRecord{:a 0, :b 1, :args nil, :doc "a foo"}
   ((assoc foo :a 0 :b 1) :a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :q :r :s :t :u :v :w :x :y :z)
   => 0 1 :a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :q :r :s :t :u :v :w :x :y :z
   nil)

## License

Copyright © 2017 Justin Glenn Smith noisesmith@gmail.com

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
