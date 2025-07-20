<?php
include "connect.php";

$iduser = intval($_POST['iduser']); // Ép kiểu an toàn
if ($iduser ==0) {
    // get all don hang
  $query = 'SELECT donhang.id ,donhang.diachi,donhang.sodienthoai,donhang.email,donhang.soluong,donhang.tongtien,donhang.trangthai,donhang.ngaydat,user.username FROM `donhang` INNER JOIN user ON donhang.iduser = user.id ORDER BY donhang.id DESC';


}else{

$query = 'SELECT * FROM `donhang` WHERE `iduser` = ' . $iduser . ' ORDER BY id DESC';
}


$data = mysqli_query($conn, $query);

$result = array();

while ($row = mysqli_fetch_assoc($data)) {
    $truyvan = 'SELECT * FROM `chitietdonhang` INNER JOIN sanphammoi ON chitietdonhang.idsp = sanphammoi.id WHERE chitietdonhang.iddonhang = ' . $row['id'];
    $data1 = mysqli_query($conn, $truyvan);

    $item = array();
    while ($row1 = mysqli_fetch_assoc($data1)) {
        $item[] = $row1;
    }

    $row['item'] = $item;
    $result[] = $row;
}

if (!empty($result)) {
    $arr = [
        'success' => true,
        'message' => "thành công",
        'result' => $result
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "không thành công",
        'result' => []
    ];
}

echo json_encode($arr); // Dùng echo thay vì print_r
?>
