<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
{
    $snack_name=$_POST['snack_name'];

    if(empty($snack_name)) {
        $errMSG = "get_snack_keyword 과자 이름을 입력하세요.";
    }

    $stmt = $con->prepare('select * from Snack_Score');
    $stmt->execute();

    if ($stmt->rowCount() > 0) {

        $data = array();
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            if($row['Name'] == $snack_name) {

                //extract($row);

                if($row['Keyword_Two'] == null  || $row['Keyword_Two'] == 'null') $second_score = null;
                else $second_score = $row[$row['Keyword_Two']];
                if($row['Keyword_Three'] == null || $row['Keyword_Three'] == 'null') {
                    $third_score = null;
                }
                else $third_score = $row[$row['Keyword_Three']];

                array_push($data,
                    array('taste' => $row['Taste'], 'cost' => $row['Cost'], 'keyword1' => $row['Keyword_One'], 'keyword2' => $row['Keyword_Two'], 'keyword3' => $row['Keyword_Three'], 'keyword1_score' => $row[$row['Keyword_One']], 'keyword2_score' => $second_score, 'keyword3_score' => $third_score));

                header('Content-Type: application/json; charset=utf8');
                $json = json_encode(array("snack_json"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                echo $json;

                break;
            }
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

    </body>
    </html>

    <?php
}
?>




