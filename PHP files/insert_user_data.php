<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )  // Database (User_data)에 새로운 유저를 추가하는 파일
{
    $user=$_POST['user'];

    if(empty($user)) {
        $errMSG = "아이디를 입력하세요.";
    }

    if(!isset($errMSG)) {  // 기존 User_data에 해당 아이디가 있지 않으면 새로 추가하는 파일

        $user_data = $con->prepare('select * from User_data');
        $user_data->execute();

        $exist = 0;

        if ($user_data->rowCount() > 0) {
            while($row=$user_data->fetch(PDO::FETCH_ASSOC)) {
                if($user == $row['Name']){
                    $exist = 1;
                    break;
                }
            }
        }

        if ($exist == 0) {
            try {
                $new_user_data = $con->prepare('INSERT INTO User_data(Name) VALUES(:user)');
                $new_user_data->bindParam(':user', $user);

                if($new_user_data->execute()) {
                    $successMSG = "새로운 유저를 추가했습니다.";
                }
                else {
                    $errMSG = "유저 추가 에러";
                }
            }
            catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }
        else {
            $successMSG = "이미 가입된 유저입니다.";
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
        ID: <input type = "text" name = "user" />
        <input type = "submit" name = "submit" />
    </form>

    </body>
    </html>

    <?php
}
?>




