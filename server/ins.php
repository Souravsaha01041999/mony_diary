<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $id=$_POST['id'];
    $title=$_POST['title'];
    $price=$_POST['price'];
    $date=$_POST['date'];
    $uid=$_POST['uid'];
    
         $sql2="INSERT INTO `mony_diary_acc` (`id`,`title`,`price`,`date`,`uid`) VALUES ('$id','$title','$price','$date','$uid');";
         $rt=mysqli_query($con,$sql2);
        
         echo "1";
}
?>