package pl.mbassara.battleships.connections.bluetooth;

import java.io.IOException;

import pl.mbassara.battleships.Global;

import android.bluetooth.BluetoothDevice;

public class BluetoothClientService extends BluetoothService {

	public BluetoothClientService(BluetoothDevice device) {
		super();
		try {
			socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean connectSpecific() {
		if(Global.LOGS_ENABLED) System.out.println("BluetoothClientService.connectSpecific()");
		if(socket == null)
			return false;
		
		try {
			socket.connect();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void cancelSpecific() {
		try {
			if(socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
