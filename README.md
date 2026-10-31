# Auto Reboot (ô khởi động lại trong bảng kéo xuống)

Thêm một ô "Khởi động lại" vào bảng Cài đặt nhanh. Chạm một lần là máy khởi động lại,
không cần nút nguồn. Dùng quyền Device Owner (`DevicePolicyManager.reboot`), dự phòng root.

## 1. Build ra APK

1. Tạo repo GitHub, đẩy toàn bộ thư mục này lên nhánh `main`.
2. Tab **Actions** > **Build APK** > chờ chạy xong > tải **AutoReboot-apk** ở mục Artifacts > giải nén ra `app-debug.apk`.

Hoặc mở bằng Android Studio > Build > Build APK(s).

## 2. Cài và cấp quyền (một lần)

1. Cài `app-debug.apk`.
2. Xóa hết tài khoản trên máy (Google, OPPO/Heytap...). Android chỉ cho cấp Device Owner khi không còn tài khoản.
3. Bật Gỡ lỗi USB, cắm máy tính, chạy:
   ```
   adb shell dpm set-device-owner com.dc.autoreboot/.AdminReceiver
   ```
   Thấy `Success` là xong, rồi thêm lại tài khoản.

## 3. Thêm ô vào bảng kéo xuống

Kéo thanh thông báo xuống > biểu tượng bút chì (Chỉnh sửa) > kéo ô **Khởi động lại** lên khu vực đang dùng.
Từ đó, kéo xuống và chạm ô này để reboot.

## Ghi chú

- Nhấn giữ ô để mở màn hình trạng thái, có nút **Gỡ quyền Device Owner**. Hãy gỡ quyền trước khi gỡ cài đặt app.
- Android từ chối reboot khi đang có cuộc gọi.
- Ô chỉ hiện ở màn hình khóa nếu ColorOS cho phép; máy có thể yêu cầu mở khóa trước khi dùng ô.
- Lỗi `Not allowed to set the device owner because there are already some accounts`: còn tài khoản, xóa hết rồi chạy lại.
