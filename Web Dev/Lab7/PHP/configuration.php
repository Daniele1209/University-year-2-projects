<?php

$connection = mysqli_connect("127.0.0.1", "root", "", "store");
if ($connection === false) {
  die("ERROR: " . mysqli_connect_error());
}
