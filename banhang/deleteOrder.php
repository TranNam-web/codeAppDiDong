<?php
include "connect.php";

// Nhận dữ liệu từ client
$id = $_POST['iddonhang'];

// Xóa chi tiết đơn hàng trước nếu có liên kết FK
$query1 = "DELETE FROM `chitietdonhang` WHERE `iddonhang` = '$id'";
$deleteChitiet = mysqli_query($conn, $query1);

// Sau đó xóa đơn hàng
$query = "DELETE FROM `donhang` WHERE `id` = '$id'";
$deleteDonhang = mysqli_query($conn, $query);

if ($deleteDonhang) {
    $arr = [
        'success' => true,
        'message' => "Xóa đơn hàng thành công",
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "Xóa đơn hàng thất bại",
    ];
}

// Trả kết quả về client
echo json_encode($arr);
?>
