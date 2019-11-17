<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
{
    $name=$_POST['name'];
    $taste=$_POST['taste'];
    $cost=$_POST['cost'];
    $number_of_rate=$_POST['number_of_rate'];

    if(empty($taste)) {
        $errMSG = "맛 점수를 입력하세요.";
    }

    if(empty($cost)) {
        $errMSG = "가성비 점수를 입력하세요.";
    }

    if(!isset($errMSG)) {
        try {
            $stmt = $con->prepare('UPDATE Snack_Score SET Taste = :taste, Cost = :cost, NumberOfRate = :number_of_rate WHERE Name = :name');
            $stmt->bindParam(':name', $name);
            $stmt->bindParam(':taste', $taste);
            $stmt->bindParam(':cost', $cost);
            $stmt->bindParam(':number_of_rate', $number_of_rate);

            if($stmt->execute()) {
                $successMSG = "과자 정보를 업데이트했습니다.";
            }
            else {
                $errMSG = "과자 정보 업데이트 에러";
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
        Snack Name: <input type = "text" name = "name" /><br>
        Snack Taste: <input type = "text" name = "taste" /><br>
        Snack Cost: <input type = "text" name = "cost" /><br>
        Snack NumberOFRate: <input type = "text" name = "number_of_rate" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




