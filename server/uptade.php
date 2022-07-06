<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $id=$_POST['id'];
    $title=$_POST['title'];
    $price=$_POST['price'];
    
         $sql2="UPDATE `mony_diary_acc` SET `title`='$title',`price`='$price' WHERE `id`='$id'";
         $rt=mysqli_query($con,$sql2);
        
         echo "1";
}
?>