package pl.mbassara.battleships.activities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothClientActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        UUID = getString(R.string.UUID);
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
    	if(MainMenu.D) System.out.println("BluetoothActivity onStart");

//    	for(BluetoothDevice device : btService.getDevices())
//    		arrayAdapter.add(device.toString());

    	super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bluetooth, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == REQUEST_ENABLE_BT)
    		System.out.println("activity result code: " + requestCode);
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onDestroy() {
    	unregisterReceiver(receiver);
    	super.onDestroy();
    }

    public void discoveryDevicesON(View view) {
    	adapter.startDiscovery();
    }

    public void discoveryDevicesOFF(View view) {
    	adapter.cancelDiscovery();
    }
    
    public void bluetoothDeviceClicked(View view) {
    	String adres = ((TextView) view).getText().toString();
    	adres = adres.substring(adres.indexOf("\n") + 1);
    	System.out.println("connecting to " + adres);
    	adapter.cancelDiscovery();
    	
    	BluetoothDevice hostDevice = null;
    	for(BluetoothDevice device : devices)
    		if(device.getAddress().equals(adres))
    			hostDevice = device;
    	
    	if(hostDevice != null) {
    		try {
				BluetoothSocket socket = hostDevice.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
				socket.connect();
				
				Thread.sleep(2000);
				BufferedWriter writer = new BufferedWriter(
											new OutputStreamWriter(
												socket.getOutputStream()));
				writer.write("pzdr od klienta!");
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

	private String UUID;
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
}
