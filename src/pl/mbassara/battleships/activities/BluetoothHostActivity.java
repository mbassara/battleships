package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.bluetooth.BluetoothHostService;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class BluetoothHostActivity extends BluetoothActivity {
	
	private BluetoothAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_host);
        
        adapter = BluetoothAdapter.getDefaultAdapter();
        
		if(adapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			startActivity(discoverableIntent);
		}
		
    }
    
    @Override
    protected void onStart() {
		if(adapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			startActivity(discoverableIntent);
		}
		
		connect(new BluetoothHostService(adapter, this));
		
    	super.onStart();
    }

	@Override
	public String getSpecificInfoString() {
		return getString(R.string.waiting_for_client);
	}

}
