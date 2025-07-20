<?php

include "connect.php";
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'PHPMailer/src/Exception.php';
require 'PHPMailer/src/PHPMailer.php';
require 'PHPMailer/src/SMTP.php';

$email = $_POST['email'] ?? '';

$query = 'SELECT * FROM `user` WHERE `email`="' . mysqli_real_escape_string($conn, $email) . '"';
$data = mysqli_query($conn, $query);

$result = array();
while ($row = mysqli_fetch_assoc($data)) {
    $result[] = $row;
}

if (empty($result)) {
    $arr = [
        'success' => false,
        'message' => "Mail không chính xác",
        'result' => $result
    ];
    echo json_encode($arr);
    exit();
} else {
    $email = $result[0]["email"];
    $pass = $result[0]["pass"];
    $link = "<a href='http://192.168.1.5/banhang/reset_pass.php?key=" . urlencode($email) . "&reset=" . urlencode($pass) . "'>Click To Reset Password</a>";

    $mail = new PHPMailer(true);
    try {
        $mail->CharSet = "utf-8";
        $mail->isSMTP();
        $mail->SMTPAuth = true;
        $mail->Username = "namduchuong204@gmail.com";
        $mail->Password = "lwndrsxxlyclughb"; // Cẩn thận với việc lộ mật khẩu
        $mail->SMTPSecure = "ssl";
        $mail->Host = "smtp.gmail.com";
        $mail->Port = 465;

        $mail->setFrom("plinh204@gmail.com", 'App ban hang');
        $mail->addAddress($email, 'Người nhận');
        $mail->Subject = 'Reset Password';
        $mail->isHTML(true);
        $mail->Body = 'Click vào link sau để đặt lại mật khẩu: ' . $link;

        $mail->send();

        $arr = [
            'success' => true,
            'message' => "Hãy kiểm tra email và nhấn vào liên kết để đặt lại mật khẩu",
            'result' => $result
        ];
    } catch (Exception $e) {
        $arr = [
            'success' => false,
            'message' => "Lỗi gửi mail: " . $mail->ErrorInfo,
            'result' => []
        ];
    }

    echo json_encode($arr);
}
?>
