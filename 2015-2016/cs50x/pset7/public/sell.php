<?php
	
	// require config, eg if user is not logged in with valid id cookie, redirect to login
	require("../includes/config.php");

	// render the form 
	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		// render a form with users stocks to sell
		$stocks_to_sell = query("SELECT * FROM portfolio WHERE id=?", $_SESSION["id"]);
		if($stocks_to_sell == NULL || $stocks_to_sell === false)
		{
			apologize("Error. Could not find user's shares.");
		}

		// send a variable to be rendered called stocks which will contain stock data
		render("../templates/sell_form.php", ["stocks" => $stocks_to_sell]);
	}
	// querying database for transaction
	else if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		// check if user actually selected anything
		if($_POST == NULL)
		{
			apologize("Error: Please select a stock to sell.");
		}

		// query amount of shares from portfolio where id and symbol matches before we delete it, ie so we know how many shares of xyz user had to calc value
		$shares = query("SELECT shares FROM portfolio WHERE id=? AND symbol=?", $_SESSION["id"], $_POST["symbols"]);
		if($shares == NULL || $shares === false)
		{
			apologize("Error. Could not find stocks data.");
		}

		// sell the stocks
		$delete = query("DELETE FROM portfolio WHERE symbol=? AND id=?", $_POST["symbols"], $_SESSION["id"]);
		// if could not delete
		if($delete === false)
		{
			apologize("Error: Could not delete stocks.");
		}
		// check if row was actually deleted
		$check = query("SELECT * FROM portfolio WHERE symbol=? AND id=?", $_POST["symbols"], $_SESSION["id"]);
		// if its null, ie proably actually deleted
		if($check != NULL)
		{
			apologize("Error: SQL query into ostensibly deleted row in database returned not false.");
		}
		
		// refund proceeds of sale to user
		// symbol 
		$symbol = $_POST["symbols"];

		// lookup symbol data into prices
		$prices = lookup($symbol);
		
		// refund amount will be price of current share * amount of shares - a 1% commission for using the service ;)))))))))))
		$commission = 0.99;
		$refund = ($prices["price"] *  $shares[0]["shares"]) * $commission; 
		
		// update users cash
		query("UPDATE users SET cash=cash + ? WHERE id=?", $refund, $_SESSION["id"]);

		// log history
		query("INSERT INTO history (user_id, transaction, stock, shares, time, price) VALUES (?, 'SALE', ?, ?, CURRENT_TIMESTAMP, ?)", $_SESSION["id"], $symbol, $shares, $prices["price"]);

		// redirect to portfolio
		redirect("/");
	}
?>