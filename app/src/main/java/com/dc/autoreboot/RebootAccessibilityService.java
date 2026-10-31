package com.dc.autoreboot;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.List;
import java.util.Locale;

/**
 * Mở menu nguồn của hệ thống rồi tự bấm "Khởi động lại" (và nút xác nhận nếu có).
 * Chỉ hoạt động trong 10 giây sau khi người dùng chạm vào ô Cài đặt nhanh.
 */
public class RebootAccessibilityService extends AccessibilityService {
    static volatile RebootAccessibilityService instance;

    private static final String[] LABELS = {
            "khởi động lại", "khởi động lại máy", "khởi động lại điện thoại",
            "restart", "reboot", "restart phone"
    };

    private long deadline = 0;
    private int clicks = 0;
    private long lastClick = 0;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        instance = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    /** Gọi từ ô Cài đặt nhanh. */
    void requestReboot() {
        deadline = SystemClock.elapsedRealtime() + 10000;
        clicks = 0;
        lastClick = 0;
        performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        long now = SystemClock.elapsedRealtime();
        if (now > deadline || clicks >= 2 || now - lastClick < 800) return;

        List<AccessibilityWindowInfo> windows = getWindows();
        if (windows == null) return;
        for (AccessibilityWindowInfo w : windows) {
            AccessibilityNodeInfo root = w.getRoot();
            if (root == null) continue;
            if (getPackageName().equals(String.valueOf(root.getPackageName()))) continue;
            if (clickMatch(root)) {
                lastClick = now;
                clicks++;
                return;
            }
        }
    }

    private boolean clickMatch(AccessibilityNodeInfo node) {
        if (node == null) return false;
        if (matches(node.getText()) || matches(node.getContentDescription())) {
            AccessibilityNodeInfo c = node;
            while (c != null && !c.isClickable()) c = c.getParent();
            if (c != null && c.performAction(AccessibilityNodeInfo.ACTION_CLICK)) return true;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            if (clickMatch(node.getChild(i))) return true;
        }
        return false;
    }

    private static boolean matches(CharSequence s) {
        if (s == null) return false;
        String t = s.toString().trim().toLowerCase(Locale.ROOT);
        for (String l : LABELS) {
            if (t.equals(l)) return true;
        }
        return false;
    }

    @Override
    public void onInterrupt() {}
}
