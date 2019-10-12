<?php
/**
 * User: dan
 * Date: 19/01/2016
 * Time: 11:07 AM
 * File Name: index.php
 */

require_once "include/User.php";
require_once "include/Games.php";
require_once "include/Question.php";


User::redirectIfNotLoggedIn();

$displays_games = isset($_GET["game"]);
$user = $_SESSION["user"];
$message = "";

$next_level = empty($user["completed_tests"]) ? 1 : (int)end($user["completed_tests"]) + 1;
reset($user["completed_tests"]);


if (!$displays_games)
{
    $games = Game::getAll();
}
else
{
    $game = $_GET["game"];

    // submit game form
    if (!empty($_POST))
    {
        $questions = Question::getQuestionsGame($game);
        $success = true;
        foreach ($questions as $question)
        {
            if (!empty($_POST["answer_" . $question["id"]]))
            {
                $answer = $_POST["answer_" . $question["id"]];
                if ($answer == "w")
                {
                    $success = false;
                    User::addMessage(sprintf("Question '%s' is wrong", $question["question"]));
                }
            }
            else
            {
                $success = false;
                User::addMessage(sprintf("Question '%s' has no answer", $question["question"]));
            }
        }

        if ($success && !empty($questions))
        {
            User::addCompletedTest($game);
            User::redirect("index.php");
        }
    }
}

function user_completed_game($user, $number)
{
    foreach ($user["completed_tests"] as $game)
    {
        if ((int)$game == (int)$number) return true;
    }
}

$total_points = 0;
foreach ($user["completed_tests"] as $game)
{
    $total_points += (int)$game;
}


?>

<!DOCTYPE html>
<html>
<head>
    <title>Games</title>
    <meta charset="utf-8">
    <style>
        .total-points {
            position: fixed;
            right: 50px;
        }
    </style>
</head>
<body>
<div class="total-points">
    Total points:
    <span class="total-points-display">
<!--        --><?php //echo $total_points; ?>
    </span>
    <input type="button" class="get-total-points" value="Get Total Points">
</div>

<a href="logout.php">Logout</a>
<?php
User::displayMessages();

if (!$displays_games)
{
    ?>
    <h1>All games</h1>
    <?php
    echo "<ul>";
    foreach ($games as $game)
    {
        if (user_completed_game($user, $game["id"]) || $game["id"] == $next_level)
        {
            echo sprintf(
                "<li><a href=\"?game=%d\">Level %d, Game name: %s</a></li>",
                $game["id"],
                $game["level"],
                $game["testname"]
            );
        }
        else
        {
            echo sprintf("<li>Level %d, Game name: %s</li>", $game["level"], $game["testname"]);
        }

    }
    echo "</ul>";
}
else
{
    $game = $_GET["game"];
    if ($game != $next_level && !user_completed_game($user, $game))
    {
        User::addMessage("You did not unlock that level");
        User::redirect("index.php");
    }

    echo "<br><a href='index.php'>All games completed</a>";
    if (user_completed_game($user, $game))
    {
        echo sprintf("<h1>You already unlocked this level, worth %d points</h1>", $game);
    }
    $questions = Question::getQuestionsGame($game);
    echo sprintf("<h1>Complete Level %d</h1>", $game);
    echo "<form method=\"post\">";
    foreach ($questions as $question)
    {
        echo "<p>";
        echo sprintf("<h3>%s</h3>", $question["question"]);
        echo sprintf(
            "<input type='radio' name='%s' value='c'>%s<br>",
            "answer_" . $question["id"],
            $question["correct_answer"]
        );
        echo sprintf(
            "<input type='radio' name='%s' value='w'>%s",
            "answer_" . $question["id"],
            $question["wrong_answer"]
        );
        echo "</p>";
    }
    echo "<input type='hidden' name='fdsf' value='fd'>";
    echo "<input type='submit' value='Check'>";
    echo "</form>";
//    var_dump($questions);
//    var_dump($_SESSION);
}
?>

<script src="jquery.js"></script>
<script>
    $(document).ready(function() {
        $(".get-total-points").click(function() {
            $.get( "total-points.php", function( data ) {
                console.log(data);
                $(".total-points-display").text(data);
            });
        });
    });
</script>
</body>
</html>