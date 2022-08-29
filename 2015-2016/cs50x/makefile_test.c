#include <test.h>

hellomake: hellomake.c hellofunc.c
    gcc -o hellomake hellomake.c hellofunc.c -I

