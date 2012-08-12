package pl.mbassara.battleships;

import java.io.Serializable;

public class Coordinates implements Serializable {
	private static final long serialVersionUID = -8267406114346251730L;
	private int x;
	private int y;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}