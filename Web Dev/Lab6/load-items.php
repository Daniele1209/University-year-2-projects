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

$page_results = 4;
$category = $_POST['category'];
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
$query = "SELECT * FROM store.product WHERE category LIKE '$category' LIMIT $offset,$page_results";
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
