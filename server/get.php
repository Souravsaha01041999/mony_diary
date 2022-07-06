 
  <?php

    require_once('concention.php');
    if($_SERVER['REQUEST_METHOD'] == 'POST'){
        
        $uid=$_POST['uid'];
        $sql="SELECT * FROM `mony_diary_acc` WHERE uid='$uid'";
        $res= mysqli_query($con,$sql);
        $data="[";
          while($row=mysqli_fetch_assoc($res))
        {
            $data=$data.json_encode($row).',';
        }
       $data=substr($data,0,strlen($data)-1);
        $data=$data."]";
            echo $data;
    }
?>

