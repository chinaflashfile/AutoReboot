package com.dc.autoreboot;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** Màn hình phụ: xem trạng thái quyền và gỡ Device Owner. Mở bằng cách nhấn giữ ô trong bảng kéo xuống. */
public class MainActivity extends Activity {
    private static final String ADB_CMD =
            "adb shell dpm set-device-owner com.dc.autoreboot/.AdminReceiver";

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, pad, pad, pad);

        status = new TextView(this);
        status.setTextSize(16);
        root.addView(status);

        TextView help = new TextView(this);
        help.setTextIsSelectable(true);
        help.setTextSize(13);
        help.setPadding(0, pad, 0, pad);
        help.setText("Cấp quyền một lần từ máy tính (xóa hết tài khoản trên máy trước):\n\n" + ADB_CMD
                + "\n\nSau đó kéo bảng thông báo xuống, bấm biểu tượng bút chì và kéo ô \"Khởi động lại\" vào.");
        root.addView(help);

        Button remove = new Button(this);
        remove.setText("Gỡ quyền Device Owner");
        remove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    dpm.clearDeviceOwnerApp(getPackageName());
                    Toast.makeText(MainActivity.this, "Đã gỡ", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Không gỡ được: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                refresh();
            }
        });
        root.addView(remove);

        setContentView(root);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        status.setText(RebootHelper.isDeviceOwner(this)
                ? "Quyền: ĐÃ là Device Owner. Ô khởi động lại sẵn sàng."
                : "Quyền: CHƯA có. Chạy lệnh ADB bên dưới.");
    }
}
