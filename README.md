## I was bored so I did a prime number thing

### That's basically it.

I started in Clojure, just for kicks. Then I wrote the rough equivalent in C 
to see how it compared in performance. Then I wrote it in Java for great science.

Interestingly the java version using Arrays is faster than the C version, compiled
without optimizations. C was faster with -O2 though. Clojure is about 1000 times slower
than the Java or C implementation... I don't expect it to be as fast, of course, but I'm
sure there must be some better optimized functions I can use. Of course, the lazy-seq is
cached, so it only has to calculate the values once.

### Some results

* I'm on a lastest-gen MBP 15" with the 2.4 GHz i7 and 8G of RAM.
* Java: 1.6.0 64-bit with HotSpot
* Clojure: 1.3.0 stable
* GCC: 4.2.1, Apple build 
* LLVM-GCC: 4.2.1, LLVM 2335.15.00

Clojure: (I re-def'd #'primes between each run to kill the memoization)

```clojure
(time (print-primes 10000))   ;=> 1079.706ms
(time (print-primes 100000))  ;=> 58769.652
(time (print-primes 1000000)) ;=> C-c I gave up :-/
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
./primes 10000000 -p #=> 2495ms (Note that the internal timing started printing garbage. 
                     #           I used /usr/bin/time for this)
```

GCC with -02:

```bash
./primes 10000 -p    #=> 0ms
./primes 100000 -p   #=> 6ms
./primes 1000000 -p  #=> 67ms
./primes 10000000 -p #=> 1263ms (Note that the internal timing started printing garbage.
                     #           I used /usr/bin/time for this)
```

LLVM (timings were the same with and without optimizations):

```bash
./primes 10000 -p    #=> 0ms
./primes 100000 -p   #=> 12ms
./primes 1000000 -p  #=> 127ms
./primes 10000000 -p #=> 2467ms (Note that the internal timing started printing garbage.
                     #           I used /usr/bin/time for this)
```
