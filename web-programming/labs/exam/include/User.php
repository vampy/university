<?php
require_once "DB.php";

session_start();

class User
{
    public static function addMessage($message)
    {
        if (!isset($_SESSION["messages"])) $_SESSION["messages"] = [];

        $_SESSION["messages"][] = $message;
    }

    public static function getMessagess()
    {
        if (empty($_SESSION["messages"])) return [];

        $messages = $_SESSION["messages"];
        $_SESSION["messages"] = [];
//        unset($_SESSION["messages"]);

        return $messages;
    }

    public static function displayMessages()
    {
        $messages = static::getMessagess();

        $return = "";
        foreach ($messages as $message)
        {
            $return .= sprintf("<p>%s</p>", $message);
        }

        echo $return;
    }


    public static function redirectIfNotLoggedIn()
    {
        if (!static::isLoggedIn())
        {
            static::redirect("login.php");
        }
    }

    public static function redirect($url)
    {
        header('Location: ' . $url);
        exit();
    }

    public static function isLoggedIn()
    {
        return !empty($_SESSION["user"]) && $_SESSION["user"];
    }

    public static function addCompletedTest($test)
    {
        $completed_so_far = $_SESSION["user"]["completed_tests"];

        $completed_so_far[] = $test;

        DB::get()->query(
            "UPDATE users
            SET completed_tests = :tests
            WHERE username = :username",
            DB::NOTHING,
            [
                ':tests'           => implode(",", $completed_so_far),
                ':username'         => $_SESSION["user"]["username"],
            ]
        );

        $_SESSION["user"]["completed_tests"] = $completed_so_far;
    }

    public static function login($username, $password)
    {
        $user = DB::get()->query(
            "SELECT username, completed_tests FROM users WHERE username = :username AND password = :password",
            DB::FETCH_FIRST,
            [':username' => $username, ":password" => $password]
        );

        if ($user)
        {
            if (empty($user["completed_tests"])) $user["completed_tests"] = [];
            else $user["completed_tests"] = static::commaStringToArray($user["completed_tests"]);

            $_SESSION["user"] = $user;
        }

        return $user;
    }

    public static function commaStringToArray($string)
    {
        return array_map("trim", explode(',', $string));
    }
}

