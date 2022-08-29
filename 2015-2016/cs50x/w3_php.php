<!DOCTYPE html>
<html lang="en-US">
<head>
	<title>practce site</title>
	<style>
	h1{
		background-color: blue;
		font-family: verdana;
		font-size: 40px;
	}
	body{
		background-color: lightgrey;
	}
	</style>
</head>
<body>

<?php
	$mysql = "bad";
	$ursql = "good";
	function yeaaaa(){
		global $mysql,$ursql;
		$GLOBALS['mysql'] = $GLOBALS['ursql'];
	}
	$concatenate = "concatenate is a weird word";
	yeaaaa();
	echo "Hello,world".$GLOBALS['mysql']. "!<br>";

	function staticc(){
		static $k = 50;
		echo $k;
		$k++;
	}
	echo "<br><br><br><br><br><br>";
	echo "multiple", "parameters";
	staticc();
	staticc();
	staticc();

	$cars = array("toyota", "benz", "aston", 'austinpowerscarmobile');
	//echo $cars;
	var_dump($cars);
	/*
	if (){

	}else if{

	}else{

	}
	$favcolor = "red";
	switch ($favcolor){
		case label1:
		//
		break;
		case label2:
		//
		break;
		default:
		//
		break;
	}
	*/
	echo $_SERVER['PHP_SELF'], "<br>";

?>
<form action="https://facebook.com/public?query" method"GET">
	<input name="q" type"text"/>
	<br/>
	<input type="submit" value="facebook">
</form>
<h1>hey guys this is eaustin</h1>
</body>
</html>>
