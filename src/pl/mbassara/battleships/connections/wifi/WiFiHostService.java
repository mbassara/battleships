package pl.mbassara.battleships.connections.wifi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;

import pl.mbassara.battleships.Constants;

public class WiFiHostService extends WiFiService {
	
	public WiFiHostService(Context parentContext) {
		super(parentContext);
	}

	@Override
	public Socket connectSpecific() {
		Socket socket = null;
		try {
			ServerSocket serverSocket = new ServerSocket(Constants.wifi_port);
			socket = serverSocket.accept();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}

}
