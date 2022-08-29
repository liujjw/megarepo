<?php 

    /**
     * index.php
     *
     * David J. Malan
     * malan@harvard.edu
     *
     * A home page for the course.
     */

?>

<!DOCTYPE html>

<?php require("helpers.php"); ?> 
<?php renderHeader(["title" => "CS50"]); ?>

<ul>
    <li><a href="lectures.php">Lectures</a></li>
    <li><a href="http://cdn.cs50.net/2014/fall/lectures/0/w/syllabus/syllabus.html">Syllabus</a></li>
</ul>

<?php renderFooter(); ?>