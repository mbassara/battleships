package pl.mbassara.battleships.connections.wifi;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import pl.mbassara.battleships.connections.GamePacket;
import pl.mbassara.battleships.connections.RemoteService;

public abstract class WiFiService implements RemoteService {
	
	public static String getHostAdress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		int ip = info.getIpAddress();
		@SuppressWarnings("deprecation")
		String ipText = Formatter.formatIpAddress(ip);
		
		return ipText;
	}

	public void connect() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	public void send(GamePacket gamePacket) {
		// TODO Auto-generated method stub

	}

	public GamePacket receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
