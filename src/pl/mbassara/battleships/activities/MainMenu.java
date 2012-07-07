package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

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
    
    public void newGame(View view) {
    	Intent intent = new Intent(this, GameActivity.class);
    	startActivity(intent);
    	if(D) System.out.println("newGame button clicked");
    }
    
    public void bluetooth(View view) {
    	if(D) System.out.println("bluetooth button clicked 1");
    	Intent intent = new Intent(this, BluetoothActivity.class);
    	if(D) System.out.println("bluetooth button clicked 2");
    	startActivity(intent);
    	if(D) System.out.println("bluetooth button clicked 3");
    }
    
}
