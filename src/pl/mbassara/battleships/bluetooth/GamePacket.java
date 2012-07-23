package pl.mbassara.battleships.bluetooth;

import java.io.Serializable;

import pl.mbassara.battleships.Coordinates;

public class GamePacket implements Serializable{
	private static final long serialVersionUID = 5504703199854898469L;
	private String message = null;
	private Coordinates coordinates = null;
	
	public GamePacket(int x, int y) {
		coordinates = new Coordinates(x, y);
	}
	
	public GamePacket(String message) {
		this.message = message;
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
