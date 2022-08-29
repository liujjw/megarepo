<?php

    // configuration
    require("../includes/config.php"); 

    /* render portfolio*/

    // query cash pile from users into cash
    $cash = query("SELECT cash FROM users WHERE id=?", $_SESSION["id"]);
    if($cash === false)
    {
        apologize("Error: Cash balance could not be found.");
    }

    // query username
    $username = query("SELECT username FROM users WHERE id=?", $_SESSION["id"]);
    if($username === false)
    {
        apologize("Error: User for cash balance could not be found.");
    }
    
    // get users portfolio data(shares, symbols, corresponding id) from session id in however many arrays(different shares of stocks)
    $portfolio = query("SELECT * FROM portfolio WHERE id=?", $_SESSION["id"]);

    // users with data
    if($portfolio != NULL)
    {

        // for each array in portfolio called holdings
        foreach($portfolio as $holdings)
        {
        	// lookup the symbol of holding to check current price into lookup
        	$lookup = lookup($holdings["symbol"]);
        	// if not false, errors taken care of before adding, or stock changed
        	if($lookup !== false)
        	{
        		// an array data for all data current price, shares, symbol, name
        		$data[] = [
        		"prices" => $lookup["price"],
        		"name" => $lookup["name"],
        		"shares" => $holdings["shares"],
        		"symbol" => $lookup["symbol"],
                "cost" => $lookup["price"] * $holdings["shares"]
        		];
        	}
        }
        // render portfolio with user's data
        render("portfolio.php", ["title" => "Portfolio", "portfolio" => $data, "money" => $cash[0]["cash"], "username" => $username[0]["username"]]);
    }
    // new user with no portfolio data
    else
    {
        // render portfolio with user's data
        render("portfolio.php", ["title" => "Portfolio", "money" => $cash[0]["cash"], "username" => $username[0]["username"]]);
    }
?>
