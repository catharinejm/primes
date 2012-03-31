#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/time.h>

int main(int argc, char *argv[]) {
  if (argc < 2) {
    fprintf(stderr, "You need to give a limit, yo!\n");
    return 1;
  }

  int do_print = (argc == 3 && !strcmp(argv[2], "-p"));
  
  struct timeval the_time;
  gettimeofday(&the_time, NULL);
  suseconds_t start_time = the_time.tv_usec;

  int lim = atoi(argv[1]);
  int alloc_bytes = lim/10 * sizeof(int), 
    alloc_ints = lim/10;
  
  int *primes = malloc(alloc_bytes);
  int n = 3;
  *primes = 2;
  int end_idx = 1;

  int p_idx = 0, p;
  while (n <= lim) {
    if (end_idx == alloc_ints) {
      alloc_bytes += alloc_bytes;
      alloc_ints += alloc_ints;
      
      primes = realloc(primes, alloc_bytes);
      if (primes == NULL) {
        fprintf(stderr, "ERROR: Reallocation failed!\n");
        return 1;
      }
    }

    p = primes[p_idx];
    if (p > (n / p)) { 
      p_idx = 0;
      primes[end_idx++] = n;
      n++;
    } else if (! (n % p)) { 
      p_idx = 0;
      n++;
    } else
      p_idx++;
  }

  if (do_print) {
    printf("[");
    if (end_idx > 20) {
      for (p_idx = 0; p_idx < 10; ++p_idx)
        printf("%i ", primes[p_idx]);
      printf("... ");
      for (p_idx = end_idx - 10; p_idx < end_idx - 1; p_idx++)
        printf("%i ", primes[p_idx]);
    } else {
      for (p_idx = 0; p_idx < end_idx - 1; p_idx++)
        printf("%i ", primes[p_idx]);
    }
    printf("%i]\n", primes[p_idx]);
    printf("Total: %i\n", end_idx);
  }

  gettimeofday(&the_time, NULL);
  suseconds_t end_time = the_time.tv_usec;
  printf("Time: %ims\n", (end_time - start_time) / 1000);

  return 0;
}
