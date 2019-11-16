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

// MySQL - DB 접속.

$conn = mysqli_connect($db_host,$db_user,$db_passwd,$db_name);

if (mysqli_connect_errno()){
    echo "MySQL 연결 오류: " . mysqli_connect_error();
    exit;
} else {
    echo "DB : \"$db_name\"에 접속 성공.<br/>";
}

// table 만들기
/*
$sql = "CREATE TABLE Snack_List 
(
idSnack INT(10) unsigned not null auto_increment,
Name VARCHAR(255) not null,
PRIMARY KEY(idSnack)
) charset=utf8";

if (mysqli_query($conn,$sql)){
    echo "성공적으로 테이블을 만들었습니다.<br/>";
} else {
    echo "테이블 생성 오류 : " . mysqli_error($conn);
    exit;
}
*/

// 문자셋 설정, utf8.

mysqli_set_charset($conn,"utf8");



// 테이블에 값 쓰기.

//$sql = "INSERT INTO Snack_List (Name) VALUES ('칸쵸'), ('시리얼')";
//$result = mysqli_query($conn, $sql);


$sql = "select * from Snack_List";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    echo "<table border='1'>";
    while ($row = $result->fetch_assoc()) {
        echo "<tr><td>$row[idSnack]</td><td>$row[Name]</td></tr>";
    }
    echo "</table>";
}
else {
    echo "No Snack in Database";
}

/*
if ($result){
    echo "테이블에 값 쓰기 완료: $result<br/>";
} else {
    echo "테이블에 값 쓰기 오류: " . mysqli_error($conn);
}
*/


mysqli_close($conn);

?>

</html>
