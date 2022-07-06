<html>
    <head>
        <title>Print</title>
        <link rel="stylesheet" href="https://souravsaha1234.000webhostapp.com/removeadd/removeadd.css">
    </head>
    <body>
        <button onclick="trg()">PRINT</button>
        <div id="pcon">
        <table width="100%" border="2">
       <tr>
       <th>items</th>
       <th>title</th>
       <th>price</th>
       <th>date</th>
       </tr>
        <?php
         $count=1;
         $total=0;
        require_once('concention.php');
        $id=$_GET['id'];
        $month=$_GET['month'];
        $alldata="SELECT * FROM `mony_diary_acc` WHERE `uid`='$id' AND `date` LIKE '%$month'";
        $rt=mysqli_query($con,$alldata);
       while($rw=mysqli_fetch_assoc($rt))
        {
            if($rw['price']!=0)
            {
            ?>
            <tr>
                <td>  <?php  echo $count; ?>  </td>
                <td><?php  echo $rw['title']; ?></td>
                <td><?php  
                           $total+=$rw['price'];
                            echo $rw['price']; 
                         ?></td>
               <td><?php  echo $rw['date']; ?></td>
             </tr>
          <?php
          $count+=1;
            }
         }
        ?>
        </table>
        <h2>Total: <?php echo $total;?></h2>
        </div>
        
        <script>
           function trg()
           {
                var divElementContents = document.getElementById("pcon").innerHTML;
			    var windows = window.open("", "", "height=600, width=600");
			    windows.document.write(divElementContents);
 			    windows.document.close();
 			    windows.print();
           }
        </script>
    </body>
</html>