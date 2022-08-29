<?php

    // require PHPMailer
    require("libphp-phpmailer/class.phpmailer.php"); 

    // validate submission
    if (!empty($_POST["name"]) && !empty($_POST["gender"]) && !empty($_POST["dorm"])) 
    {
        // instantiate mailer 
        $mail = new PHPMailer();

        // use SMTP
        $mail->IsSMTP(); 
        $mail->Host = "smtp.fas.harvard.edu"; 
        $mail->Port = 587; 
        $mail->SMTPSecure = "tls"; 

        // set From:
        $mail->SetFrom("jharvard@cs50.harvard.edu"); 

        // set To:
        $mail->AddAddress("jharvard@cs50.harvard.edu"); 

        // set Subject:
        $mail->Subject = "registration";

        // set body
        $mail->Body =
            "This person just registered:\n\n" .
            "Name: " . $_POST["name"] . "\n" .
            "Captain: " . $_POST["captain"] . "\n" .
            "Gender: " . $_POST["gender"] . "\n" .
            "Dorm: " . $_POST["dorm"];

        // send mail
        if ($mail->Send() == false) 
        {
            die($mail->ErrInfo); 
        }
    }
    else
    {
        header("Location: http://localhost/src8m/froshims/froshims-3.php"); 
        exit;
    }
?>

<!DOCTYPE html>

<html>
    <head>
        <title>Frosh IMs</title>
    </head>
    <body>
        You are registered!  (Really.) 
    </body>
</html>