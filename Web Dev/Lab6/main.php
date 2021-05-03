<?php
include "connection.php";

if (isset ($_POST["add-button"])) {
    $item_name = $_POST["item_name"];
    $item_category = $_POST["item_category"];
    $item_price = $_POST["item_price"];
    $cart_id = 1;
    $sql = "INSERT INTO store.item(name,category,price,cart_id) VALUES ('" . $item_name . "','" . $item_category . "','" . $item_price . "','" . $cart_id . "')";
    mysqli_query($connection, $sql);
}
?>

<!DOCTYPE html>
<html>
<head>

    <title>Store</title>
    <link rel="stylesheet" href="style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

<script>
    $(document).ready(function() {
        $("#hoodies-button").click(function () {
            var category = "Hoodie";
            $("#store-products").load("load-items.php", {category});
        });
    });

    $(document).ready(function() {
        $("#tshirt-button").click(function () {
            var category = "T-Shirt";
            $("#store-products").load("load-items.php", {category});
        });
    });

    $(document).ready(function() {
        $("#longsleeve-button").click(function () {
            var category = "Long Sleeve";
            $("#store-products").load("load-items.php", {category});
        });
    });

</script>

<div class="header">
    <h1>Singularity Store</h1>
    <button type="button" class="cart-button" onclick="location.href='cart.php'">CART</button>
    <nav>
        <ul>
            <li><button id="hoodies-button">Hoodies</button></li>
            <li><button id="tshirt-button">T-Shirts</button></li>
            <li><button id="longsleeve-button">Long Sleeve</button></li>
        </ul>
    </nav>
    <h3>Latest products :</h3>
</div>
<div id="store-products">
<?php
$page_results = 4;
$pages_sql = "SELECT COUNT(*) FROM store.product";
$result = mysqli_query($connection,$pages_sql);
$total_rows = mysqli_fetch_array($result)[0];
$total_pages = ceil($total_rows / $page_results);
if (isset ($_GET['page'])) {
    $selected_page = $_GET['page'];
}
else {
    $selected_page = 1;
}
$offset = ($selected_page - 1) * $page_results;
$item_index = 0;
$query = "SELECT * FROM store.product LIMIT $offset,$page_results";
$result = mysqli_query($connection, $query);
if(mysqli_num_rows($result)>0) {
    echo "<div id='items'>";
    while ($row = mysqli_fetch_array($result)) {
        $item_index ++;
        ?>
            <div id="item<?php echo $item_index ?>">
                <form method="post" action="main.php?action=add&id=<?php echo $row["id"]; ?>">
            <div class='img'><a><img alt='' src='img/<?php echo $row['image']; ?>'></a></div>
            <div class='info'><a class='title'><?php echo $row['name']; ?></a>
            <br>
            <a class='category'><?php echo $row['category'] ?></a>
            <div class='price'><span class='st'>Price:</span><strong>$<?php echo $row['price']; ?></strong></div>
            <input type="hidden" name="item_id" value="<?php echo $row['id']; ?>" />
            <input type="hidden" name="item_name" value="<?php echo $row['name']; ?>" />
            <input type="hidden" name="item_category" value="<?php echo $row['category']; ?>" />
            <input type="hidden" name="item_price" value="<?php echo $row['price']; ?>" />
            <input type='submit' name="add-button" class='add-button' value="ADD TO CART" />
            </form>
        </div>
        </div>

        <?php
    }
    echo "</div>";

}
$connection->close();
?>
</div>

<ul class="pages">
    <li><a href="?page=1">First</a></li>&emsp;
    <li class="<?php if($selected_page <= 1){ echo 'disabled'; } ?>">
        <a href="<?php if($selected_page <= 1){ echo '#'; } else { echo "?page=".($selected_page - 1); } ?>">Prev</a>
    </li>&emsp;
    <li class="<?php if($selected_page >= $total_pages){ echo 'disabled'; } ?>">
        <a href="<?php if($selected_page >= $total_pages){ echo '#'; } else { echo "?page=".($selected_page + 1); } ?>">Next</a>
    </li>&emsp;
    <li><a href="?page=<?php echo $total_pages; ?>">Last</a></li>&emsp;

</ul>

</body>
</html>
