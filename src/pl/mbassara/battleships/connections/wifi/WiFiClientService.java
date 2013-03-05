package pl.mbassara.battleships.connections.wifi;

import java.io.IOException;
import java.net.Socket;

public class WiFiClientService extends WiFiService {
	
	private String serverIP;
	
	public WiFiClientService(String serverIP) {
		super();
		this.serverIP = serverIP;
	}

	@Override
	public Socket connectSpecific() {
		if(serverIP == null)
			return null;
		
		Socket socket = null;
		try {
			socket = new Socket(serverIP, global.WIFI_PORT);
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
