<?php
include "connection.php";

?>

<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart</title>
    <link rel="stylesheet" href="cart_style.css">
</head>
<body>
<h3>Shopping Cart</h3>
<?php
include "connection.php";

if(isset($_GET["action"]))
{
    $id = $_GET["id"];
    $sql ="DELETE FROM store.item where id=".$id;
    mysqli_query($connection, $sql);
}

$query = "SELECT * FROM store.item WHERE cart_id LIKE '1'";
$result = mysqli_query($connection, $query);
$total_price = 0;
if(mysqli_num_rows($result)>0) {
    echo "<table border = \"1\">";
    echo "<thead>";
    echo "<tr>";
    echo "<th>Product Name</th>";
    echo "<th>Category</th>";
    echo "<th>Price</th>";
    echo "</tr>";
    echo "</thead>";
    while ($row = mysqli_fetch_array($result)){
        $total_price += $row['price'];
        ?>
        <tr>
        <th><?php echo $row['name']; ?></th>
        <th><?php echo $row['category'] ?></th>
        <th><?php echo $row['price'] ?></th>
        <td><a href="cart.php?action=delete&id=<?php echo $row["id"]; ?>"><span class="text-danger">Remove</span></a></td>
        </tr>
        <?php
    }
    echo "</table>";
}
echo "<span id='cart_total'>Total Price: " .$total_price. "</span>";
$connection->close();
?>
</body>
</html>