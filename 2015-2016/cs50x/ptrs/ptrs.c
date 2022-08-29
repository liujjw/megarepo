#include <stdio.h>

int main(void)
{
	char array[40] = "123456789";
	int i = 3;
	printf("something to talk \n%i\n", (int)array[i]);
	printf("\nohh %i\n", (int)i[array]);
	return 0;
}



*(array + i)
*(i + array)
