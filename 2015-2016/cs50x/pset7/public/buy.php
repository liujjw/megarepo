<?php

	require("../includes/config.php");

	// parser error unexpected if, you just forgot a semicolon
	// is there a way to check of request method was just href?
	// blue looks like parser known functions
	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		render("../templates/buy_form.php");
	}

	else if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		// renaming and localizing post variables
		$symbol = $_POST["symbol"];
		$amount = $_POST["amount"];

		// did user forget a field
		if($symbol == NULL || $amount == NULL)
		{
			apologize("Error: Empty field(s).");
		}

		// check if valid amount of shares, if number is non-negative
		if(!preg_match("/^\d+$/", $amount))
		{
			apologize("Error: Amount of shares requested is not defined.");
		}
		// if amount is greater than an int, integer overflow
		if($amount > PHP_INT_MAX)
		{
			apologize("Error: Integer overflow.");
		}
		// lookup share name
		$lookup = lookup($symbol);
		// check if real
		if($lookup === false)
		{
			apologize("Error: Stock not found.");
		}

		// does user have enough dough to buy shares?
		$cost = $lookup["price"] * $amount;
		$cash = query("SELECT cash FROM users WHERE id=?", $_SESSION["id"]);
		if($cash < $cost)
		{
			apologize("Error: You do not have enough dough!");
		}

		// else they do, charge them money
		query("UPDATE users SET cash=cash - ? WHERE id=?", $cost, $_SESSION["id"]);

		// insert requested amount of shares into users portfolio
		// toupper the symbol for storage logistics
		strtoupper($symbol);
		query("INSERT INTO portfolio (id, symbol, shares) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE shares = shares + VALUES(shares)", $_SESSION["id"], $symbol, $amount);

		// log change into history
		// A_I autoincrement
		query("INSERT INTO history (user_id, transaction, stock, shares, time, price) VALUES (?, 'PURCHASE', ?, ?, CURRENT_TIMESTAMP, ?)", $_SESSION["id"], $symbol, $amount, $lookup["price"]);

		// redirect them home
		redirect("/");
	}


?>