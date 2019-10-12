<?php
require_once "include/User.php";
User::redirectIfNotLoggedIn();


$user = $_SESSION["user"];
$total_points = 0;
foreach ($user["completed_tests"] as $game)
{
    $total_points += (int)$game;
}

echo $total_points;
