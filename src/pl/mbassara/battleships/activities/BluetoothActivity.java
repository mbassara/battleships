package pl.mbassara.battleships.activities;

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
		bluetoothService = service;
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", getSpecificInfoString(), true);
		final Intent intent = new Intent(this, CreatingShipsActivity.class);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				bluetoothService.connect();
	    		
				while(!bluetoothService.isConnected())
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
				progressDialog.dismiss();
				startActivity(intent);
			}
		});
		
		thread.start();
    }

    public static BluetoothService getBluetoothService() {
		return bluetoothService;
	}
}
