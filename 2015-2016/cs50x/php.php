!# /usr/bin/env php
<?php
	
	$k = readline("hey gimmer number please:");

	if($k > 0)
		printf("positive\n");
	else if($k == 0)
		printf("zero\n");
	else
		printf("negative\n");

?>