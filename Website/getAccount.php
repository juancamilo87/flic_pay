	<?php
// echo phpinfo();
// require "util/httpful.phar";
$user = "flic_pay";
$password = "Flic_Junc_15";
$database = "flic_pay";


$link = mysqli_connect("localhost", $user, $password, $database);

$username = $_GET['username'];
$password = $_GET['password'];
//echo 'Connected to database'."\xA";
/* check connection */
if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

$query = "SELECT * FROM user WHERE 
        username like '".$username."' AND 
        password like '".$password."'";

$result = mysqli_query($link, $query);

  if(! $result )
  {

    echo mysqli_error($link);
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message1: ' . mysqli_error($link));
  }
  
  $response = array();

  if ($result->num_rows > 0) {
      // output data of each row
      while($row = $result->fetch_assoc()) {

          
          $response["name"] = $row["name"];
          $account_id = $row["account_id"];
          $response["account_id"] = $account_id;
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

$query = "SELECT * FROM flic_pay_links WHERE 
        account_id like '".$account_id."'";

  $result = mysqli_query($link, $query);

  if(! $result )
  {
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message3: ' . mysqli_error($link));
  }

  if ($result->num_rows > 0) {
      // output data of each row
      while($row = $result->fetch_assoc()) {
          
          $response["flic_id"] = $row["flic_id"];
          $response["status"] = $row["status"];
      }
  } 

  $query = "SELECT * FROM balance WHERE 
        account_id like '".$account_id."'";

  $result = mysqli_query($link, $query);

  if(! $result )
  {
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message3: ' . mysqli_error($link));
  }

  if ($result->num_rows > 0) {
      // output data of each row
      while($row = $result->fetch_assoc()) {
          
          $response["balance"] = $row["balance"];
      }
  } 



/* close connection */
mysqli_close($link);
echo json_encode($response);

header('X-PHP-Response-Code: 200', true, 200);



?>