package pl.mbassara.battleships;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class ShipButton extends ToggleButton {
	
	public static final int SIZE_SMALL = 1;
	public static final int SIZE_BIG = 2;
	
	public static final int LAF_POSSIBLE = 3;
	public static final int LAF_IMPOSSIBLE = 4;
	public static final int LAF_NORMAL = 5;
	public static final int LAF_SELECTED = 6;
	public static final int LAF_HIT = 7;
	public static final int LAF_SUNK = 8;
	public static final int LAF_MISSED = 9;
	public static final int LAF_SHOOTABLE = 10;
	public static final int LAF_NOT_SHOOTABLE = 11;
	public static final int LAF_TARGET = 12;
	public static final int LAF_PREVIOUS = 13;
	public static final int LAF_SHOT = 14;

	protected int x = -1;
	protected int y = -1;
	protected int size = SIZE_BIG;
	protected int LaF = LAF_NORMAL;
	protected int prevLaF = LAF_NORMAL;
	
	public ShipButton(Context context, int x, int y) {
		super(context);
		this.x = x;
		this.y = y;
		setUp();
	}
	
	public ShipButton(Context context, int x, int y, int size) {
		super(context);
		this.x = x;
		this.y = y;
		this.size = size;
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
	
	private void setUp() {
		setLaF(LAF_NORMAL);
		setTextOn("");
		setTextOff("");
		setMinimumHeight(10);
		setMinimumWidth(10);
		setWidth(10);
		setHeight(10);
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
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		setLaF(LaF);
	}
	
	public int getLaF() {
		return LaF;
	}
	
	public void setLaF(int laf) {
		if(laf == LAF_PREVIOUS) {
			setLaF(prevLaF);
			return;
		}
		
		int prevPrevLaf = prevLaF;
		prevLaF = LaF;
		LaF = laf;
		switch (laf) {
			case LAF_IMPOSSIBLE:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_impossible_small : R.drawable.ic_im_ship_impossible);
				break;
			case LAF_POSSIBLE:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_possible_small : R.drawable.ic_im_ship_possible);
				break;
			case LAF_NORMAL:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_small : R.drawable.ic_im_ship);
				break;
			case LAF_SELECTED:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_sel_small : R.drawable.ic_im_ship_sel);
				break;
			case LAF_HIT:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_hit_small : R.drawable.ic_im_ship_hit);
				break;
			case LAF_SUNK:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_sunk_small : R.drawable.ic_im_ship_sunk);
				break;
			case LAF_MISSED:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_missed_small : R.drawable.ic_im_ship_missed);
				break;
			case LAF_SHOOTABLE:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_shootable_small : R.drawable.ic_im_ship_shootable);
				break;
			case LAF_NOT_SHOOTABLE:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_not_shootable_small : R.drawable.ic_im_ship_not_shootable);
				break;
			case LAF_TARGET:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_shootable_target_small : R.drawable.ic_im_ship_shootable_target);
				break;
			case LAF_SHOT:
				setBackgroundResource((size == SIZE_SMALL) ? R.drawable.ic_im_ship_shootable_shot_small : R.drawable.ic_im_ship_shootable_shot);
				break;
			default:
				LaF = prevLaF;
				prevLaF = prevPrevLaf;
				break;
		}
	}

}