package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class GameModeActivity extends Activity {
	
	private static String mode = Global.GAME_MODE_HOST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);
    }
    
    public static String getMode() {
		return mode;
	}
    
    public void gameModeClient(View view) {
    	mode = Global.GAME_MODE_CLIENT;
    	Intent intent = null;
    	if(Global.GAME_MODE == Global.GAME_MODE_BT)
    		intent = new Intent(this, BluetoothClientActivity.class);
    	else if(Global.GAME_MODE == Global.GAME_MODE_WIFI)
    		intent = new Intent(this, WiFiClientActivity.class);
    	
    	if(intent != null)
    		startActivity(intent);
    }
    
    public void gameModeHost(View view) {
    	mode = Global.GAME_MODE_HOST;
    	Intent intent = null;
    	if(Global.GAME_MODE == Global.GAME_MODE_BT)
    		intent = new Intent(this, BluetoothHostActivity.class);
    	else if(Global.GAME_MODE == Global.GAME_MODE_WIFI)
    		intent = new Intent(this, WiFiHostActivity.class);
    	
    	if(intent != null)
    		startActivity(intent);
    }
    
}
