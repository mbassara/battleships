package pl.mbassara.battleships;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class ShipButton extends ToggleButton {

	protected int x = -1;
	protected int y = -1;
	
	public ShipButton(Context context, int x, int y) {
		super(context);
		this.x = x;
		this.y = y;
		setUp();
	}
	
	public ShipButton(Context context, AttributeSet attrs, int x, int y) {
		super(context, attrs);
		this.x = x;
		this.y = y;
		setUp();
	}
	
	public ShipButton(Context context, AttributeSet attrs, int defStyle, int x, int y) {
		super(context, attrs, defStyle);
		this.x = x;
		this.y = y;
		setUp();
	}

	public ShipButton(Context context) {
		super(context);
		setUp();
	}

	public ShipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUp();
	}

	public ShipButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setUp();
	}

	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	private void setUp() {
		setBackgroundResource(R.drawable.ic_im_ship);
		setTextOn("");
		setTextOff("");
		setMinimumHeight(10);
		setMinimumWidth(10);
		setSize(35, 35);
		setChecked(false);
	}

	public void setFieldX(int x) {
		this.x = x;
	}

	public void setFieldY(int y) {
		this.y = y;
	}

	public int getFieldX() {
		return x;
	}

	public int getFieldY() {
		return y;
	}

}