
<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$stmt = $con->prepare('select * from Snack_Score order by Taste desc'); // DB에서 Snack_Score tablel의 정보를 가져와 json 파일로 변환하는 파일
$stmt->execute();

if ($stmt->rowCount() > 0) {
    $data = array();
    while($row=$stmt->fetch(PDO::FETCH_ASSOC))
    {
        extract($row);

        array_push($data,
            array('name'=>$Name, 'taste'=>$Taste, 'cost'=>$Cost, 'number_of_rate'=>$NumberOfRate, 'keyword1'=>$Keyword_One, 'keyword2'=>$Keyword_Two, 'keyword3'=>$Keyword_Three
            ));
    }

    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array("snack_json"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;

    // json 파일을 echo하는 파일에서는 테스팅하려고 다른 것을 echo 하면 json에 섞여서 에러가 뜬다
}

?>