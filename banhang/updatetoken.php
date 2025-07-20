<?php
include "connect.php";

// Nhận dữ liệu từ client
$token = $_POST['token'];
$id = $_POST['id']; // Thêm nhận ID để biết sản phẩm nào cần UPDATE

// Viết câu lệnh UPDATE đúng cú pháp
$query = "UPDATE `user` SET `token` = '$token' WHERE `id` = '$id'";

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
