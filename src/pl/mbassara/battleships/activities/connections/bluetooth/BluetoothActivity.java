package pl.mbassara.battleships.activities.connections.bluetooth;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.activities.CreatingShipsActivity;
import pl.mbassara.battleships.connections.bluetooth.BluetoothService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public abstract class BluetoothActivity extends Activity
	implements DialogInterface.OnCancelListener {
	
	private static BluetoothService bluetoothService;
	private boolean isActive = true;
	protected Global global = Global.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	this.finish();
    }

    protected abstract String getSpecificInfoString();
    
    protected void connect(BluetoothService service) {
		if(global.LOGS_ENABLED) System.out.println("BluetoothActivity.connect()");
		bluetoothService = service;
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", getSpecificInfoString(), true, true, this);
		final Intent intent = new Intent(this, CreatingShipsActivity.class);
		
		final BluetoothActivity activity = this;
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				bluetoothService.connect();
	    		
				int trialsCounter = 0;
				int COUNTER_MAX = 50;
				while(!bluetoothService.isConnected() && trialsCounter < COUNTER_MAX && isActive) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					trialsCounter++;
				}
				
				progressDialog.dismiss();
				if(trialsCounter < COUNTER_MAX && isActive){
					global.remoteService = bluetoothService;
					startActivity(intent);
				}
				else if(isActive){
					bluetoothService.stop();
					System.out.println(getString(R.string.connection_timeout));
					activity.finish();
				}
			}
		});
		
		thread.start();
    }

    public void onCancel(DialogInterface dialog) {
		bluetoothService.stop();
		isActive = false;
		System.out.println("connection canceled");
		this.finish();
    }
}
