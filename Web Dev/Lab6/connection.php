<?php
$connection = mysqli_connect("127.0.0.1", "root", "", "store");
if (!$connection) {
    die("Could not connect !" . mysqli_connect_error());
}