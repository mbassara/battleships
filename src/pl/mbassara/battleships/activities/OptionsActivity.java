package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.Vibra;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;

public class OptionsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	((CheckBox) findViewById(R.id.enableVibraCheckBox)).setChecked(Vibra.isEnabled());
    	((CheckBox) findViewById(R.id.enableTipsCheckBox)).setChecked(Global.SHOOTING_TIPS_ENABLED);
    }
    
    public void enableVibra(View view) {
    	CheckBox button = (CheckBox) view;
    	Vibra.setEnabled(button.isChecked());
    	if(Vibra.isEnabled())
    		Vibra.getInstance(this).beepTriple();
    }
    
    public void enableTips(View view) {
    	CheckBox button = (CheckBox) view;
    	Global.SHOOTING_TIPS_ENABLED = button.isChecked();
    	if(button.isChecked())
    		Vibra.getInstance(this).beepTriple();
    }
    
}
