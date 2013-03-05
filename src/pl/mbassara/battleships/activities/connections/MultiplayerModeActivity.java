package pl.mbassara.battleships.activities.connections;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.enums.GameMode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MultiplayerModeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_multiplayer_mode);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	this.finish();
    }

    public void chooseMode(View view) {
    	Intent intent = new Intent(this, GameModeActivity.class);

    	Global global = Global.getInstance();
    	if(view.equals(findViewById(R.id.WiFiModeButton)))
    		global.GAME_MODE = GameMode.WIFI;
    	else if(view.equals(findViewById(R.id.bluetoothModeButton)))
    		global.GAME_MODE = GameMode.BLUETOOTH;
    	
    	if(global.LOGS_ENABLED) System.out.println("Multiplayer mode: " + global.GAME_MODE);
    	startActivity(intent);
    }

}
