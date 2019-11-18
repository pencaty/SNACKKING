
<?php

$host = 'localhost';
$username = 'snack'; # MySQL 계정 아이디
$password = 'cs473cookking!'; # MySQL 계정 패스워드
$dbname = 'snack';  # DATABASE 이름

$options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');

try {  // 데이터베이스 연결을 위한 기본적인 세팅을 하는 페이지
    $con = new PDO("mysql:host={$host};dbname={$dbname};charset=utf8",$username, $password);
} catch(PDOException $e) {
    die("Failed to connect to the database: " . $e->getMessage());
}

$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

if(function_exists('get_magic_quotes_gpc') && get_magic_quotes_gpc()) {
    function undo_magic_quotes_gpc(&$array) {
        foreach($array as &$value) {
            if(is_array($value)) {
                undo_magic_quotes_gpc($value);
            }
            else {
                $value = stripslashes($value);
            }
        }
    }

    undo_magic_quotes_gpc($_POST);
    undo_magic_quotes_gpc($_GET);
    undo_magic_quotes_gpc($_COOKIE);
}

header('Content-Type: text/html; charset=utf-8');
#session_start();
?>
