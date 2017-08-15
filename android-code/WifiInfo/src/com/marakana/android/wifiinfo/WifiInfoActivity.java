
package com.marakana.android.wifiinfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class WifiInfoActivity extends Activity implements OnClickListener {
    TextView out;
    Button buttonRefresh;
    WifiManager wifimanager;
    BroadcastReceiver receiver;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize UI
        setContentView(R.layout.main);
        out = (TextView) findViewById(R.id.out);
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(this);

        // Get WifiManager
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
        onClick(null);
    }

    static final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

    @Override
    protected void onStart() {
        super.onStart();
        if (receiver == null)
            receiver = new ScanCompleteReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public void onClick(View v) {
        out.setText("");

        out.append("\n\nConfigured Networks:");
        // Get IP Address
        int ipAddress = wifimanager.getConnectionInfo().getIpAddress();
        out.append("\nThe ip address is "
                + Formatter.formatIpAddress(ipAddress));

        // Get configured networks
        List<WifiConfiguration> configuredNetworks = wifimanager.getConfiguredNetworks();
        for (WifiConfiguration conf : configuredNetworks) {
            out.append(String.format("\n%s", conf.SSID));
        }

        wifimanager.startScan();
    }

    // Scan Complete Broadcast Receiver
    class ScanCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            out.append("\n\nScan Results:");
            List<ScanResult> results = wifimanager.getScanResults();
            for (ScanResult result : results) {
                out.append(String.format("\n%s (%s) %dMHz %ddBm", result.SSID, result.capabilities,
                        result.frequency, result.level));
            }
        }

    }
}
