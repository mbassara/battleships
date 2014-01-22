package pl.mbassara.battleships;

import android.content.Context;
import android.util.AttributeSet;

public class CreatingShipsButton extends ShipButton {

	public CreatingShipsButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CreatingShipsButton(Context context, int x, int y, Size size) {
		super(context, x, y, size);
	}

	public CreatingShipsButton(Context context) {
		super(context);
	}

	public CreatingShipsButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean isSelected = true;

	public boolean isNotSelected() {
		return !isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setNotSelected(boolean creatingMode) {
		isSelected = false;
		if (creatingMode)
			this.setLaF(LafType.POSSIBLE);
		else
			this.setLaF(LafType.NORMAL);
	}

	public void setSelected() {
		isSelected = true;
		this.setLaF(LafType.SELECTED);
	}

}
