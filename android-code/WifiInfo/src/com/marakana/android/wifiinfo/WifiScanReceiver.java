
package com.marakana.android.wifiinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class WifiScanReceiver extends BroadcastReceiver {
    WifiManager wifimanager;

    @Override
    public void onReceive(Context context, Intent intent) {
        wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> results = wifimanager.getScanResults();
        for (ScanResult result : results) {
            Log.d("WifiScanReceiver",
                    String.format("\n%s (%s) %dMHz %ddBm", result.SSID, result.capabilities,
                            result.frequency, result.level));
        }
    }

}
