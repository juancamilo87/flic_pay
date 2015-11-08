	<?php
// echo phpinfo();
// require "util/httpful.phar";
$user = "flic_pay";
$password = "Flic_Junc_15";
$database = "flic_pay";


$link = mysqli_connect("localhost", $user, $password, $database);

//echo 'Connected to database'."\xA";
/* check connection */
if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

$query = "SELECT * FROM user";

$result = mysqli_query($link, $query);

  if(! $result )
  {

    echo mysqli_error($link);
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message1: ' . mysqli_error($link));
  }
  
  $data = array();

  if ($result->num_rows > 0) {
      // output data of each row
      while($row = $result->fetch_assoc()) {

          $response = array();
          $response["name"] = $row["name"];
          $account_id = $row["account_id"];
          $response["account_id"] = $account_id;

          $query2 = "SELECT * FROM balance WHERE 
                account_id like '".$account_id."'";

          $result2 = mysqli_query($link, $query2);

          if(! $result2 )
          {
          //  echo 'Erro'."\xA";
            header('X-PHP-Response-Code: 409', true, 409);
            die('Could not post message3: ' . mysqli_error($link));
          }

          if ($result2->num_rows > 0) {
              // output data of each row
              while($row2 = $result2->fetch_assoc()) {
                  
                  $response["balance"] = $row2["balance"];
              }
          }
          $data[] = $response;
      }
  } 
  else
  {
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message2: ' . mysqli_error($link));
  }
//echo 'Connected to database'."\xA";
/* check connection */
if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

  



/* close connection */
mysqli_close($link);
echo json_encode($data);

header('X-PHP-Response-Code: 200', true, 200);



?>