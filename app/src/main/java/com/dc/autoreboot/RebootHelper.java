package com.dc.autoreboot;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

final class RebootHelper {
    private RebootHelper() {}

    static boolean isDeviceOwner(Context c) {
        DevicePolicyManager dpm = (DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm.isDeviceOwnerApp(c.getPackageName());
    }

    /** Khởi động lại bằng Device Owner; nếu máy đã root thì dự phòng bằng su. */
    static boolean reboot(Context c) {
        try {
            if (isDeviceOwner(c)) {
                DevicePolicyManager dpm = (DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE);
                dpm.reboot(new ComponentName(c, AdminReceiver.class));
                return true;
            }
        } catch (Exception ignored) {
            // ví dụ đang có cuộc gọi: Android từ chối reboot
        }
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
            return p.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
