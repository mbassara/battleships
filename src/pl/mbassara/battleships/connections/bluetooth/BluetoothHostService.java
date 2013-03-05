package pl.mbassara.battleships.connections.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

public class BluetoothHostService extends BluetoothService {
	
	private BluetoothAdapter adapter;
	private BluetoothServerSocket serverSocket = null;

	public BluetoothHostService(BluetoothAdapter adapter) {
		super();
		this.adapter = adapter;
	}

	@Override
	public boolean connectSpecific() {
		if(global.LOGS_ENABLED) System.out.println("BluetoothHostService.connectSpecific()");
		if(adapter == null)
			return false;
		
		try {
			serverSocket = adapter.listenUsingRfcommWithServiceRecord("Battleships Game", java.util.UUID.fromString(UUID));
			socket = serverSocket.accept();
			serverSocket.close();
			
			if(socket != null)
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public void cancelSpecific() {
		try {
			if(serverSocket != null)
				serverSocket.close();
			if(socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
