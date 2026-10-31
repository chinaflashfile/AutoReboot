package com.dc.autoreboot;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/** Màn hình hướng dẫn và trạng thái. Mở bằng icon app hoặc nhấn giữ ô trong bảng kéo xuống. */
public class MainActivity extends Activity {
    private TextView status;
    private Button removeOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);

        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, pad, pad, pad);
        sv.addView(root);

        status = new TextView(this);
        status.setTextSize(16);
        root.addView(status);

        TextView help = new TextView(this);
        help.setTextSize(14);
        help.setPadding(0, pad, 0, pad);
        help.setText("Cách dùng:\n"
                + "1. Bấm nút bên dưới, vào Ứng dụng đã cài đặt > Auto Reboot, bật công tắc.\n"
                + "   Nếu máy báo \"Cài đặt bị hạn chế\": vào Cài đặt > Ứng dụng > Auto Reboot > "
                + "menu 3 chấm góc phải > Cho phép cài đặt bị hạn chế, rồi bật lại công tắc.\n"
                + "2. Kéo thanh thông báo xuống, bấm biểu tượng bút chì, kéo ô \"Khởi động lại\" vào.\n"
                + "3. Từ đó, kéo xuống và chạm ô này để khởi động lại máy.");
        root.addView(help);

        Button open = new Button(this);
        open.setText("Mở cài đặt Trợ năng");
        open.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
        root.addView(open);

        removeOwner = new Button(this);
        removeOwner.setText("Gỡ quyền Device Owner (nếu đã cấp)");
        removeOwner.setOnClickListener(new View.OnClickListener() {
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
        root.addView(removeOwner);

        setContentView(sv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        String s;
        if (RebootHelper.isDeviceOwner(this)) {
            s = "Trạng thái: SẴN SÀNG (Device Owner).";
        } else if (RebootHelper.isAccessibilityReady()) {
            s = "Trạng thái: SẴN SÀNG (dịch vụ Trợ năng đang bật).";
        } else {
            s = "Trạng thái: CHƯA SẴN SÀNG. Hãy bật dịch vụ Trợ năng \"Auto Reboot\".";
        }
        status.setText(s);
        removeOwner.setVisibility(RebootHelper.isDeviceOwner(this) ? View.VISIBLE : View.GONE);
    }
}
