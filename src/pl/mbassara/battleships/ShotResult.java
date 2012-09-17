package pl.mbassara.battleships;

import java.io.Serializable;

public class ShotResult implements Serializable{
	private static final long serialVersionUID = 635685272254738631L;
	private boolean[][] matrix;
	private boolean isHit;
	private boolean isSunk;
	private boolean isGameEnded;
	private Coordinates coordinates;
	
	public ShotResult(boolean isHit, boolean isSunk, boolean isGameEnded, Coordinates coordinates, boolean[][] matrix) {
		this.isHit = isHit;
		this.isSunk = isSunk;
		this.isGameEnded = isGameEnded;
		this.coordinates = coordinates;
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
	
	public boolean isGameEnded() {
		return isGameEnded;
	}

	public boolean[][] getMatrix() {
		return matrix;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
}
