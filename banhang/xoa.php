<?php
include "connect.php";

// Nhận dữ liệu từ client và kiểm tra hợp lệ
$id = isset($_POST['id']) ? (int)$_POST['id'] : 0;

// Chuẩn bị câu lệnh để tránh SQL injection
$stmt = mysqli_prepare($conn, "DELETE FROM `sanphammoi` WHERE `id` = ?");
mysqli_stmt_bind_param($stmt, "i", $id);
$success = mysqli_stmt_execute($stmt);

if ($success) {
    $arr = [
        'success' => true,
        'message' => "Thành công",
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "Xoá không thành công",
    ];
}

// Trả kết quả về client
echo json_encode($arr);

// Đóng kết nối
mysqli_stmt_close($stmt);
mysqli_close($conn);
?>
