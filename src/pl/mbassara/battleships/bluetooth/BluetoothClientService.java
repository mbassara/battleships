package pl.mbassara.battleships.bluetooth;

import java.io.IOException;

import pl.mbassara.battleships.Log;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class BluetoothClientService extends BluetoothService {

	public BluetoothClientService(BluetoothDevice device, Context context) {
		super(context);
		try {
			socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean connectSpecific() {
		if(Log.enabled) System.out.println("BluetoothClientService.connectSpecific()");
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
