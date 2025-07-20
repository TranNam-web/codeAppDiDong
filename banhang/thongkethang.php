<?php
include "connect.php";

$query = "
SELECT 
  YEAR(`ngaydat`) as nam,
  MONTH(`ngaydat`) as thang,
  SUM(tongtien) as tongtienthang
FROM donhang
GROUP BY YEAR(`ngaydat`), MONTH(`ngaydat`)
ORDER BY YEAR(`ngaydat`), MONTH(`ngaydat`)
";

$data = mysqli_query($conn, $query);
$result = array();

while ($row = mysqli_fetch_assoc($data)) {
    // đảm bảo tongtienthang không null
    if ($row['tongtienthang'] === null) {
        $row['tongtienthang'] = "0";
    } else {
        // chuyển thành string nếu cần
        $row['tongtienthang'] = (string)$row['tongtienthang'];
    }
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
        'message' => "khong thanh cong",
        'result' => []
    ];
}

header('Content-Type: application/json');
echo json_encode($arr);
?>
