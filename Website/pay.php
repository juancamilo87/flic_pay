	<?php
// echo phpinfo();
// require "util/httpful.phar";
$user = "flic_pay";
$password = "Flic_Junc_15";
$database = "flic_pay";


$link = mysqli_connect("localhost", $user, $password, $database);

$flic_id = $_GET['flic_id'];
$password = $_GET['password'];
$amount =  $_GET['amount'];

$query = "SELECT * FROM flic_pay_links WHERE 
        flic_id like '".$flic_id."' AND 
        password like '".$password."' AND
        status = 1";


  $result = mysqli_query($link, $query);

  if(! $result )
  {
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message1: ' . mysqli_error($link));
  }
  
  $response = array();

  if ($result->num_rows > 0) {
      // output data of each row
      while($row = $result->fetch_assoc()) {

          
          $account_id = $row["account_id"];
      }
  } 
  else
  {
    header('X-PHP-Response-Code: 420', true, 420);
    die('Wrong password2: ' . mysqli_error($link));
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
  
  $response = array();
  $canPay = false;
  if ($result->num_rows > 0) {
      // output data of each row
      while($row = $result->fetch_assoc()) {
            $balance = $row["balance"];
          if($balance>$amount)
          {
            $canPay = true;
          }
          else
          {
            header('X-PHP-Response-Code: 421', true, 421);
            die('Not enough funds4: ' . mysqli_error($link));
          }
      }
  } 
  else
  {
    header('X-PHP-Response-Code: 420', true, 420);
    die('No Funds registered10: ' . mysqli_error($link));
  }

$new_balance = $balance - $amount;
if($canPay)
{

  $query = "UPDATE balance SET balance = ".$new_balance." 
            WHERE account_id like '".$account_id."'";

  $result = mysqli_query($link, $query);

  if(! $result )
  {
  //  echo 'Erro'."\xA";
    header('X-PHP-Response-Code: 409', true, 409);
    die('Could not post message6: ' . mysqli_error($link));
  }


}
else
{
  header('X-PHP-Response-Code: 409', true, 409);
  die('FlicPay not registered7: ' . mysqli_error($link));
}
/* close connection */
mysqli_close($link);
echo json_encode("Payed");

header('X-PHP-Response-Code: 200', true, 200);



?>