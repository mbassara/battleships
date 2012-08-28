package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.connections.wifi.WiFiClientService;
import pl.mbassara.battleships.connections.wifi.WiFiService;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WiFiClientActivity extends WiFiActivity {
	
	EditText ipEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_client);
        
        ipEditText = (EditText) findViewById(R.id.HostIPAdressTextView);
        
        String IP = WiFiService.getHostAdress(this);
        IP = IP.substring(0, IP.lastIndexOf(".") + 1);
        
        ipEditText.setText(IP);
        
    }

    public void connectToServer(View view) {
		
		connect(new WiFiClientService(this, ipEditText.getText().toString()));
    }

	@Override
	public String getSpecificInfoString() {
		return getString(R.string.connecting_to_host);
	}
    
}
