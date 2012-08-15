package pl.mbassara.battleships;

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
    }
    
    public void enableVibra(View view) {
    	CheckBox button = (CheckBox) view;
    	Vibra.setEnabled(button.isChecked());
    	if(Vibra.isEnabled())
    		Vibra.getInstance(this).beepTriple();
    }
    
}
