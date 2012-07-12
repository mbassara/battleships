package pl.mbassara.battleships;

import android.content.Context;
import android.util.AttributeSet;

public class CreatingShipsButton extends ShipButton {

	public CreatingShipsButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CreatingShipsButton(Context context, int x, int y) {
		super(context, x, y);
	}

	public CreatingShipsButton(Context context) {
		super(context);
	}

	public CreatingShipsButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private static final int NOT_SELECTED = 0;
	private static final int SOFT_SELECTED = 1;
	private static final int HARD_SELECTED = 2;
	
	private int state = NOT_SELECTED;
	
	public CreatingShipsButton clone() {
		try {
			CreatingShipsButton clone = (CreatingShipsButton) super.clone();
			clone.setState(state);
			clone.setFieldX(x);
			clone.setFieldY(y);
			
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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
