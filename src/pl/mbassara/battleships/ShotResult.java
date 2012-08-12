package pl.mbassara.battleships;

import java.io.Serializable;

public class ShotResult implements Serializable{
	private static final long serialVersionUID = 635685272254738631L;
	private boolean[][] matrix;
	private boolean isHit;
	private boolean isSunk;
	
	public ShotResult(boolean isHit, boolean isSunk, boolean[][] matrix) {
		this.isHit = isHit;
		this.isSunk = isSunk;
		this.matrix = matrix;
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
}
