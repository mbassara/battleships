package pl.mbassara.battleships;

import java.util.Set;

import pl.mbassara.battleships.activities.MainMenu;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothService {
	
	private BluetoothAdapter adapter;
	private final int REQUEST_ENABLE_BT = 1;
	
	public boolean setUp(Activity parentActivity) {
		adapter = BluetoothAdapter.getDefaultAdapter();
    	if(MainMenu.D) System.out.println("BluetoothService setUp 1");
		
		if(adapter == null)
			return false;
    	if(MainMenu.D) System.out.println("BluetoothService setUp 2");
		
		if(!adapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			parentActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		return true;
	}
	
	public Set<BluetoothDevice> getDevices() {
		if(adapter != null)
			return adapter.getBondedDevices();
		else return null;
	}
	
}
