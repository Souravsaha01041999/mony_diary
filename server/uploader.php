
<?php
if($_SERVER["REQUEST_METHOD"]=="POST")
{
$target_path=basename($_FILES['fu']['name']);
if(move_uploaded_file($_FILES['fu']['tmp_name'],$target_path))
{
echo "file upload successfully..!";
}
else
{
echo "sorry file not uploaded plz try again...!";
}
}
?>
<html>
<head><title>UPLOAD A FILE</title>
</head>
<body>
<form action="uploader.php" method="post" enctype="multipart/form-data">
select file:<input type="file" name="fu"><br>
<input type="submit" value="Upload Image">
</form>
</body>
</html>