package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.Vibra;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

public class OptionsActivity extends Activity {

	private Global global = Global.getInstance();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_options);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	((CheckBox) findViewById(R.id.enableVibraCheckBox)).setChecked(Vibra.isEnabled());
    	((CheckBox) findViewById(R.id.enableTipsCheckBox)).setChecked(global.SHOOTING_TIPS_ENABLED);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	this.finish();
    }
    
    public void enableVibra(View view) {
    	CheckBox button = (CheckBox) view;
    	Vibra.setEnabled(button.isChecked());
    	if(Vibra.isEnabled())
    		Vibra.getInstance(this).beepTriple();
    }
    
    public void enableTips(View view) {
    	CheckBox button = (CheckBox) view;
    	global.SHOOTING_TIPS_ENABLED = button.isChecked();
    	if(button.isChecked())
    		Vibra.getInstance(this).beepTriple();
    }
    
}
