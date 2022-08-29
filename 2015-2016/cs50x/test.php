<html>
 <head>
  <title>PHP Test</title>
 </head>
 <body>
 <?php 
 	if(strpos($_SERVER['HTTP_USER_AGENT'], "MSIE") === false){
 ?> 
 	<h1>You dont use IE</h1>
 <?php
 	}else{
 ?>
 	<h1>YOU DO USE IE</h1>
 <?php
	}
 ?>
 </body>
</html>