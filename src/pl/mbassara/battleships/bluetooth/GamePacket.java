package pl.mbassara.battleships.bluetooth;

import java.io.Serializable;

import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.ShotResult;

public class GamePacket implements Serializable{
	public static final boolean HOST_FIRST = false;
	public static final boolean CLIENT_FIRST = true;
	public static final int TYPE_WHO_STARTS = 0;
	public static final int TYPE_SHOT = 1;
	public static final int TYPE_RESULT = 2;
	public static final int TYPE_TEXT_MESSAGE = 3;
	
	private static final long serialVersionUID = 5504703199854898469L;
	private int type;
	private String message = null;
	private Coordinates coordinates = null;
	private boolean whoStarts = HOST_FIRST;
	private ShotResult result = null;
	
	public GamePacket(boolean whoStarts) {
		type = TYPE_WHO_STARTS;
		this.whoStarts = whoStarts;
	}
	
	public GamePacket(ShotResult result) {
		this.result = result;
		type = TYPE_RESULT;
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
	
	public ShotResult getResult() {
		return result;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
}
