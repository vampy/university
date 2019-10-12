<?php
require_once "include/User.php";


session_destroy();
User::redirect("login.php");