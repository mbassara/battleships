package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        
        TextView gameResultTextView = (TextView) findViewById(R.id.game_result_textView);
        
        if(getIntent().getBooleanExtra(GameActivity.KEY_GAME_RESULT, false))
        	gameResultTextView.setText(getString(R.string.result_winner));
        else
        	gameResultTextView.setText(getString(R.string.result_looser));
        	
        
    }
    
    public void newGame(View view) {
    	Intent intent = new Intent(this, GameModeActivity.class);
    	startActivity(intent);
    }

    public void exit(View view) {
    	Toast.makeText(this, getString(R.string.not_available_yet), Toast.LENGTH_SHORT).show();
    }
    
}
