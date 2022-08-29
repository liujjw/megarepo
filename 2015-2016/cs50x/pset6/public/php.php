<?php

session_start();

if(isset($_SESSION["counter"]))
	$counter = $_SESSION["counter"];
else
	$counter = 0;

$_SESSION["counter"] = $counter + 1; 

$array = [mark => dogs, alisa => cats, gyroato => fish];
echo "{$array['mark']}\n";

foreach($array as $value){
	echo "value: $value\n";
}

// checking with == after type juggling, say char 1 and int 1
// checking with === if same type and value

// foreach($array as $call values in this array some value){
// 		iterate over it	
//}

// arrays more powerful in php, associative arrays, can store things of different types, 
// can be associative, can call each index a key array[somekey] instead of just standard array[1], array[0]
// use => notation to assign
// $array = array("key", "etc", 89);
// $array = ["hey", 10];
// htmlespeciachars to escape characters
// html is more of a markup language to style how something is veiwed, not strictly a programming language which has logic ifs and loops
// php is a server side scripting language for dynamic webpages for different users
// MVC, templates, public, includes
// loesely typed php
// php can assume youre using ints when adding like a char for example
// sql databases
// UPDATE datatbase SET year=1978 WHERE name=Emma
// INSERT INTO data VALUES(3,18,BEN)
// DELETE FROM database
?>

<!DOCTYPE html>

<html>
<head>
	<title>counter</title>
</head>
<body>
	<h1>you visischachi <?= $counter ?> times</h1>
</body>
</html>