<?php
include "connect.php";

if (isset($_POST['tensp'], $_POST['giasp'], $_POST['hinhanh'], $_POST['mota'], $_POST['loai'], $_POST['id'], $_POST['linkvideo'], $_POST['slsp'])) {
    // Nhận dữ liệu
    $tensp = $_POST['tensp'];
    $gia = $_POST['giasp']; // Sửa ở đây
    $hinhanh = $_POST['hinhanh'];
    $mota = $_POST['mota'];
    $loai = $_POST['loai'];
    $id = $_POST['id'];
    $linkvideo = $_POST['linkvideo'];
    $sltonkho = $_POST['slsp'];

    $query = "UPDATE `sanphammoi` SET 
        `tensp`='$tensp',
        `giasp`='$gia',
        `hinhanh`='$hinhanh',
        `mota`='$mota',
        `loai`='$loai',
        `linkvideo`='$linkvideo',
        `sltonkho`='$sltonkho'
        WHERE `id`='$id'";

    $data = mysqli_query($conn, $query);

    if ($data) {
        echo json_encode([
            'success' => true,
            'message' => "Thành công",
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => "Không thành công: " . mysqli_error($conn),
        ]);
    }
} else {
    echo json_encode([
        'success' => false,
        'message' => "Thiếu dữ liệu đầu vào",
    ]);
}
?>
