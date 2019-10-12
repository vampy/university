<?php
require_once "User.php";

class Game
{
    public static function getAll()
    {
        $games = DB::get()->query(
            "SELECT * FROM tests",
            DB::FETCH_ALL
        );

        return $games;
    }
}