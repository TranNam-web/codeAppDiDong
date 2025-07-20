<?php
include "connect.php";

// Nhận dữ liệu từ client
$email = $_POST['email'];
$pass = $_POST['pass'];
$username = $_POST['username'];
$mobile = $_POST['mobile'];
$uid = $_POST['uid'];
$token = $_POST['token'];
// Kiểm tra xem email đã tồn tại chưa
$query = 'SELECT * FROM `user` WHERE `email` = "'.$email.'"';
$data = mysqli_query($conn, $query);
$numrow = mysqli_num_rows($data);

if ($numrow > 0) {
    $arr = [
        'success' => false,
        'message' => "Email đã tồn tại",
    ];
} else {
    // Thêm người dùng mới
    $query = 'INSERT INTO `user`(`email`, `pass`, `username`, `mobile`,`uid`,`token`) 
              VALUES ("'.$email.'", "'.$pass.'", "'.$username.'", "'.$mobile.'", "'.$uid.'","'.$token.'")';
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
}

// Trả kết quả về client
echo json_encode($arr);
?>
