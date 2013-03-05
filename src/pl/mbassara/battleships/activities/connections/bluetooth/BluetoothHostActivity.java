package pl.mbassara.battleships.activities.connections.bluetooth;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.connections.bluetooth.BluetoothHostService;
import android.os.Bundle;
import android.view.Window;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class BluetoothHostActivity extends BluetoothActivity {
	
	private BluetoothAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bluetooth_host);
        
        adapter = BluetoothAdapter.getDefaultAdapter();
		
    }
    
    @Override
    protected void onStart() {
		if(adapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			startActivity(discoverableIntent);
		}
		
		connect(new BluetoothHostService(adapter));
		
    	super.onStart();
    }

	@Override
	public String getSpecificInfoString() {
		return getString(R.string.waiting_for_client);
	}

}
