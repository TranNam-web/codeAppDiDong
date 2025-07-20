<?php
include "connect.php";

$sdt = $_POST['sdt'];
$email = $_POST['email'];
$tongtien = $_POST['tongtien'];
$iduser = $_POST['iduser'];
$diachi = $_POST['diachi'];
$soluong = $_POST['soluong'];
$chitiet = $_POST['chitiet'];
$trangthai = $_POST['trangthai'];
$momo = $_POST['momo'];

// Thêm đơn hàng
$query = 'INSERT INTO `donhang`(`iduser`, `diachi`, `sodienthoai`, `email`, `soluong`, `tongtien`, `trangthai`, `momo`) 
          VALUES ("'.$iduser.'", "'.$diachi.'", "'.$sdt.'", "'.$email.'", "'.$soluong.'", "'.$tongtien.'", "'.$trangthai.'", "'.$momo.'")';

$data = mysqli_query($conn, $query);

if ($data) {
    // Lấy id đơn hàng vừa thêm
    $query = 'SELECT id AS iddonhang FROM `donhang` WHERE `iduser` = "'.$iduser.'" ORDER BY id DESC LIMIT 1';
    $data = mysqli_query($conn, $query);
    $result = mysqli_fetch_assoc($data);

    if (!empty($result)) {
        $iddonhang = $result["iddonhang"];
        $chitietArray = json_decode($chitiet, true); // Chuyển JSON thành mảng

        foreach ($chitietArray as $value) {
            $idsp = $value["idsp"];
            $soluongSP = $value["soluong"];
            $giaSP = $value["giasp"];

            // Thêm chi tiết đơn hàng
            $truyvan = "INSERT INTO `chitietdonhang`(`iddonhang`, `idsp`, `soluong`, `gia`) 
                        VALUES ($iddonhang, $idsp, $soluongSP, $giaSP)";
            mysqli_query($conn, $truyvan);

            // Trừ số lượng tồn kho
            $truyvankho = "SELECT `sltonkho` FROM `sanphammoi` WHERE `id` = $idsp";
            $kqkho = mysqli_query($conn, $truyvankho);
            $sltrenkho = mysqli_fetch_assoc($kqkho);

            if ($sltrenkho && isset($sltrenkho["sltonkho"])) {
                $slmoi = $sltrenkho["sltonkho"] - $soluongSP;
                if ($slmoi < 0) $slmoi = 0;

                $truyvankho2 = "UPDATE `sanphammoi` SET `sltonkho` = $slmoi WHERE `id` = $idsp";
                mysqli_query($conn, $truyvankho2);
            }
        }

        $arr = [
            'success' => true,
            'message' => "Thêm đơn hàng thành công",
            'iddonhang' => $iddonhang,
            'result' => $result
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Không tìm thấy đơn hàng sau khi thêm",
            'result' => []
        ];
    }
} else {
    $arr = [
        'success' => false,
        'message' => "Thêm đơn hàng thất bại: " . mysqli_error($conn),
        'result' => []
    ];
}

header('Content-Type: application/json; charset=utf-8');
echo json_encode($arr);
?>
