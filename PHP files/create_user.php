
<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

$stmt = $con->prepare('select * from Snack_Score');
$stmt->execute();

if ($stmt->rowCount() > 0) {
    $data = array();
    while($row=$stmt->fetch(PDO::FETCH_ASSOC))
    {
        $snack_name = preg_replace("/\s+/", "", $row['Name']);

        echo $snack_name;

        /*        $create_each_snack_table = $con->prepare('CREATE TABLE '.$snack_name.' (
                id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                Sweet DOUBLE(7,2) UNSIGNED DEFAULT 0,
                Spicy DOUBLE(7,2) UNSIGNED DEFAULT 0,
                Sour DOUBLE(7,2) UNSIGNED DEFAULT 0,
                Bitter DOUBLE(7,2) UNSIGNED DEFAULT 0
                )');

                $create_each_snack_table->execute();*/


    }
}

?>