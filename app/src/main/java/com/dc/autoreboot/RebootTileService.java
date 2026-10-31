package com.dc.autoreboot;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

/** Ô "Khởi động lại" trong bảng kéo xuống. Chạm một lần là reboot. */
public class RebootTileService extends TileService {

    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        if (tile == null) return;
        boolean ok = RebootHelper.canReboot(this);
        tile.setLabel("Khởi động lại");
        tile.setState(ok ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        if (Build.VERSION.SDK_INT >= 29) {
            tile.setSubtitle(ok ? "Chạm để reboot" : "Cần bật Trợ năng");
        }
        tile.updateTile();
    }

    @Override
    public void onClick() {
        if (!RebootHelper.reboot(this)) {
            Toast.makeText(this,
                    "Chưa bật dịch vụ Trợ năng \"Auto Reboot\". Mở app Auto Reboot để bật.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
