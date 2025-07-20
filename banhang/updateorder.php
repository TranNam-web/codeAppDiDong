<?php
include "connect.php";
$id = $_POST['id'];
$trangthai = $_POST['trangthai'];
// Viết câu lệnh UPDATE đúng cú pháp
$query = "UPDATE `donhang` SET `trangthai` = '$trangthai' WHERE `id` = '$id'";

$data = mysqli_query($conn, $query);

if ($data == true) {
    $arr = [
        'success' => true,
        'message' => "Thành công",
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "Không thành công",
    ];
}

// Trả kết quả về client
echo json_encode($arr);
?>
