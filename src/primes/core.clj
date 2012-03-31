(ns primes.core)

(def primes
  (iterate (fn [[n primes]]
             (let [n (inc n)]
               (loop [test-primes (filter (partial >= (/ n 2)) primes)]
                 (let [p (first test-primes)]
                   (cond
                     (empty? test-primes)
                     [n (conj primes n)]
                     (zero? (rem n p))
                     [n primes]
                     :else
                     (recur (filter (partial >= (/ n p)) (rest test-primes))))))))
           [2 [2]]))

(defn primes<=n [n]
  (if (< n 2)
    []
    (second (nth primes (- n 2)))))

(defn print-large-vec [lvec]
  (let [len (count lvec)]
    (print "[")
    (if (> len 20)
      (do
        (doseq [p (subvec lvec 0 10)]
          (print (str p " ")))
        (print "... ")
        (doseq [p (subvec lvec (- len 10) (dec len))]
          (print (str p " "))))
      (doseq [p (subvec lvec 0 (dec len))]
        (print (str p " "))))
    (println (str (nth lvec (dec len)) "]"))))

(defn print-primes [n]
  (print-large-vec (primes<=n n)))

(defn iterative-primes [lim]
  (if (> lim 1)
    (loop [primes [2]
           rem-primes primes
           n 3]
      (let [p (first rem-primes)]
        (cond
          (> n lim)
          primes
          (> p (/ n p))
          (recur (conj primes n) (conj primes n) (inc n))
          (zero? (rem n p))
          (recur primes primes (inc n))
          :else
          (recur primes (rest rem-primes) n))))
    []))

(defn java-primes [lim]
  (loop [primes (int-array (/ lim 10) [2])
         p-idx 0
         end-idx 1
         n 3]
    (if (= end-idx (alength primes))
      (let [new-ary (int-array (* (alength primes) 2) primes)]
        (recur new-ary p-idx end-idx n))
      (let [p (aget primes p-idx)]
        (cond
          (> n lim)
          primes
          (> p (/ n p))
          (do
            (aset-int primes end-idx n)
            (recur primes 0 (inc end-idx) (inc n)))
          (zero? (rem n p))
          (recur primes 0 end-idx (inc n))
          :else
          (recur primes (inc p-idx) end-idx n))))))
