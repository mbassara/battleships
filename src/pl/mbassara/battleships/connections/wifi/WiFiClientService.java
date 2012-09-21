package pl.mbassara.battleships.connections.wifi;

import java.io.IOException;
import java.net.Socket;

import pl.mbassara.battleships.Global;
import android.content.Context;

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
			socket = new Socket(serverIP, Global.WIFI_PORT);
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
