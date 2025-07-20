<?php
include "connect.php";

// Nhận dữ liệu từ client
$tensp = $_POST['tensp'];
$gia = $_POST['gia'];
$hinhanh = $_POST['hinhanh'];
$mota = $_POST['mota'];
$loai = $_POST['loai'];
$linkvideo = $_POST['linkvideo'];
$sltonkho = $_POST['slsp'];

$query = "INSERT INTO `sanphammoi`(`tensp`, `giasp`, `hinhanh`, `mota`, `loai`, `linkvideo`, `sltonkho`) 
          VALUES ('$tensp', $gia, '$hinhanh', '$mota', '$loai', '$linkvideo', $sltonkho)";
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
