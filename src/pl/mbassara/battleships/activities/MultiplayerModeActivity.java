package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MultiplayerModeActivity extends Activity {
	
	public static final String WIFI_MODE = "Wifi mode";
	public static final String BT_MODE = "Bluetooth mode";

	private static String mode = WIFI_MODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_mode);
    }

    public void chooseMode(View view) {
    	Intent intent = new Intent(this, GameModeActivity.class);
    	
    	if(view.equals(findViewById(R.id.WiFiModeButton)))
    		mode = WIFI_MODE;
    	else if(view.equals(findViewById(R.id.bluetoothModeButton)))
    		mode = BT_MODE;
    	
    	if(Constants.LOGS_ENABLED) System.out.println("Multiplayer mode: " + mode);
    	startActivity(intent);
    }
    
    public static String getMode() {
		return mode;
	}

}
