package pl.mbassara.battleships.bluetooth;

import java.io.IOException;

import pl.mbassara.battleships.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;

public class BluetoothHostService extends BluetoothService {
	
	BluetoothAdapter adapter;

	public BluetoothHostService(BluetoothAdapter adapter, Context context) {
		super(context);
		this.adapter = adapter;
	}

	@Override
	public boolean connectSpecific() {
		if(Log.enabled) System.out.println("BluetoothHostService.connectSpecific()");
		if(adapter == null)
			return false;
		
		try {
			BluetoothServerSocket serverSocket = adapter.listenUsingRfcommWithServiceRecord("Battleships Game", java.util.UUID.fromString(UUID));
			socket = serverSocket.accept();
			serverSocket.close();
			
			if(socket != null)
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}