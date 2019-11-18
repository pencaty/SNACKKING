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
        $errMSG = "맛 점수를 입력하세요.";
    }

    //$get_past_data = $con->prepare('select * from'.$name);
    $stmt = $con->prepare('select * from Snack_Score');  // 과자의 review에서 고른 3가지 keyword의 점수를 1씩 올리는 파일.
    $stmt->execute();
    if ($stmt->rowCount() > 0) {
        $data = array();
        while($row=$stmt->fetch(PDO::FETCH_ASSOC)) {
            if($row['Name'] == $name){
                $sweet = $sweet + $row['Sweet'];
                $spicy = $spicy + $row['Spicy'];
                $sour = $sour + $row['Sour'];
                $bitter = $bitter + $row['Bitter'];
                $update_score = $con->prepare('UPDATE Snack_Score SET Sweet = :sweet, Spicy = :spicy, Sour = :sour, Bitter = :bitter WHERE Name = :name');
                $update_score->bindParam(':name', $name);
                $update_score->bindParam(':sweet', $sweet);
                $update_score->bindParam(':spicy', $spicy);
                $update_score->bindParam(':sour', $sour);
                $update_score->bindParam(':bitter', $bitter);
                $update_score->execute();
                break;
            }
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("snack_json"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
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



