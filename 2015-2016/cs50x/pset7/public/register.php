<?php

    // configuration
    require("../includes/config.php");

    // if user reached page via GET (as by clicking a link or via redirect)
    if ($_SERVER["REQUEST_METHOD"] == "GET")
    {   
        // changing password, ie user already has an assigned id, now we update password hash
        if (isset($_SESSION["id"]))
        {
            // render a form for resetting password, which will require confirmation, delete old password hash and update replace with new
            // what variables needed?, well session id is what we have right now, will not need mentioning since alreaedy superglobal
            render("../templates/password_change.php");
        }
        
        else
        {
            // else just render a form for supposedly new user
            render("register_form.php", ["title" => "Register"]);
        }    
    }


    // else if user reached page via POST (as by submitting a form via POST)
    else if ($_SERVER["REQUEST_METHOD"] == "POST")
    {
        // password change
        if(isset($_SESSION["id"]))
        {
            /*error checking*/

            // empty fields
            if($_POST["new_password"] == NULL || $_POST["old_password"] == NULL || $_POST["confirm"] == NULL)
            {
                apologize("Error: Empty fields");
            }

            // wrong password, failed authentication
            $password = query("SELECT hash FROM users WHERE id=?", $_SESSION["id"]);
            if(crypt($_POST["old_password"], $password[0]["hash"]) != $password[0]["hash"])
            {
                apologize("Error: Password authetication failed.");
            }

            // passwords do not match
            if($_POST["new_password"] != $_POST["confirm"])
            {
                apologize("Error: Passwords do not match.");
            }

            // same password
            if($_POST["old_password"] == $_POST["new_password"])
            {
                apologize("Warning: Same password!");
            }

            // update password if all is good
            query("UPDATE users SET hash=? WHERE id=?", crypt($_POST["new_password"]), $_SESSION["id"]);

            render("../templates/password_change_success.php");
        }
        
        else
        {    
            // validate submission
            if (empty($_POST["username"]))
            {
                apologize("You must provide your username.");
            }
            // password
            else if (empty($_POST["password"]))
            {
                apologize("You must provide your password.");
            }
            //confirmation
            else if (empty($_POST["confirmation"]))
            {
                apologize("You must confirm your password.");
            }
            else if ($_POST["confirmation"] !== $_POST["password"])
            {
                apologize("Passwords do not match.");
            }
            
            // add to database
            $insert = query("INSERT INTO users (username, hash, cash) VALUES(?, ?, 10000.00)", $_POST["username"], crypt($_POST["password"]));
            
            // if fail, error
            if($insert === false)
            {
                apologize("Error. Username already taken.");
            }
            
            // log user in btw
            $rows = query("SELECT LAST_INSERT_ID() AS id");
            $id = $rows[0]["id"];
            
            $_SESSION["id"] = $id;
            
            redirect("/");
        }   
            
    }

?>
