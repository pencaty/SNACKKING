<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
{
    $name=$_POST['name'];

    if(empty($name)) {
        $errMSG = "이름을 입력하세요.";
    }

    if(!isset($errMSG)) {
        try {
            $stmt = $con->prepare('INSERT INTO Snack_List(name) VALUES(:name)');
            $stmt->bindParam(':name', $name);

            if($stmt->execute()) {
                $successMSG = "새로운 과자를 추가했습니다.";
            }
            else {
                $errMSG = "과자 추가 에러";
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
        Snack Name: <input type = "text" name = "name" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




