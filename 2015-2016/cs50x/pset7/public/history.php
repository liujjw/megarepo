<?php
	
	// logging history: use a database
	// when clicked, we query db for all fields with user id in it
		// create a databse, with fields for transactions,for each transaction, insert into db
			// have a field for type of transaction, user, what shares were involved, how much, etc per spec
	
	// configs
	require("../includes/config.php");

	// query history db 
	$user_history = query("SELECT * FROM history WHERE user_id=?", $_SESSION["id"]);
	
	if($user_history != NULL)
	{	
		// render users data if there is history
		render("history_table.php", ["data" => $user_history]);
	}
	else
	{
		render("history_table.php");
	}
?>