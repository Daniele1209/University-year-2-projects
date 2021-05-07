<?php
header("Access-Control-Allow-Headers: *");
header("Access-Control-Allow-Origin: *");
require_once "configuration.php";
global $connection;
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);
$id = $request->id;

$sql = "DELETE FROM item WHERE id='$id'";
$result = mysqli_query($connection, $sql);

mysqli_close($connection);
