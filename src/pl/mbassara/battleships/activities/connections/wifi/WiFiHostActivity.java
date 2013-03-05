package pl.mbassara.battleships.activities.connections.wifi;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.connections.wifi.WiFiHostService;
import pl.mbassara.battleships.connections.wifi.WiFiService;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class WiFiHostActivity extends WiFiActivity {
	
	private TextView ipTextView;
	private Button startServerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wi_fi_host);
        
        ipTextView = (TextView) findViewById(R.id.HostIPAdressTextView);
        startServerButton = (Button) findViewById(R.id.WiFiStartServerButton);

    	setIP(WiFiService.getHostAdress(this));
		startServerButton.setEnabled(!ipTextView.getText().equals(getString(R.string.wifi_connecting)));
		
    }
    
    private void setIP(String ip) {
    	if(ipTextView == null)
    		return;
    	
    	if(ip.equals("0.0.0.0"))
    		ipTextView.setText(this.getString(R.string.wifi_connecting));
    	else {
    		startServerButton.setEnabled(true);
    		ipTextView.setText(ip);
    	}
    }

    public void start(View view) {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		
		if(!wifiManager.isWifiEnabled()) {
			wifiManager.reconnect();
		}
		
		connect(new WiFiHostService());
    }
    
    public void refreshIP(View view) {
    	setIP(WiFiService.getHostAdress(this));
    }

	@Override
	public String getSpecificInfoString() {
		return getString(R.string.waiting_for_client);
	}
    
}
