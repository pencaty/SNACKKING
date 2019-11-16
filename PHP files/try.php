<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    </head>
<?php

$db_host = "localhost";
$db_user = "snack";
$db_passwd = "cs473cookking!";
$db_name = "snack";

$conn = mysqli_connect($db_host,$db_user,$db_passwd,$db_name);

if (mysqli_connect_errno($conn)) {
    echo "데이터베이스 연결 실패: " . mysqli_connect_error();
} else {
    echo "데이터베이스 연결 성공";
}

?>

</html>
