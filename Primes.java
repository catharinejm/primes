package jon;

import java.util.Vector;
import java.util.Iterator;

public class Primes
{
    
    public static void main(String[] args) 
    {
        if (args.length < 2) {
            System.out.println("You need to give a limit and container type, yo!");
            System.exit(1);
        }
        
        long startTime = System.currentTimeMillis();

        boolean doPrint = args.length > 2 && args[2].equals("-p");
        int lim = Integer.parseInt(args[0]);
        
        if (args[1].equals("vector"))
            withVector(lim, doPrint);
        else
            withArray(lim, doPrint);
        
        System.out.printf("Time: %dms\n", System.currentTimeMillis() - startTime);
    }

    private static void withArray(int lim, boolean doPrint) {
        int[] primes = new int[lim/10];
        primes[0] = 2;
        int n = 3, endIdx = 1, p, pIdx = 0;

        while (n <= lim) {
            p = primes[pIdx];
            if (endIdx == primes.length) {
                int[] newArray = new int[primes.length * 2];
                System.arraycopy(primes, 0, newArray, 0, primes.length);
                primes = newArray;
            }
            
            if (p > (n / p)) {
                pIdx = 0;
                primes[endIdx++] = n;
                n++;
            } else if (n % p == 0) {
                pIdx = 0;
                n++;
            } else
                pIdx++;
        }

        if (doPrint) {
            System.out.print("[");
            if (endIdx > 20) {
                for (pIdx = 0; pIdx < 10; pIdx++)
                    System.out.printf("%d ", primes[pIdx]);
                System.out.print("... ");
                for (pIdx = endIdx - 10; pIdx < endIdx - 1; pIdx++)
                    System.out.printf("%d ", primes[pIdx]);
            } else {
                for (pIdx = 0; pIdx < primes.length - 1; pIdx++)
                    System.out.printf("%d ", primes[pIdx]);
            }
            System.out.printf("%d]\n", primes[pIdx]);
            System.out.printf("Total: %d\n", endIdx);
        }
    }

    private static void withVector(int lim, boolean doPrint) {
        Vector<Integer> primes = new Vector<Integer>();
        primes.add(2);
        int n = 3, pIdx = 0, p;
        
        while (n <= lim) {
            p = primes.get(pIdx);
            if (p > (n / p)) {
                pIdx = 0;
                primes.add(n);
                n++;
            } else if ((n % p) == 0) {
                pIdx = 0;
                n++;
            } else
                pIdx++;
        }

        if (doPrint) {
            System.out.print("[");
            if (primes.size() > 20) {
                for (pIdx = 0; pIdx < 10; pIdx++)
                    System.out.printf("%d ", primes.get(pIdx));
                System.out.print("... ");
                for (pIdx = primes.size() - 10; pIdx < primes.size() - 1; pIdx++)
                    System.out.printf("%d ", primes.get(pIdx));
            } else {
                for (pIdx = 0; pIdx < primes.size() - 1; pIdx++)
                    System.out.printf("%d ", primes.get(pIdx));
            }
            System.out.printf("%d]\n", primes.lastElement());
            System.out.printf("Total: %d\n", primes.size());
        }
    }
                                
}
