package pl.mbassara.battleships;

import android.content.Context;

public class GameShipButton extends ShipButton {

	public static int NOT_SHIP = 0;
	public static int SHIP = 1;
	public static int SUNK = 2;
	
	private Context context;
	private int state;
	
	public GameShipButton(Context context) {
		this(context, NOT_SHIP, -1, -1);
	}

	public GameShipButton(Context context, int state, int x, int y) {
		super(context, x, y);
		
		this.context = context;
		this.state = state;
		
		if(state == NOT_SHIP)
			this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_im_ship));
		else if(state == SHIP)
			this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_im_ship_sel));
		else if(state == SUNK)
			this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_im_ship_sunk));
	}
	
	public boolean isShip() {
		return state != NOT_SHIP;
	}
	
	public boolean isSunk() {
		return state == SUNK;
	}
	
	public void setSunk() {
		state = SUNK;
		this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_im_ship_sunk));
	}

}
