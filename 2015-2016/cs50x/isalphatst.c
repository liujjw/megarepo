#include <ctype.h>
#include <stdio.h>

int main()
{
	char string[] = {'a', '\0'};
	for(int i = 0; string[i] != 0; i++)
	{
		isalpha(string[i]);
	}	
		
}