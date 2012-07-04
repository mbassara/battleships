package pl.mbassara.battleships;

import android.annotation.TargetApi;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

@TargetApi(14)
public class ShipButton extends ToggleButton {

	public ShipButton(Context context) {
		this(context, -1);
	}
	
	public ShipButton(Context context, int num) {
		super(context);
		setBackgroundResource(R.drawable.ic_im_but);
		setTextOn("");
		setTextOff("");
		setMinimumHeight(10);
		setMinimumWidth(10);
		setWidth(35);
		setHeight(35);
		setChecked(false);
		
		setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
					buttonView.setBackgroundResource(R.drawable.ic_im_but_hov);
				else
					buttonView.setBackgroundResource(R.drawable.ic_im_but);
			}
		});
	}

}
