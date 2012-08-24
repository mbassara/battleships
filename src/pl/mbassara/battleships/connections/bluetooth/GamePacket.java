package pl.mbassara.battleships.connections.bluetooth;

import java.io.Serializable;

import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.GameResult;
import pl.mbassara.battleships.ShotResult;

public class GamePacket implements Serializable{
	public static final boolean HOST_FIRST = false;
	public static final boolean CLIENT_FIRST = true;
	public static final int TYPE_WHO_STARTS = 0;
	public static final int TYPE_SHOT = 1;
	public static final int TYPE_RESULT = 2;
	public static final int TYPE_TEXT_MESSAGE = 3;
	public static final int TYPE_GAME_RESULT = 4;
	
	private static final long serialVersionUID = 5504703199854898469L;
	private int type;
	private String message = null;
	private Coordinates coordinates = null;
	private boolean whoStarts = HOST_FIRST;
	private ShotResult shotResult = null;
	private GameResult gameResult = null;
	
	public GamePacket(boolean whoStarts) {
		this.whoStarts = whoStarts;
		type = TYPE_WHO_STARTS;
	}
	
	public GamePacket(ShotResult result) {
		this.shotResult = result;
		type = TYPE_RESULT;
	}
	
	public GamePacket(GameResult result) {
		this.gameResult = result;
		type = TYPE_GAME_RESULT;
	}
	
	public GamePacket(Coordinates coordinates) {
		this.coordinates = coordinates;
		type = TYPE_SHOT;
	}
	
	public GamePacket(int x, int y) {
		coordinates = new Coordinates(x, y);
		type = TYPE_SHOT;
	}
	
	public GamePacket(String message) {
		this.message = message;
		type = TYPE_TEXT_MESSAGE;
	}

	public int getType() {
		return type;
	}
	
	public boolean getWhoStarts() {
		return whoStarts;
	}
	
	public String getMessage() {
		return message;
	}
	
	public ShotResult getShotResult() {
		return shotResult;
	}
	
	public GameResult getGameResult() {
		return gameResult;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
}
