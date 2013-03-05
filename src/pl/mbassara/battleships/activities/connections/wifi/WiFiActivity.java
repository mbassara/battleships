package pl.mbassara.battleships.activities.connections.wifi;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.activities.CreatingShipsActivity;
import pl.mbassara.battleships.connections.wifi.WiFiService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;

public abstract class WiFiActivity extends Activity
	implements DialogInterface.OnCancelListener {
	
	private static WiFiService wiFiService;
	private boolean isActive = true;
	protected Global global = Global.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if(!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
			Toast.makeText(this, R.string.wifi_turned_on, Toast.LENGTH_SHORT).show();
		}
		
	}
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	this.finish();
    }
	
	protected abstract String getSpecificInfoString();
	
	protected void connect(WiFiService service) {
		if(global.LOGS_ENABLED) System.out.println("WiFiActivity.connect()");
		wiFiService = service;
		
		final ProgressDialog dialog = ProgressDialog.show(this, "", getSpecificInfoString(), true, true, this);
		final Intent intent = new Intent(this, CreatingShipsActivity.class);
		
		final Activity thisActivity = this;
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				wiFiService.connect();
	    		
				int trialsCounter = 0;
				int COUNTER_MAX = 50;
				while(!wiFiService.isConnected() && trialsCounter < COUNTER_MAX && isActive) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					trialsCounter++;
				}
				
				dialog.dismiss();
				if(trialsCounter < COUNTER_MAX && isActive){
					global.remoteService = wiFiService;
					startActivity(intent);
				}
				else if(isActive){
					wiFiService.stop();
					System.out.println(getString(R.string.connection_timeout));
					thisActivity.finish();
				}
			}
		});
		
		thread.start();
	}
	
	public void onCancel(DialogInterface dialog) {
		if(wiFiService != null) wiFiService.stop();
		isActive = false;
		System.out.println("connection canceled");
		this.finish();
	}
	
}
