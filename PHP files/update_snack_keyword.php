<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
{
    $name=$_POST['name'];
    $keyword1=$_POST['keyword1'];
    $keyword2=$_POST['keyword2'];
    $keyword3=$_POST['keyword3'];

    if(empty($keyword1)) {
        $errMSG = "키워드를 입력하세요";
    }

    if(!isset($errMSG)) { // review를 한 후 바뀐 각 키워드의 개수를 비교. 가장 많은 3개를 Keyword_One, Two, Three에 배정
        try {
            $stmt = $con->prepare('UPDATE Snack_Score SET Keyword_One = :keyword1, Keyword_Two = :keyword2, Keyword_Three = :keyword3 WHERE Name = :name');
            $stmt->bindParam(':name', $name);
            $stmt->bindParam(':keyword1', $keyword1);
            $stmt->bindParam(':keyword2', $keyword2);
            $stmt->bindParam(':keyword3', $keyword3);

            if($stmt->execute()) {
                $successMSG = "과자 키워드 정보를 업데이트했습니다.";
            }
            else {
                $errMSG = "과자 키워드 정보 업데이트 에러";
            }
        }
        catch(PDOException $e) {
            die("Database error: " . $e->getMessage());
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

    <form action="<?php $_PHP_SELF ?>" method="POST">
        name: <input type = "text" name = "name" /><br>
        KEY1: <input type = "text" name = "keyword1" /><br>
        KEY2: <input type = "text" name = "keyword2" /><br>
        KEY3: <input type = "text" name = "keyword3" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




