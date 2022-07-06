<?php
require_once('concention.php');
$id=$_GET['id'];
$month=$_GET['month'];
$sum=0;


echo "date,price<br>";
for($date=1;$date<=31;$date=$date+1)
{
    $tempdate=strval(sprintf('%02d', $date))."/".$month;
    $alldata="SELECT * FROM `mony_diary_acc` WHERE `uid`='$id' AND `date`='$tempdate'";
    $rt=mysqli_query($con,$alldata);
    while($rw=mysqli_fetch_assoc($rt))
    {
        $sum=$sum+$rw['price'];
    }
    echo $tempdate.",".strval($sum)."<br>";
    $sum=0;
}

mysqli_close($con);
?>