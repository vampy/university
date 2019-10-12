<?php
require_once "include/User.php";
if (User::isLoggedIn()) User::redirect("index.php");

// submit form
if (!empty($_POST))
{
    $user = User::login($_POST["username"], $_POST["password"]);
    if ($user)
    {
        User::redirect("index.php");
    }
    else
    {
        User::addMessage("Username or password is wrong.");
    }
}

?>
<!DOCTYPE html>
<html>
<head>
    <title>1</title>
    <meta charset="utf-8">
</head>
<body>
<form method="POST">
    <?php User::displayMessages(); ?>
    <p><label>Username: <input type="text" placeholder="username"  name="username"></label></p>
    <p><label>Password: <input type="password"   name="password"></label></p>
    <p><input type="submit" value="Login"></p>
</form>
</body>
</html>
