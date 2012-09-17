package pl.mbassara.battleships.activities;

import java.util.ArrayList;

import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.connections.bluetooth.BluetoothClientService;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothClientActivity extends BluetoothActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);

		adapter = BluetoothAdapter.getDefaultAdapter();
		
		if(adapter == null)
			Toast.makeText(getApplicationContext(), getString(R.string.bt_not_supported), Toast.LENGTH_SHORT).show();
		
		if(!adapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
    	
    	arrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_device);
    	listView = (ListView) findViewById(R.id.listView1);
    	
    	listView.setAdapter(arrayAdapter);
    	
    	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	registerReceiver(receiver, filter);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
//    	Toast.makeText(this, getString(R.string.bluetooth_help_finding_OFF), Toast.LENGTH_LONG).show();
    }
    
    @Override
    protected void onDestroy() {
    	unregisterReceiver(receiver);
    	super.onDestroy();
    }

    public void discoveryDevicesON(View view) {
    	adapter.startDiscovery();
    	Toast.makeText(this, getString(R.string.bluetooth_help_finding_ON), Toast.LENGTH_LONG).show();
    }

    public void discoveryDevicesOFF(View view) {
    	adapter.cancelDiscovery();
    	Toast.makeText(this, getString(R.string.bluetooth_help_finding_OFF), Toast.LENGTH_LONG).show();
    }
    
    public void bluetoothDeviceClicked(View view) {
    	String deviceName = ((TextView) view).getText().toString();
    	deviceName = deviceName.substring(deviceName.indexOf("\n") + 1);
    	if(Constants.LOGS_ENABLED) System.out.println("connecting to " + deviceName);
    	adapter.cancelDiscovery();
    	
    	BluetoothDevice hostDevice = null;
    	for(BluetoothDevice device : devices)
    		if(device.getAddress().equals(deviceName))
    			hostDevice = device;
    	
    	if(hostDevice != null)
    		connect(new BluetoothClientService(hostDevice));
    }

	private BluetoothAdapter adapter;
	private final int REQUEST_ENABLE_BT = 1;
	private ListView listView;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			
			if(action.equals(BluetoothDevice.ACTION_FOUND)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				arrayAdapter.add(device.getName() + "\n" + device.getAddress());
				devices.add(device);
			}
			
		}
	};

	@Override
	public String getSpecificInfoString() {
		return getString(R.string.connecting_to_host);
	}
}
