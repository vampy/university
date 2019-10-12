<?php
// Write a web application for managing vacation destinations. A destination has in the database besides the name of the
// location (i.e. city etc.), the country name, description, tourist targets in that location an an estimated cost per day.
// The user can add, delete or modify the destinations and he can also browse the vacation destinations grouped by countries
// (use AJAX for this).
require_once 'config.php';
require_once 'include/Destination.php';
require_once 'include/Template.php';
?>
<!DOCTYPE html>
<html>
<head>
    <title>Lab 7</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="main.css">
</head>
<body>
<header>
    <h1>Destinations</h1>
</header>
<main>
    <section class="section-add">
        <?php
        echo '<h2>Add destination</h2>';
        echo '<form method="post">';
        echo '<p class="status"><p>';
        Template::echo_input('Name', 'name', '');
        echo "<p><label>Description: <textarea name='description' required></textarea></label> </p>";
        Template::echo_input('Targets', 'targets', '');
        Template::echo_input('Country', 'country', '');
        Template::echo_input('Cost/day', 'cost-per-day', '');
        echo '<button class="destination-add">Add</button>';
        echo '</form><hr><hr>';
        ?>
    </section>
    <div class="content">
        <?php
        require_once 'destinations-all.php';;
        ?>
    </div>
</main>

<div class="menu">
    <button class="menu-all">All</button>
    <br>
    <button class="menu-country">By Country</button>
</div>
<script src="main.js"></script>
</body>
</html>
