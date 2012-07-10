package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.bluetooth.BluetoothHostService;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
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
//    	Intent intent = new Intent(this, BluetoothHostActivity.class);
//    	startActivity(intent);
    	
    	BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			startActivity(discoverableIntent);
		}
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.waiting_for_client), true);
		final Intent intent = new Intent(this, CreatingShipsActivity.class);
		
		
		final BluetoothHostService bluetooth = new BluetoothHostService(adapter, this);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				bluetooth.connect();

				while(!bluetooth.isConnected()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				progressDialog.dismiss();

				startActivity(intent);
			}
		});
		
		thread.start();
    }
    
}
