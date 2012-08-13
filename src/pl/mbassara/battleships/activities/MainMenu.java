package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainMenu extends Activity {
	
	public static final boolean D = false; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    public void multiplayer(View view) {
    	Intent intent = new Intent(this, GameModeActivity.class);
    	startActivity(intent);
    }
    
    public void singleplayer(View view) {
    	Intent intent = new Intent(this, CreatingShipsActivity.class);
    	intent.putExtra(Constants.GAME_TYPE, Constants.SINGLEPLAYER);
    	startActivity(intent);
    }
    
    public void options(View view) {
    	Toast.makeText(this, getString(R.string.not_available_yet), Toast.LENGTH_SHORT).show();
    }
    
}
