<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
{
    //$name=preg_replace("/\s+/", "", $_POST['name']);;
    $name = $_POST['name'];
    $sweet=(int)($_POST['sweet']);
    $spicy=(int)($_POST['spicy']);
    $sour=(int)($_POST['sour']);
    $bitter=(int)($_POST['bitter']);

    if(empty($sweet) && empty($spicy) && empty($sour) && empty($bitter)) {
        $errMSG = "update_snack 점수를 입력하세요.";
    }

    //$get_past_data = $con->prepare('select * from'.$name);
    $stmt = $con->prepare('select * from Snack_Score');  // 과자의 review에서 고른 3가지 keyword의 점수를 1씩 올리는 파일.
    $stmt->execute();
    if ($stmt->rowCount() > 0) {
        $data = array();
        while($row=$stmt->fetch(PDO::FETCH_ASSOC)) { // $row[] : 기존 각 keyword 별 점수
            if($row['Name'] == $name){
                $sweet = $sweet + $row['Sweet'];
                $spicy = $spicy + $row['Spicy'];
                $sour = $sour + $row['Sour'];
                $bitter = $bitter + $row['Bitter'];

                $score_array[0] = $sweet;
                $score_array[1] = $spicy;
                $score_array[2] = $sour;
                $score_array[3] = $bitter;
                $first_index = -1;
                $second_index = -1;
                $third_index = -1;

                $max = 0;
                for($i=0; $i<4; $i++) { // 가장 큰 값 찾기
                    if($score_array[$i] > $max) {
                        $first_index = $i;
                        $max = $score_array[$i];
                    }           
                }

                $max = 0;
                for($i=0; $i<4; $i++) { // 가장 큰 값 찾기
                    if($score_array[$i] > $max && $i != $first_index) {
                        $second_index = $i;
                        $max = $score_array[$i];
                    }
                }

                $max = 0;
                for($i=0; $i<4; $i++) { // 가장 큰 값 찾기
                    if($score_array[$i] > $max && $i != $first_index && $i != $second_index) {
                        $third_index = $i;
                        $max = $score_array[$i];
                    }
                }

                switch($first_index) {
                    case 0 : $keyword_one = 'Sweet';
                    break;
                    case 1 : $keyword_one = 'Spicy';
                    break;
                    case 2 : $keyword_one = 'Sour';
                    break;
                    case 3 : $keyword_one = 'Bitter';
                    break;
                    default : $keyword_one = 'null';
                    break;
                }

                switch($second_index) {
                    case 0 : $keyword_two = 'Sweet';
                        break;
                    case 1 : $keyword_two = 'Spicy';
                        break;
                    case 2 : $keyword_two = 'Sour';
                        break;
                    case 3 : $keyword_two = 'Bitter';
                        break;
                    default : $keyword_two = 'null';
                        break;
                }

                switch($third_index) {
                    case 0 : $keyword_three = 'Sweet';
                        break;
                    case 1 : $keyword_three = 'Spicy';
                        break;
                    case 2 : $keyword_three = 'Sour';
                        break;
                    case 3 : $keyword_three = 'Bitter';
                        break;
                    default : $keyword_three = 'null';
                        break;
                }

                //여기서 키워드 별 점수를 비교해서 가장 높은 3개를 Keyword_One, Two, Three에 배정한 후 한번에 UPDATE하자.

/*                $update_score = $con->prepare('UPDATE Snack_Score SET Sweet = :sweet, Spicy = :spicy, Sour = :sour, Bitter = :bitter WHERE Name = :name');
                $update_score->bindParam(':name', $name);
                $update_score->bindParam(':sweet', $sweet);
                $update_score->bindParam(':spicy', $spicy);
                $update_score->bindParam(':sour', $sour);
                $update_score->bindParam(':bitter', $bitter);
                $update_score->execute();
                break;*/

                $update_score = $con->prepare('UPDATE Snack_Score SET Keyword_One = :keyword1, Keyword_Two = :keyword2, Keyword_Three = :keyword3, Sweet = :sweet, Spicy = :spicy, Sour = :sour, Bitter = :bitter WHERE Name = :name');
                $update_score->bindParam(':name', $name);
                $update_score->bindParam(':keyword1', $keyword_one);
                $update_score->bindParam(':keyword2', $keyword_two);
                $update_score->bindParam(':keyword3', $keyword_three);
                $update_score->bindParam(':sweet', $sweet);
                $update_score->bindParam(':spicy', $spicy);
                $update_score->bindParam(':sour', $sour);
                $update_score->bindParam(':bitter', $bitter);
                $update_score->execute();
                break;
            }
        }

        /*header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("snack_json"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;*/
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
        Name: <input type = "text" name = "name" /><br>
        Sweet: <input type = "text" name = "sweet" /><br>
        Spicy: <input type = "text" name = "spicy" /><br>
        Sour: <input type = "text" name = "sour" /><br>
        Bitter: <input type = "text" name = "bitter" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




