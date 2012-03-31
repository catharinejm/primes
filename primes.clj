(ns om.primes)

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

(defn print-primes [n]
  (let [ps (primes<=n n)
        len (count ps)]
    (print "[")
    (if (> len 20)
      (do
        (doseq [p (subvec ps 0 10)]
          (print (str p " ")))
        (print "... ")
        (doseq [p (subvec ps (- len 10) (dec len))]
          (print (str p " "))))
      (doseq [p (subvec ps 0 (dec len))]
        (print (str p " "))))
    (println (str (nth ps (dec len)) "]"))))