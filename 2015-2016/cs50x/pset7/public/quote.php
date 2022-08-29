<?php
    
    require("../includes/config.php");    
    
    if($_SERVER['REQUEST_METHOD'] == 'GET')
    {
        render("../templates/quote_form.php");
    }
    
    else if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        if(empty($_POST["quote"]))
        {
            apologize("Please provide a stock symbol.");
        }
        else 
        {
            $quote = lookup($_POST["quote"]);
            if($quote === false)
            {
                apologize("Stock not found.");
            }
            
            else
            {
                render("../templates/quote_display.php", $quote);
            }
        }
    }
?>
