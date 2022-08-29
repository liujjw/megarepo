<?php

    $size = 0;
    $table = [];

    function check($word)
    {
    	global $table;
    	return isset(table[strlower($word)]);
    }

    function load($dictionary)
    {
    	global $size;
    	global $table;

    	foreach(file($dictionary) as $word)
    		$table[chop($word)] = true;
    	$size++;

    	return true;
    }

    function size()
    {
    	global $size;
    	return $size;
    }

    function unload()
    {

    }

?>