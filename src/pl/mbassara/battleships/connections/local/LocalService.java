package pl.mbassara.battleships.connections.local;

import java.util.LinkedList;

import pl.mbassara.battleships.AIComputer;
import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.GameResult;
import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.connections.GamePacket;
import pl.mbassara.battleships.connections.RemoteService;

public class LocalService implements RemoteService {
	
	private boolean isConnected = false;
	private final LinkedList<GamePacket> fromCPUtoPlayerQueue = new LinkedList<GamePacket>();
	private final LinkedList<GamePacket> fromPlayerToCPUQueue = new LinkedList<GamePacket>();
	private AIComputer aiComputer;
	
	public LocalService() {
		aiComputer = new AIComputer();
	}

	public void connect() {
		isConnected = true;
		
		Thread aiComputerThread = new Thread(new Runnable() {
			
			public void run() {
				
				while(isConnected) {
					while(isConnected && fromPlayerToCPUQueue.isEmpty()) {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					GamePacket gamePacket = fromPlayerToCPUQueue.poll();
	
					if(gamePacket.getType() == GamePacket.TYPE_WHO_STARTS) {
						if(gamePacket.getWhoStarts() == Global.CLIENT_FIRST)
							fromCPUtoPlayerQueue.offer(aiComputer.doShot());
					}
					else if(gamePacket.getType() == GamePacket.TYPE_SHOT) {
						Coordinates field = gamePacket.getCoordinates();
						ShotResult result = aiComputer.receiveShot(field.getX(), field.getY());
						fromCPUtoPlayerQueue.offer(new GamePacket(result));
	
						if(result.isGameEnded())
							fromCPUtoPlayerQueue.offer(new GamePacket(new GameResult(Global.GAME_RESULT_WINNER)));
						else if((!result.isHit() || result.isSunk()) && !result.isGameEnded())
							fromCPUtoPlayerQueue.offer(aiComputer.doShot());
					}
					else if(gamePacket.getType() == GamePacket.TYPE_RESULT) {
						aiComputer.receiveResult(gamePacket.getShotResult());
						if(gamePacket.getShotResult().isHit())
							fromCPUtoPlayerQueue.offer(aiComputer.doShot());
					}
					else if(gamePacket.getType() == GamePacket.TYPE_GAME_RESULT) {
						isConnected = false;
					}
				}
			}
		});
		
		aiComputerThread.start();
	}

	public void stop() {
		isConnected = false;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void send(GamePacket gamePacket) {
		if(!isConnected)
			return;
		
		fromPlayerToCPUQueue.offer(gamePacket);
	}

	public GamePacket receive() {
		if(!isConnected || fromCPUtoPlayerQueue.isEmpty())
			return null;
		
		return fromCPUtoPlayerQueue.poll();
	}

}
