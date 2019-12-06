<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )  // Database (User_data)에 새로운 유저를 추가하는 파일
{
    $request=$_POST['request'];
    $user_id=$_POST['user_id'];
    $snack_name=$_POST['snack'];

    $keyword1=$_POST['keyword_1'];
    $keyword2=$_POST['keyword_2'];
    $keyword3=$_POST['keyword_3'];
    $comment=$_POST['comment'];

    if(empty($user_id)) {
        $errMSG = "아이디를 입력하세요.";
    }

    if(!isset($errMSG)) {  // 기존 User_data에 해당 아이디가 있지 않으면 새로 추가하는 파일

        $stmt = $con->prepare('INSERT INTO recommend_'.$request.' (User, Snack) VALUES (:user ,:snack)');
        $stmt->bindParam(':user', $user_id);
        $stmt->bindParam(':snack', $snack_name);
        $stmt->execute();

        $request_store_table = $con->prepare('INSERT INTO Request_History(Requester, Respondent, Snack, Keyword_One, Keyword_Two, Keyword_Three, Comment) VALUES(:request, :user_id, :snack, :key1, :key2, :key3, :comment)');
        $request_store_table->bindParam(':request', $request);
        $request_store_table->bindParam(':user_id', $user_id);
        $request_store_table->bindParam(':snack', $snack_name);
        $request_store_table->bindParam(':key1', $keyword1);
        $request_store_table->bindParam(':key2', $keyword2);
        $request_store_table->bindParam(':key3', $keyword3);
        $request_store_table->bindParam(':comment', $comment);

        if($request_store_table->execute()) {
            $successMSG = "Request 정보 저장 완료";
        }
        else {
            $errMSG = "Request 정보 저장 에러";
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
        REQUEST: <input type = "text" name = "request" />
        ID: <input type = "text" name = "user_id" />
        SNACK NAME: <input type = "text" name = "snack" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




