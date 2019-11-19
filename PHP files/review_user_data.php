<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
{
    //$name=preg_replace("/\s+/", "", $_POST['name']);;
    $user_id = $_POST['user_id'];
    $name = $_POST['name'];
    $taste = $_POST['taste'];
    $cost = $_POST['cost'];
    $key1=$_POST['key1'];
    $key2=$_POST['key2'];
    $key3=$_POST['key3'];
    $have_reviewed = $_POST['have_reviewed']; // 1이면 리뷰를 단 적 있음 -> update 해야함. 0이면 리뷰를 단 적 없음 -> insert 해야함

    if(empty($sweet) && empty($spicy) && empty($sour) && empty($bitter)) {
        $errMSG = "맛 점수를 입력하세요.";
    }

    //$get_past_data = $con->prepare('select * from'.$name);
    $stmt = $con->prepare('select * from '.$user_id);  // 과자의 review에서 고른 3가지 keyword의 점수를 1씩 올리는 파일.
    $stmt->execute();
    if ($stmt->rowCount() > 0 && (int)$have_reviewed == 1) { // 과거에 리뷰를 단 적 있음 -> update
        $data = array();
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) { // $row[] : 기존 각 keyword 별 점수
            if ($row['Name'] == $name) {

                $update_review = $con->prepare('UPDATE ' . $user_id . ' SET Taste = :taste, Cost = :cost, Keyword_One = :key1, Keyword_Two = :key2, Keyword_Three = :key3 WHERE Name = :name');
                $update_review->bindParam(':taste', $taste);
                $update_review->bindParam(':cost', $cost);
                $update_review->bindParam(':keyword1', $key1);
                $update_review->bindParam(':keyword2', $key2);
                $update_review->bindParam(':keyword3', $key3);
                if ($update_review->execute()) {
                    $successMSG = "새로운!! 유저를 추가했습니다.";
                } else {
                    $errMSG = "유저!! 추가 에러";
                }
                break;
            }
        }
    }
    else { // 리뷰를 단 적 없을 때 -> insert
        $insert_review = $con->prepare('INSERT INTO ' .$user_id. '(Name, Taste, Cost, Keyword_One, Keyword_Two, Keyword_Three) VALUES (:name, :taste, :cost, :keyword1, :keyword2, :keyword3)');
        $insert_review->bindParam(':name', $name);
        $insert_review->bindParam(':taste', $taste);
        $insert_review->bindParam(':cost', $cost);
        $insert_review->bindParam(':keyword1', $key1);
        $insert_review->bindParam(':keyword2', $key2);
        $insert_review->bindParam(':keyword3', $key3);
        if($insert_review->execute()) {
            $successMSG = "새로운 유저를 추가했습니다.";
        }
        else {
            $errMSG = "유저 추가 에러";
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
        id: <input type = "text" name = "user_id" /><br>
        name: <input type = "text" name = "name" /><br>
        taste: <input type = "text" name = "taste" /><br>
        cost: <input type = "text" name = "cost" /><br>
        key1: <input type = "text" name = "key1" />
        key2: <input type = "text" name = "key2" />
        key3: <input type = "text" name = "key3" />
        have: <input type = "text" name = "have_reviewed" />
        <input type = "submit" name = "submit" />
    </form>
    </body>
    </html>

    <?php
}
?>




