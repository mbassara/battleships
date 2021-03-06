package pl.mbassara.battleships.activities.connections;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.activities.connections.bluetooth.BluetoothClientActivity;
import pl.mbassara.battleships.activities.connections.bluetooth.BluetoothHostActivity;
import pl.mbassara.battleships.activities.connections.wifi.WiFiClientActivity;
import pl.mbassara.battleships.activities.connections.wifi.WiFiHostActivity;
import pl.mbassara.battleships.enums.GameMode;
import pl.mbassara.battleships.enums.MultiplayerGameMode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class GameModeActivity extends Activity {

	private Global global = Global.getInstance();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_mode);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	this.finish();
    }
    
    public void gameModeClient(View view) {
    	global.MULTIPLAYER_GAME_MODE = MultiplayerGameMode.CLIENT;
    	Intent intent = null;
    	if(global.GAME_MODE == GameMode.BLUETOOTH)
    		intent = new Intent(this, BluetoothClientActivity.class);
    	else if(global.GAME_MODE == GameMode.WIFI)
    		intent = new Intent(this, WiFiClientActivity.class);
    	
    	if(intent != null)
    		startActivity(intent);
    }
    
    public void gameModeHost(View view) {
    	global.MULTIPLAYER_GAME_MODE = MultiplayerGameMode.HOST;
    	Intent intent = null;
    	if(global.GAME_MODE == GameMode.BLUETOOTH)
    		intent = new Intent(this, BluetoothHostActivity.class);
    	else if(global.GAME_MODE == GameMode.WIFI)
    		intent = new Intent(this, WiFiHostActivity.class);
    	
    	if(intent != null)
    		startActivity(intent);
    }
    
}
