package pl.mbassara.battleships;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class ShipButton extends ToggleButton {

	public static enum Size {
		BIG, SMALL
	}

	public enum LafType {
		POSSIBLE, IMPOSSIBLE, NORMAL, SELECTED, HIT, SUNK, MISSED, SHOOTABLE, NOT_SHOOTABLE, TARGET, PREVIOUS, SHOT
	}

	protected int x = -1;
	protected int y = -1;
	protected Size size = Size.BIG;
	protected LafType LaF = LafType.NORMAL;
	protected LafType prevLaF = LafType.NORMAL;

	public ShipButton(Context context, int x, int y) {
		super(context);
		this.x = x;
		this.y = y;
		setUp();
	}

	public ShipButton(Context context, int x, int y, Size size) {
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
		setLaF(LafType.NORMAL);
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

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
		setLaF(LaF);
	}

	public LafType getLaF() {
		return LaF;
	}

	public void setLaF(LafType laf) {
		if (laf == LafType.PREVIOUS) {
			setLaF(prevLaF);
			return;
		}

		LafType prevPrevLaf = prevLaF;
		prevLaF = LaF;
		LaF = laf;
		switch (laf) {
		case IMPOSSIBLE:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_impossible_small
					: R.drawable.ic_im_ship_impossible);
			break;
		case POSSIBLE:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_possible_small : R.drawable.ic_im_ship_possible);
			break;
		case NORMAL:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_small : R.drawable.ic_im_ship);
			break;
		case SELECTED:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_sel_small : R.drawable.ic_im_ship_sel);
			break;
		case HIT:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_hit_small : R.drawable.ic_im_ship_hit);
			break;
		case SUNK:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_sunk_small : R.drawable.ic_im_ship_sunk);
			break;
		case MISSED:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_missed_small : R.drawable.ic_im_ship_missed);
			break;
		case SHOOTABLE:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_shootable_small : R.drawable.ic_im_ship_shootable);
			break;
		case NOT_SHOOTABLE:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_not_shootable_small
					: R.drawable.ic_im_ship_not_shootable);
			break;
		case TARGET:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_shootable_target_small
					: R.drawable.ic_im_ship_shootable_target);
			break;
		case SHOT:
			setBackgroundResource((size == Size.SMALL) ? R.drawable.ic_im_ship_shootable_shot_small
					: R.drawable.ic_im_ship_shootable_shot);
			break;
		default:
			LaF = prevLaF;
			prevLaF = prevPrevLaf;
			break;
		}
	}

}