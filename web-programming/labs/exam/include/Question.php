<?php

class Question
{
    public static function getQuestionsGame($game_id)
    {
        $questions = DB::get()->query(
            "SELECT * FROM questions WHERE test_id = :game_id",
            DB::FETCH_ALL,
            [':game_id' => $game_id],
            [':game_id' => DB::PARAM_INT]

        );

        return $questions;
    }
}