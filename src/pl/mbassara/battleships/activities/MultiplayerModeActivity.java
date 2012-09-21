package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MultiplayerModeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_mode);
    }

    public void chooseMode(View view) {
    	Intent intent = new Intent(this, GameModeActivity.class);
    	
    	if(view.equals(findViewById(R.id.WiFiModeButton)))
    		Global.GAME_MODE = Global.GAME_MODE_WIFI;
    	else if(view.equals(findViewById(R.id.bluetoothModeButton)))
    		Global.GAME_MODE = Global.GAME_MODE_BT;
    	
    	if(Global.LOGS_ENABLED) System.out.println("Multiplayer mode: " + Global.GAME_MODE);
    	startActivity(intent);
    }

}
