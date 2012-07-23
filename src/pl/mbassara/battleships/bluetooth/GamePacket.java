package pl.mbassara.battleships.bluetooth;

import java.io.Serializable;

import pl.mbassara.battleships.Coordinates;

public class GamePacket implements Serializable{
	public static final boolean HOST_FIRST = false;
	public static final boolean CLIENT_FIRST = true;
	public static final int WHO_STARTS = 0;
	public static final int SHOT = 1;
	public static final int TEXT_MESSAGE = 2;
	
	private static final long serialVersionUID = 5504703199854898469L;
	private int type;
	private String message = null;
	private Coordinates coordinates = null;
	private boolean whoStarts = HOST_FIRST;
	
	public GamePacket(boolean who) {
		whoStarts = who;
		type = WHO_STARTS;
	}
	
	public GamePacket(int x, int y) {
		coordinates = new Coordinates(x, y);
		type = SHOT;
	}
	
	public GamePacket(String message) {
		this.message = message;
		type = TEXT_MESSAGE;
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

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
}
