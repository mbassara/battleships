package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.connections.wifi.WiFiService;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class WiFiHostActivity extends Activity {
	
	TextView ipTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_host);
        
        ipTextView = (TextView) findViewById(R.id.HostIPAdressTextView);
        ipTextView.setText(WiFiService.getHostAdress(this));
        
    }

    public void start(View view) {
    	
    }

    
}
