<?php
include "connect.php";

$search = trim($_POST['search']); // Xóa khoảng trắng thừa

// Nếu rỗng thì trả về luôn, không cho tìm
if (empty($search)) {
    echo json_encode([
        'success' => false,
        'message' => "Từ khóa trống",
        'result' => []
    ]);
    exit;
}

// Bảo vệ SQL Injection
$search_safe = mysqli_real_escape_string($conn, $search);

// Truy vấn chính xác từ khóa (không trả kết quả lạc)
$query = "SELECT * FROM `sanphammoi` WHERE `tensp` LIKE '%$search_safe%'";
$data = mysqli_query($conn, $query);
$result = array();

while ($row = mysqli_fetch_assoc($data)) {
    $result[] = $row;
}

if (!empty($result)) {
    $arr = [
        'success' => true,
        'message' => "thanh cong",
        'result' => $result
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "khong tim thay san pham",
        'result' => []
    ];
}

echo json_encode($arr);
?>
