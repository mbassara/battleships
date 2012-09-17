package pl.mbassara.battleships.connections;

import java.io.Serializable;

import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.GameResult;
import pl.mbassara.battleships.ShotResult;

public class GamePacket implements Serializable{
	public static final int TYPE_WHO_STARTS = 0;
	public static final int TYPE_SHOT = 1;
	public static final int TYPE_RESULT = 2;
	public static final int TYPE_TEXT_MESSAGE = 3;
	public static final int TYPE_GAME_RESULT = 4;
	public static final int TYPE_USER_NAME = 5;
	public static final int TYPE_PACKET_BROKEN = 6;
	
	private static final long serialVersionUID = 5504703199854898469L;
	private int type;
	private String message = null;
	private String userName = null;
	private Coordinates coordinates = null;
	private boolean whoStarts = Constants.HOST_FIRST;
	private ShotResult shotResult = null;
	private GameResult gameResult = null;
	
	public GamePacket(int type, Object value) {
		this.type = type;
		switch (type) {
			case TYPE_WHO_STARTS:
				this.whoStarts = (Boolean) value;
				break;
			case TYPE_SHOT:
				this.coordinates = (Coordinates) value;
				break;
			case TYPE_RESULT:
				this.shotResult = (ShotResult) value;
				break;
			case TYPE_TEXT_MESSAGE:
				this.message = (String) value;
				break;
			case TYPE_GAME_RESULT:
				this.gameResult = (GameResult) value;
				break;
			case TYPE_USER_NAME:
				this.userName = (String) value;
				break;
	
			default:
				this.type = TYPE_PACKET_BROKEN;
				break;
		}
	}
	
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
	
	public String getUserName() {
		return userName;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
}
