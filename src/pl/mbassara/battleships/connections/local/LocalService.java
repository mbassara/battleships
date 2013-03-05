package pl.mbassara.battleships.connections.local;

import java.util.LinkedList;

import android.content.Context;

import pl.mbassara.battleships.AIComputer;
import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.connections.GamePacket;
import pl.mbassara.battleships.connections.RemoteService;
import pl.mbassara.battleships.enums.GamePacketType;
import pl.mbassara.battleships.enums.GameResult;
import pl.mbassara.battleships.enums.WhoStarts;

public class LocalService implements RemoteService {
	
	private boolean isConnected = false;
	private final LinkedList<GamePacket> fromCPUtoPlayerQueue = new LinkedList<GamePacket>();
	private final LinkedList<GamePacket> fromPlayerToCPUQueue = new LinkedList<GamePacket>();
	private AIComputer aiComputer;
	
	public LocalService(Context context) {
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
					if(!isConnected)
						continue;
	
					if(gamePacket.getType() == GamePacketType.WHO_STARTS) {
						if(gamePacket.getWhoStarts() == WhoStarts.CLIENT_STARTS)
							fromCPUtoPlayerQueue.offer(aiComputer.doShot());
					}
					else if(gamePacket.getType() == GamePacketType.SHOT) {
						Coordinates field = gamePacket.getCoordinates();
						ShotResult result = aiComputer.receiveShot(field.getX(), field.getY());
						fromCPUtoPlayerQueue.offer(new GamePacket(result));
	
						if(result.isGameEnded())
							fromCPUtoPlayerQueue.offer(new GamePacket(GameResult.WINNER));
						else if((!result.isHit() || result.isSunk()) && !result.isGameEnded())
							fromCPUtoPlayerQueue.offer(aiComputer.doShot());
					}
					else if(gamePacket.getType() == GamePacketType.RESULT) {
						aiComputer.receiveResult(gamePacket.getShotResult());
						if(gamePacket.getShotResult().isHit() && !gamePacket.getShotResult().isSunk())
							fromCPUtoPlayerQueue.offer(aiComputer.doShot());
					}
					else if(gamePacket.getType() == GamePacketType.GAME_RESULT) {
						isConnected = false;
					}
				}
				if(Global.getInstance().LOGS_ENABLED)
					System.out.println("Local service's receiving thread finished.");
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
