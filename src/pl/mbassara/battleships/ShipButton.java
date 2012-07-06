package pl.mbassara.battleships;

import android.annotation.TargetApi;
import android.content.Context;
import android.widget.ToggleButton;

@TargetApi(14)
public class ShipButton extends ToggleButton {

	private static final int NOT_SELECTED = 0;
	private static final int SOFT_SELECTED = 1;
	private static final int HARD_SELECTED = 2;
	
	private int x;
	private int y;
	private int state = NOT_SELECTED;

	public ShipButton(Context context) {
		this(context, -1, -1);
	}
	
	public ShipButton(Context context, int x, int y) {
		super(context);
		this.x = x;
		this.y = y;
		setBackgroundResource(R.drawable.ic_im_ship);
		setTextOn("");
		setTextOff("");
		setMinimumHeight(10);
		setMinimumWidth(10);
		setWidth(35);
		setHeight(35);
		setEnabled(false);
		setChecked(false);
	}
	
	public int getFieldX() {
		return x;
	}
	
	public int getFieldY() {
		return y;
	}
	
	public boolean isNotSelected() {
		return state == NOT_SELECTED;
	}
	
	public boolean isSoftSelected() {
		return state == SOFT_SELECTED;
	}
	
	public boolean isHardSelected() {
		return state == HARD_SELECTED;
	}
	
	public void setNotSelected() {
		state = NOT_SELECTED;
	}
	
	public void setSoftSelected() {
		state = SOFT_SELECTED;
	}
	
	public void setHardSelected() {
		state = HARD_SELECTED;
	}

}
