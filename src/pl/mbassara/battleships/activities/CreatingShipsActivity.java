package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.AIComputer;
import pl.mbassara.battleships.Board;
import pl.mbassara.battleships.CreatingShipsBoard;
import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ToggleButton;

public class CreatingShipsActivity extends Activity{

	private static CreatingShipsBoard board = null;
	private Global global = Global.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_creating_ships);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.creating_ships_layout);
        board = new CreatingShipsBoard(this, Board.SIZE_BIG);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        board.setLayoutParams(layoutParams);
        layout.addView(board);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	this.finish();
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
   		intent = new Intent(this, GameActivity.class);

   		global.setLocalUserBoardMatrix(board.getBoardMatrix());
   		this.startActivity(intent);
    	this.finish();
	}
    
}
