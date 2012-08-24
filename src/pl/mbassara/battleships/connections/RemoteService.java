package pl.mbassara.battleships.connections;

import pl.mbassara.battleships.connections.bluetooth.GamePacket;

public interface RemoteService {

	
	public void connect();
	
	public void stop();
	
	public boolean isConnected();
	
	public void send(GamePacket gamePacket);

	public GamePacket receive();
}
