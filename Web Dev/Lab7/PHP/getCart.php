<?php
header("Access-Control-Allow-Headers: *");
header("Access-Control-Allow-Origin: *");
require_once 'configuration.php';
global $connection;
$sql = "SELECT * FROM store.item";
$result = mysqli_query($connection, $sql);


if ($result) {
  $total_rows = mysqli_num_rows($result);
  $product_array = array();

  while ($row = mysqli_fetch_array($result)) {
    array_push($product_array, array(
      "id" => (int)$row["id"],
      "name" => $row["name"],
      "category" => $row["category"],
      "price" => (float)$row["price"],
    ));
  }
  mysqli_free_result($result);
  echo json_encode($product_array);
}
mysqli_close($connection);
