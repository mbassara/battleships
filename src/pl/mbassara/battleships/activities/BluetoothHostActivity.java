package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.bluetooth.BluetoothHostService;
import pl.mbassara.battleships.bluetooth.GamePacket;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class BluetoothHostActivity extends Activity {
	
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
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.waiting_for_client), true);
		final Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
		
		
		final BluetoothHostService bluetooth = new BluetoothHostService(adapter, this);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				bluetooth.connect();

				GamePacket reply = null;
				do {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					reply = bluetooth.receive();
				} while (reply == null);
				
				progressDialog.dismiss();

				toast.setText("Message from client: " + reply.getMessage());
				toast.show();
			}
		});
		
		thread.start();
		
    	super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bluetooth_host, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
//    public void listenForClient(View view) {
//		if(adapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//			startActivity(discoverableIntent);
//		}
//		
//		final BluetoothHostService bluetooth = new BluetoothHostService(adapter, this);
//		(new Runnable() {
//			@Override
//			public void run() {
//				bluetooth.connect();
//
//				GamePacket reply = null;
//				do {
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					reply = bluetooth.receive();
//				} while (reply == null);
//				Button button = (Button) findViewById(R.id.listen_button);
//				button.setText(reply.getMessage());
//			}
//		}).run();
//    }

}
