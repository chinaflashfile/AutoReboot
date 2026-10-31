# Auto Reboot (ô khởi động lại trong bảng kéo xuống)

Thêm một ô "Khởi động lại" vào bảng Cài đặt nhanh. Chạm một lần là máy khởi động lại,
không cần nút nguồn. Cài như app bình thường, **không cần xóa tài khoản, không cần máy tính**.

## Cách hoạt động

Khi chạm ô, app dùng dịch vụ Trợ năng để mở menu nguồn của hệ thống rồi tự bấm "Khởi động lại"
(và nút xác nhận nếu có). Dịch vụ chỉ hành động trong 10 giây sau khi bạn chạm ô.

## Cài đặt

1. Tải `app-debug.apk` từ tab **Actions** > lần chạy mới nhất > Artifacts > **AutoReboot-apk**, cài lên máy.
2. Mở app, bấm **Mở cài đặt Trợ năng**, vào Ứng dụng đã cài đặt > **Auto Reboot** và bật công tắc.
   - Nếu máy báo "Cài đặt bị hạn chế" (Android 13 trở lên): Cài đặt > Ứng dụng > Auto Reboot > menu 3 chấm > **Cho phép cài đặt bị hạn chế**, rồi bật lại công tắc.
3. Kéo thanh thông báo xuống > biểu tượng bút chì > kéo ô **Khởi động lại** vào.

## Tùy chọn: Device Owner (ổn định hơn, nhưng phải xóa hết tài khoản trước)

```
adb shell dpm set-device-owner com.dc.autoreboot/.AdminReceiver
```
Khi là Device Owner, app gọi thẳng lệnh reboot của hệ thống mà không cần bấm menu nguồn.

## Ghi chú

- Nếu ColorOS đổi tên nút trong menu nguồn, sửa danh sách `LABELS` trong `RebootAccessibilityService.java`.
- Ở màn hình khóa, ColorOS có thể không cho mở menu nguồn hoặc yêu cầu mở khóa trước.
- Android từ chối reboot khi đang có cuộc gọi.
