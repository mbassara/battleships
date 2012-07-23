package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Log;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.bluetooth.BluetoothService;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public abstract class BluetoothActivity extends Activity {
	
	private static BluetoothService bluetoothService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract String getSpecificInfoString();
    
    protected void connect(BluetoothService service) {
		if(Log.enabled) System.out.println("BluetoothActivity.connect()");
		bluetoothService = service;
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", getSpecificInfoString(), true);
		final Intent intent = new Intent(this, CreatingShipsActivity.class);
		
		final BluetoothActivity activity = this;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				bluetoothService.connect();
	    		
				int trialsCounter = 0;
				int COUNTER_MAX = 100;
				while(!bluetoothService.isConnected() && trialsCounter < COUNTER_MAX) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					trialsCounter++;
				}
				
				progressDialog.dismiss();
				if(trialsCounter < COUNTER_MAX)
					startActivity(intent);
				else {
					bluetoothService.stop();
					System.out.println(getString(R.string.connection_timeout));
					activity.finish();
				}
			}
		});
		
		thread.start();
    }

    public static BluetoothService getBluetoothService() {
		return bluetoothService;
	}
}
