package pl.mbassara.battleships;

import android.content.Context;

public class GameShipButton extends ShipButton {

	public static final int NOT_SHIP = 0;
	public static final int SHIP = 1;
	public static final int HIT = 2;
	public static final int SUNK = 3;
	public static final int MISSED = 4;
	
	private int state;
	private Vibra vibra;
	
	public GameShipButton(Context context) {
		this(context, NOT_SHIP, -1, -1, SIZE_BIG);
	}

	public GameShipButton(Context context, int state, int x, int y, int size) {
		super(context, x, y, size);

		this.vibra = Vibra.getInstance(context);
		this.state = state;
		
		if(state == NOT_SHIP)
			this.setNotShip();
		else if(state == SHIP)
			this.setShip();
		else if(state == SUNK)
			this.setSunk();
		else if(state == HIT)
			this.setHit();
		else if(state == MISSED)
			this.setMissed();
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
	
	public boolean isNotShip() {
		return state == NOT_SHIP;
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
	
	protected void setNotShip() {
		state = NOT_SHIP;
		this.setLaF(LAF_NORMAL);
	}
	
	protected void setShip() {
		state = SHIP;
		this.setLaF(LAF_SELECTED);
	}
	
	protected void setSunk() {
		vibra.beepTriple();
		state = SUNK;
		this.setLaF(LAF_SUNK);
	}
	
	protected void setHit() {
		vibra.beepDouble();
		state = HIT;
		this.setLaF(LAF_HIT);
	}
	
	protected void setMissed() {
		vibra.beepLong();
		state = MISSED;
		this.setLaF(LAF_MISSED);
	}

}
