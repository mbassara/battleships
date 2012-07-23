package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class GameModeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game_mode, menu);
        return true;
    }
    
    public void gameModeClient(View view) {
    	Intent intent = new Intent(this, BluetoothClientActivity.class);
    	startActivity(intent);
    }
    
    public void gameModeHost(View view) {
    	Intent intent = new Intent(this, BluetoothHostActivity.class);
    	startActivity(intent);
    }
    
}
