## I was bored so I did a prime number thing

### That's basically it.

I started in Clojure, as an attempt to better familiarize myself with the Sequence library. Then I
wrote the rough equivalent in C to see how it compared in performance. Then I wrote it in Java for
great science.

Interestingly the Java version using Arrays is faster than the C version, when compiled without
optimizations. C was faster with -O2, but only barely.

My initial Clojure implementation is about 1000 times slower than the Java or C implementation... 
I used the Seq library pretty heavily, which while elegant is not really ideal for this kind of
quick number crunching. Of course, the lazy-seq is cached, so it only has to calculate values up to
a given N once.

After the initial benchmarking, I emulated the C/Java algorithm in Clojure and the results were much
bettter. It's still slower, but more reasonable. Just for kicks, I also implemented it using actual
Java Arrays, but it wasn't significantly different.

It's important to note, however, that the non-seq Clojure implementation was considerably easier to
write than its C/Java analog. Despite that it's essentially the same algorithm, there was almost no
state management to deal with, definitely no pointers or allocations to screw up, and the loop/recur
form is much simpler to reason about than a for loop.

For comparison's sake, it took me only about 20-30 minutes to hammer out both iterative-style
Clojure functions, compared to several hours using C, spent tracking down off-by-one errors and
refactoring, and about 30-40 minutes on each version in Java (although that was largely spent
reading docs. I don't know Java well). In the end, the C version works, but occasionally gives me
negative benchmark times, and the Java version is unreadably verbose. Maybe I'm biased, but I think
Clojure is the clear winner here.

### Some results

* I'm on a lastest-gen MBP 15" with the 2.4 GHz i7 and 8G of RAM.
* Java: 1.6.0 64-bit with HotSpot
* Clojure: 1.3.0 stable
* GCC: 4.2.1, Apple build 
* LLVM-GCC: 4.2.1, LLVM 2335.15.00

Clojure using seq: (I re-def'd #'primes between each run to kill the memoization)

```clojure
(time (print-primes 10000))   ;=> 1079.706ms
(time (print-primes 100000))  ;=> 58769.652
(time (print-primes 1000000)) ;=> C-c I gave up :-/
```

Clojure using Vectors and loop/recur:

```clojure
(time (print-large-vec (iterative-primes 10000)))    ;=> 16.081ms
(time (print-large-vec (iterative-primes 100000)))   ;=> 274.732ms
(time (print-large-vec (iterative-primes 1000000)))  ;=> 5254.079ms
(time (print-large-vec (iterative-primes 10000000))) ;=> 114479.498ms
```

Clojure using Java arrays:

```clojure
(time (java-primes 10000))    ;=> 23.119ms
(time (java-primes 100000))   ;=> 230.103ms
(time (java-primes 1000000))  ;=> 4789.715ms
(time (java-primes 10000000)) ;=> 104944.505ms
```

Java with Vector:

```bash
java jon.Primes 10000 vector -p    #=> 16ms
java jon.Primes 100000 vector -p   #=> 44ms
java jon.Primes 1000000 vector -p  #=> 382ms
java jon.Primes 10000000 vector -p #=> 7411ms
```

Java with Array:

```bash
java jon.Primes 10000 array -p    #=> 12ms
java jon.Primes 100000 array -p   #=> 25ms
java jon.Primes 1000000 array -p  #=> 90ms
java jon.Primes 10000000 array -p #=> 1304ms
```

GCC without optimization:

```bash
./primes 10000 -p    #=> 1ms
./primes 100000 -p   #=> 10ms
./primes 1000000 -p  #=> 131ms
./primes 10000000 -p #=> 2495ms (The internal timing started printing garbage, so
                     #           I used /usr/bin/time to get this)
```

GCC with -02:

```bash
./primes 10000 -p    #=> 0ms
./primes 100000 -p   #=> 6ms
./primes 1000000 -p  #=> 67ms
./primes 10000000 -p #=> 1263ms (/usr/bin/time)
```

LLVM (timings were the same with and without optimizations):

```bash
./primes 10000 -p    #=> 0ms
./primes 100000 -p   #=> 12ms
./primes 1000000 -p  #=> 127ms
./primes 10000000 -p #=> 2467ms (/usr/bin/time)
```
