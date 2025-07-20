<?php
include "connect.php";
$status = $_POST['status']; // thêm dấu chấm phẩy

$query = "SELECT * FROM `user` WHERE `status` = '$status'"; // thêm dấu nháy đơn quanh $status để tránh lỗi khi là chuỗi

$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
	$result[] = ($row);
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
		'message' => " khong thanh cong",
		'result' => $result
	];
}

echo json_encode($arr); // thay print_r bằng echo
?>
