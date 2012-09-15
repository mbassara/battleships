package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.activities.GameActivity;
import pl.mbassara.battleships.AIComputer;
import pl.mbassara.battleships.Board;
import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.CreatingShipsBoard;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.RelativeLayout.LayoutParams;

public class CreatingShipsActivity extends Activity{

	private static CreatingShipsBoard board = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_ships);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.creating_ships_layout);
        board = new CreatingShipsBoard(this, Board.SIZE_BIG);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        board.setLayoutParams(layoutParams);
        layout.addView(board);
    }
    
    public void togglePlacingShipsMode(View view){
    	boolean successfull = board.togglePlacingShipsMode((ToggleButton) view);
    	if(successfull) {
        	((Button) findViewById(R.id.next_button)).setEnabled(!((ToggleButton) view).isChecked());
        	((Button) findViewById(R.id.create_random_button)).setEnabled(((ToggleButton) view).isChecked());
    	}
    }
    
    public void random(View view){
    	board.setBoard(AIComputer.generateMatrix());
    }
    
    public void next(View view) {
    	Intent intent;
    	if(getIntent().getStringExtra(Constants.GAME_TYPE).equals(Constants.MULTIPLAYER))
    		intent = new Intent(this, GameActivity.class);
    	else
    		intent = new Intent(this, OfflineGameActivity.class);
    	
    	startActivity(intent);
    	this.finish();
	}
    
    public static boolean[][] getBoardMatrix() {
    	return board.getBoardMatrix();
    }
}

