package pl.mbassara.battleships;

import android.content.Context;

public class GameShipButton extends ShipButton {

	public static final int NOT_SHIP = 0;
	public static final int SHIP = 1;
	public static final int HIT = 2;
	public static final int SUNK = 3;
	public static final int MISSED = 4;
	
	private int state;
	
	public GameShipButton(Context context) {
		this(context, NOT_SHIP, -1, -1, SIZE_BIG);
	}

	public GameShipButton(Context context, int state, int x, int y, int size) {
		super(context, x, y, size);
		
		this.state = state;
		
		if(state == NOT_SHIP)
			this.setLaF(LAF_NORMAL);
		else if(state == SHIP)
			this.setLaF(LAF_SELECTED);
		else if(state == SUNK)
			this.setLaF(LAF_SUNK);
		else if(state == HIT)
			this.setLaF(LAF_HIT);
		else if(state == MISSED)
			this.setLaF(LAF_MISSED);
	}
	
	public boolean shoot() {
		if(state == SHIP) {
			setHit();
			return true;
		}
		else if(state == SUNK || state == HIT)
			return true;
		else {
			setMissed();
			return false;	
		}
	}
	
	public boolean isShip() {
		return state == SHIP;
	}
	
	public boolean isHit() {
		return state == SUNK || state == HIT;
	}
	
	public boolean isSunk() {
		return state == SUNK;
	}
	
	public boolean isFinished() {	// sunk, missed or not ship
		return state == SUNK || state == MISSED || state == NOT_SHIP;
	}
	
	protected void setSunk() {
		state = SUNK;
		this.setLaF(LAF_SUNK);
	}
	
	protected void setHit() {
		state = HIT;
		this.setLaF(LAF_HIT);
	}
	
	protected void setMissed() {
		state = MISSED;
		this.setLaF(LAF_MISSED);
	}

}
