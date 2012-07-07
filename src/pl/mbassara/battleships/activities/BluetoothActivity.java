package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.BluetoothService;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.R.layout;
import pl.mbassara.battleships.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class BluetoothActivity extends Activity {
	
	private BluetoothService btService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if(MainMenu.D) System.out.println("BluetoothActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

    	btService = new BluetoothService();
    	if(!btService.setUp(this))
    		Toast.makeText(getApplicationContext(), getString(R.string.bt_not_supported), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onStart() {
    	if(MainMenu.D) System.out.println("BluetoothActivity onStart");
    	
    	ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_devices);
    	ListView listView = (ListView) findViewById(R.id.listView1);
    	
    	listView.setAdapter(arrayAdapter);

    	for(BluetoothDevice device : btService.getDevices())
    		arrayAdapter.add(device.toString());

    	arrayAdapter.add("siemka!");
    	
    	super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bluetooth, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == 1)
    		System.out.println("activity result code: " + requestCode);
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
}
