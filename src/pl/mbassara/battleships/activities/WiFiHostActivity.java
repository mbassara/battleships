package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.connections.wifi.WiFiHostService;
import pl.mbassara.battleships.connections.wifi.WiFiService;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WiFiHostActivity extends WiFiActivity {
	
	TextView ipTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_host);
        
        ipTextView = (TextView) findViewById(R.id.HostIPAdressTextView);
        ipTextView.setText(WiFiService.getHostAdress(this));
        
    }

    public void start(View view) {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		
		if(!wifiManager.isWifiEnabled()) {
			wifiManager.reconnect();
		}
		
		connect(new WiFiHostService(this));
    }

	@Override
	public String getSpecificInfoString() {
		return getString(R.string.waiting_for_client);
	}
    
}
