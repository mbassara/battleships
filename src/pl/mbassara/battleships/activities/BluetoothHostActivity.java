package pl.mbassara.battleships.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import pl.mbassara.battleships.R;
import pl.mbassara.battleships.R.layout;
import pl.mbassara.battleships.R.menu;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class BluetoothHostActivity extends Activity {
	
	private String UUID;
	private BluetoothServerSocket serverSocket;
	private BluetoothAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_host);
        UUID = getString(R.string.UUID);
        
        adapter = BluetoothAdapter.getDefaultAdapter();
        try {
			serverSocket = adapter.listenUsingRfcommWithServiceRecord("Battleships Game", java.util.UUID.fromString(UUID));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
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
    
    public void listenForClient(View view) {
		try {
			System.out.println(1);
			BluetoothSocket socket = serverSocket.accept();
			System.out.println(2);
			serverSocket.close();
			System.out.println(3);

			System.out.println(4);
			BufferedReader reader = new BufferedReader(
										new InputStreamReader(
											socket.getInputStream()));
			System.out.println(5);
			String str = reader.readLine();
			((Button) findViewById(R.id.listen_button)).setText(str);
			System.out.println(6);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
