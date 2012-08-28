package pl.mbassara.battleships.connections.wifi;

import java.io.IOException;
import java.net.Socket;

import android.content.Context;

import pl.mbassara.battleships.Constants;

public class WiFiClientService extends WiFiService {
	
	private String serverIP;
	
	public WiFiClientService(Context context, String serverIP) {
		super(context);
		this.serverIP = serverIP;
	}

	@Override
	public Socket connectSpecific() {
		if(serverIP == null)
			return null;
		
		Socket socket = null;
		try {
			socket = new Socket(serverIP, Constants.wifi_port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return socket;
	}
	
	@Override
	public void cancelSpecific() {
		try {
			if(super.socket != null)
				super.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
