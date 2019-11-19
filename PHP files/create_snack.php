
<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$stmt = $con->prepare('select * from Snack_Score');
$stmt->execute();

if ($stmt->rowCount() > 0) {
    $data = array();
    while($row=$stmt->fetch(PDO::FETCH_ASSOC))  // 각 과자의 이름으로 table을 만드는 파일
    {
        $snack_name = preg_replace("/\s+/", "", $row['Name']);

        //echo $snack_name<br>;

/*        $create_each_snack_table = $con->prepare('CREATE TABLE '.$snack_name.' (
        id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Sweet DOUBLE(7,2) UNSIGNED DEFAULT 0, 
        Spicy DOUBLE(7,2) UNSIGNED DEFAULT 0,
        Sour DOUBLE(7,2) UNSIGNED DEFAULT 0,
        Bitter DOUBLE(7,2) UNSIGNED DEFAULT 0
        )');
        $create_each_snack_table->execute();*/

        /*$zero = 0;
        $insert_default = $con->prepare('INSERT INTO '.$snack_name.'(id) VALUES :id');
        $insert_default->bindParam(':id', $zero);
        $insert_default->execute();*/


        /*$delete_each_snack_table = $con->prepare('DROP TABLE '.$snack_name);
        $delete_each_snack_table->execute();*/
    }
}

?>