<?php
header("Access-Control-Allow-Headers: *");
header("Access-Control-Allow-Origin: *");
require_once "configuration.php";
global $connection;

$postdata = file_get_contents("php://input");
$request = json_decode($postdata);
$name = $request->item_name;
$category = $request->item_category;
$price = $request->item_price;
$cartId = 1;
$sql = "INSERT INTO store.item (name, category, price, cart_id) VALUES ('" . $name . "','" . $category . "','" . $price . "','" . $cartId . "')";

mysqli_query($connection, $sql);
mysqli_close($connection);
