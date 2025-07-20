<?php  
include "connect.php";

$target_dir = "images/";
$response = array();  

// Kiểm tra ID lớn nhất trong bảng sanphammoi
$query = "SELECT MAX(id) AS id FROM sanphammoi";
$data = mysqli_query($conn, $query);
$result = mysqli_fetch_assoc($data);

// Nếu chưa có bản ghi nào trong bảng, bắt đầu từ ID 1
$name = ($result['id'] === null) ? 1 : ++$result['id'];

// Lấy đuôi file và kiểm tra định dạng
$ext = pathinfo($_FILES["file"]["name"], PATHINFO_EXTENSION);
$allowed_extensions = array("jpg", "jpeg", "png", "gif");
if (!in_array(strtolower($ext), $allowed_extensions)) {
    $response['success'] = false;
    $response['message'] = "Chỉ chấp nhận các file hình ảnh (JPG, PNG, GIF).";
    echo json_encode($response);
    exit;
}

$target_file_name = $target_dir . $name . "." . $ext;

if (isset($_FILES["file"])) {  
    if ($_FILES["file"]["error"] !== 0) {
        $response['success'] = false;
        $response['message'] = "Lỗi khi upload: " . $_FILES["file"]["error"];
    } elseif (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name)) {
        $response['success'] = true;
        $response['message'] = "Upload thành công!";
        $response['name'] = $name . "." . $ext; // Trả lại tên file đã upload
    } else {
        $response['success'] = false;
        $response['message'] = "Không thể di chuyển file vào thư mục 'images/'";
    }
} else {
    $response['success'] = false;
    $response['message'] = "Không nhận được file!";
}

echo json_encode($response);  
?>
