<?php

    require(__DIR__ . "/../includes/config.php");

    // numerically indexed array of places
    $places = [];

    // TODO: search database for places matching $_GET["geo"]
    
    // sanitize query.....
    htmlspecialchars($_GET['geo']);
    
    // query with wildcard if user knows beginnings of city, state, postal code
    $places = query("SELECT * FROM places WHERE postal_code LIKE '{$_GET['geo']}%' 
    	OR admin_name1 LIKE '{$_GET['geo']}%' OR admin_code1 LIKE '{$_GET['geo']}%'
    	OR admin_name2 LIKE '{$_GET['geo']}%'");
    
    // output places as JSON (pretty-printed for debugging convenience)
    header("Content-type: application/json");
    print(json_encode($places, JSON_PRETTY_PRINT));

?>
