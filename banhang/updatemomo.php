<?php
include "connect.php";

// Nhận dữ liệu từ client
$token = $_POST['token'];
$iddonhang = $_POST['id']; // Đã nhận đúng

// Viết câu lệnh UPDATE đúng cú pháp
$query = "UPDATE `donhang` SET `momo` = '$token' WHERE `id` = '$iddonhang'";

// Gửi câu lệnh SQL
$data = mysqli_query($conn, $query);

// Kiểm tra và trả kết quả
if ($data == true) {
    $arr = [
        'success' => true,
        'message' => "Thành công",
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "Không thành công",
        'error' => mysqli_error($conn) // Gợi ý: thêm dòng này để debug
    ];
}

echo json_encode($arr);
?>
