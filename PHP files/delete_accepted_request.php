<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )  // Database (User_data)에 새로운 유저를 추가하는 파일
{
    $user=$_POST['user'];

    if(empty($user)) {
        $errMSG = "아이디를 입력하세요.";
    }

    if(!isset($errMSG)) {  // 기존 User_data에 해당 아이디가 있지 않으면 새로 추가하는 파일

        $delete_request = $con->prepare('DELETE FROM Chatroom WHERE user_id = :user');
        $delete_request->bindParam(':user', $user);
        $delete_request->execute();

        $drop_table = $con->prepare('DROP TABLE recommend_'.$user);
        $drop_table->execute();

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
        ID: <input type = "text" name = "user" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




