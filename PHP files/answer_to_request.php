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

    if(empty($user_id)) {
        $errMSG = "아이디를 입력하세요.";
    }

    if(!isset($errMSG)) {  // 기존 User_data에 해당 아이디가 있지 않으면 새로 추가하는 파일

        $stmt = $con->prepare('INSERT INTO recommend_'.$request.' (User, Snack) VALUES (:user ,:snack)');
        $stmt->bindParam(':user', $user_id);
        $stmt->bindParam(':snack', $snack_name);
        $stmt->execute();
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




