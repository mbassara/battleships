package pl.mbassara.battleships;

import java.io.Serializable;

public class ShotResult implements Serializable{
	private static final long serialVersionUID = 635685272254738631L;
	private boolean[][] matrix;
	private boolean isHit;
	private boolean isSunk;
	private Coordinates coordinates;
	
	public ShotResult(boolean isHit, boolean isSunk, Coordinates coordinates) {
		this.isHit = isHit;
		this.isSunk = isSunk;
		this.coordinates = coordinates;
	}
	
	public ShotResult(boolean isHit, boolean isSunk, boolean[][] matrix) {
		this.isHit = isHit;
		this.isSunk = isSunk;
		this.matrix = matrix;
	}
	
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	
	public boolean isHit() {
		return isHit;
	}
	
	public boolean isSunk() {
		return isSunk;
	}

	public boolean[][] getMatrix() {
		return matrix;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
}
