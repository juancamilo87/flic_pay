	<?php
// echo phpinfo();
// require "util/httpful.phar";
$user = "flic_pay";
$password = "Flic_Junc_15";
$database = "flic_pay";

$link = mysqli_connect("localhost", $user, $password, $database);

$flic_id = $_GET['flic_id'];
$password = $_GET['password'];
$account_id =  $_GET['account_id'];

$username  = $_GET['username'];
$other_password  = $_GET['other_password'];

$query = "SELECT * FROM user WHERE 
        username like '".$username."' AND 
        password like '".$other_password."'";

$result = mysqli_query($link, $query);

  if(! $result )
  {

    echo mysqli_error($link);
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message1: ' . mysqli_error($link));
  }
  
  $response = array();

  if ($result->num_rows < 1) {
      header('X-PHP-Response-Code: 420', true, 420);
    die('Wrong credentials: ' . mysqli_error($link));
  } 
  else
  {
  	while($row = $result->fetch_assoc()) {

          if($account_id != $row["account_id"])
          {
			header('X-PHP-Response-Code: 420', true, 420);
			die('Wrong account id: ' . mysqli_error($link));
          }
      }
  }


  $query = "INSERT INTO flic_pay_links (account_id, flic_id, password, status) VALUES ('".$account_id."','".$flic_id."','".$password."',1)";

  $result = mysqli_query($link, $query);

  if(! $result )
  {
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message: ' . mysqli_error($link));
  }
  
/* close connection */
mysqli_close($link);
echo json_encode("Added");

header('X-PHP-Response-Code: 200', true, 200);



?>