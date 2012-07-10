package pl.mbassara.battleships.bluetooth;

import java.io.Serializable;

public class GamePacket implements Serializable{
	private static final long serialVersionUID = 5504703199854898469L;
	private String message;
	
	public GamePacket(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
