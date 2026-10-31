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
        boolean ok = RebootHelper.isDeviceOwner(this);
        tile.setLabel("Khởi động lại");
        tile.setState(ok ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        if (Build.VERSION.SDK_INT >= 29) {
            tile.setSubtitle(ok ? "Chạm để reboot" : "Chưa cấp quyền");
        }
        tile.updateTile();
    }

    @Override
    public void onClick() {
        if (!RebootHelper.reboot(this)) {
            Toast.makeText(this, "Không reboot được: chưa cấp quyền Device Owner hoặc đang có cuộc gọi",
                    Toast.LENGTH_LONG).show();
        }
    }
}
