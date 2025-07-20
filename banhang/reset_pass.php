<?php
include "connect.php";

if (isset($_GET['key']) && isset($_GET['reset'])) {
    // Lọc dữ liệu đầu vào
    $email = mysqli_real_escape_string($conn, $_GET['key']);
    $pass = mysqli_real_escape_string($conn, $_GET['reset']);
    
    // Kiểm tra xem user có tồn tại với email và pass như trên không
    $query = "SELECT email FROM user WHERE email = '$email' AND pass = '$pass'";
    $data = mysqli_query($conn, $query);
    
    if ($data) {
        if (mysqli_num_rows($data) == 1) {
            ?>
            <form method="post" action="submit_new.php">
                <input type="hidden" name="email" value="<?php echo htmlspecialchars($email); ?>">
                <p>Enter New password</p>
                <input type="password" name="password" required>
                <input type="submit" name="submit_password" value="Submit">
            </form>
            <?php
        } else {
            echo "Email hoặc mật khẩu không đúng.";
        }
    } else {
        echo "Lỗi truy vấn: " . mysqli_error($conn);
    }
} else {
    echo "Thiếu thông tin.";
}
?>
