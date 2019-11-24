<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )  // Database (User_data)에 새로운 유저를 추가하는 파일
{
    $user_id=$_POST['user_id'];
    $keyword1=$_POST['keyword_1'];
    $keyword2=$_POST['keyword_2'];
    $keyword3=$_POST['keyword_3'];
    $comment=$_POST['comment'];

    if(empty($user_id)) {
        $errMSG = "아이디를 입력하세요.";
    }

    if(!isset($errMSG)) {  // 기존 User_data에 해당 아이디가 있지 않으면 새로 추가하는 파일

        try {
            $new_chatroom_data = $con->prepare('INSERT INTO Chatroom(user_id, Keyword_One, Keyword_Two, Keyword_Three, Comment) VALUES(:user_id, :key1, :key2, :key3, :comment)');
            $new_chatroom_data->bindParam(':user_id', $user_id);
            $new_chatroom_data->bindParam(':key1', $keyword1);
            $new_chatroom_data->bindParam(':key2', $keyword1);
            $new_chatroom_data->bindParam(':key3', $keyword2);
            $new_chatroom_data->bindParam(':comment', $comment);

            if($new_chatroom_data->execute()) {
                $successMSG = "새로운 유저를 추가했습니다.";
            }
            else {
                $errMSG = "유저 추가 에러";
            }

            $create_individual_recommend_table = $con->prepare('CREATE TABLE recommend_'.$user_id.' (
                id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                Name VARCHAR(45),
                Taste INT(10) UNSIGNED DEFAULT 1,
                Cost INT(10) UNSIGNED DEFAULT 1,
                Keyword_One VARCHAR(45), 
                Keyword_Two VARCHAR(45),
                Keyword_Three VARCHAR(45)
                )');

            if($create_individual_recommend_table->execute()) {
                $successMSG = $successMSG + "각 유저 추천 테이블 생성했습니다";
            }
            else {
                $errMSG = $errMSG + "각 유저 추천 테이블 생성 실패";
            }

        }
        catch(PDOException $e) {
            die("Database error: " . $e->getMessage());
        }
    }
}
?>

<?php
if (isset($errMSG)) echo $errMSG;
if (isset($successMSG)) echo $successMSG;

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( !$android )
{
    ?>
    <html>
    <body>

    <form action="<?php $_PHP_SELF ?>" method="POST">
        ID: <input type = "text" name = "user_id" />
        key1: <input type = "text" name = "keyword_1" />
        key2: <input type = "text" name = "keyword_2" />
        key3: <input type = "text" name = "keyword_3" />
        comment: <input type = "text" name = "comment" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




