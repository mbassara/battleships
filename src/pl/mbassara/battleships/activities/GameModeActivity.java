package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.WiFiClientActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class GameModeActivity extends Activity {
	public static final int HOST_MODE = 0;
	public static final int CLIENT_MODE = 1;
	
	private static int mode = HOST_MODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);
    }
    
    public static int getMode() {
		return mode;
	}
    
    public void gameModeClient(View view) {
    	mode = CLIENT_MODE;
    	Intent intent = null;
    	if(MultiplayerModeActivity.getMode() == MultiplayerModeActivity.BT_MODE)
    		intent = new Intent(this, BluetoothClientActivity.class);
    	else if(MultiplayerModeActivity.getMode() == MultiplayerModeActivity.WIFI_MODE)
    		intent = new Intent(this, WiFiClientActivity.class);
    	
    	if(intent != null)
    		startActivity(intent);
    }
    
    public void gameModeHost(View view) {
    	mode = HOST_MODE;
    	Intent intent = null;
    	if(MultiplayerModeActivity.getMode() == MultiplayerModeActivity.BT_MODE)
    		intent = new Intent(this, BluetoothHostActivity.class);
    	else if(MultiplayerModeActivity.getMode() == MultiplayerModeActivity.WIFI_MODE)
    		intent = new Intent(this, WiFiHostActivity.class);
    	
    	if(intent != null)
    		startActivity(intent);
    }
    
}
