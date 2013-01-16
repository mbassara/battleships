package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.activities.connections.MultiplayerModeActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenu extends Activity {
	
	private AlertDialog aboutDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\n" + getString(R.string.games_name) + 
				"\n\n" + getString(R.string.author) + "\n" + getString(R.string.authors_name) +  
				"\n\n" + getString(R.string.german_translator) + "\n" + getString(R.string.german_translators_name))
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
        
        aboutDialog = builder.create();
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
    	
		case R.id.menu_item_about:
			if(aboutDialog != null)
				aboutDialog.show();
			return true;

		default:
	    	return super.onOptionsItemSelected(item);
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    public void multiplayer(View view) {
    	Intent intent = new Intent(this, MultiplayerModeActivity.class);
    	startActivity(intent);
    }
    
    public void singleplayer(View view) {
    	Intent intent = new Intent(this, CreatingShipsActivity.class);
    	Global.GAME_MODE = Global.GAME_MODE_SINGLE;
    	startActivity(intent);
    }
    
    public void options(View view) {
    	Intent intent = new Intent(this, OptionsActivity.class);
    	startActivity(intent);
    }
    
}
