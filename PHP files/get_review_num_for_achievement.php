<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )  // Database (User_data)에 새로운 유저를 추가하는 파일
{
    $user_id=$_POST['user_id'];

    if(empty($user_id)) {
        $errMSG = "아이디를 입력하세요.";
    }

    $stmt = $con->prepare('select * from '.$user_id); // DB에서 특정 유저 table에 접속하여 그 유저가 리뷰를 쓴 과자와 각 점수들을 불러오는 파일
    $stmt->execute();

    $row_num = $stmt->rowCount();

    $data = array();
    array_push($data, array('number' => $row_num));

    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array("snack_json"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}
?>

<?php
if (isset($errMSG)) echo $errMSG;
if (isset($successMSG)) echo $successMSG;

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( !$android )
{
    ?>
    <?php
}
?>




