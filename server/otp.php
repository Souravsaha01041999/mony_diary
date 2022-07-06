<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $uid=$_POST['uid'];
    $otp=$_POST['otp'];
$subject="YOUR OTP";
$headers="From:xlrppt@gmail.com"."\r\n";
    
    //mail($uid,$subject,$otp,$headers);
    $url = "https://admin.apna-bazar.in/apis/get_mail.php";
$data = http_build_query( array( "from"=> "xlrppt@gmail.com","to"=>"$uid"
,"subject"=>"$subject","txt"=>"$otp" ) );
 
$options = array(
    'http' => array(
        'header'  => "Content-type: application/x-www-form-urlencoded",
        'method'  => 'POST',
        'content' => $data,
    ),
);
$context = stream_context_create( $options );
 
$result = file_get_contents( $url, false, $context );
         
         echo "1";
}
?>