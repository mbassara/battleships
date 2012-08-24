package pl.mbassara.battleships.connections;


public interface RemoteService {

	
	public void connect();
	
	public void stop();
	
	public boolean isConnected();
	
	public void send(GamePacket gamePacket);

	public GamePacket receive();
}
