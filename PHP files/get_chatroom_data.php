
<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$stmt = $con->prepare('select * from Chatroom'); // DB에서 Chatroom table의 정보를 가져와 json 파일로 변환하는 파일
$stmt->execute();

if ($stmt->rowCount() > 0) {
    $data = array();
    while($row=$stmt->fetch(PDO::FETCH_ASSOC))
    {
        extract($row);

        array_push($data,
            array('request_id'=>$user_id, 'key1'=>$Keyword_One, 'key2'=>$Keyword_Two, 'key3'=>$Keyword_Three, 'comment'=>$Comment));
    }

    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array("snack_json"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}

?>