package pl.mbassara.battleships.activities;

import java.util.ArrayList;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.bluetooth.BluetoothClientService;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
    		final BluetoothClientService bluetooth = new BluetoothClientService(hostDevice, this);
    		bluetooth.connect();
    		
			final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.connecting_to_host), true);
			final Intent intent = new Intent(this, CreatingShipsActivity.class);
    		
    		Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(!bluetooth.isConnected())
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
}
