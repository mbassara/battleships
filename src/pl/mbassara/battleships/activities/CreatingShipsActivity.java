package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.activities.GameActivity;
import pl.mbassara.battleships.Board;
import pl.mbassara.battleships.CreatingShipsBoard;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
    
    public void togglePlacingShipsMode(View view){
    	board.togglePlacingShipsMode(view);
    }
    
    public void next(View view) {
    	Intent intent = new Intent(this, GameActivity.class);
    	startActivity(intent);
	}
    
    public static boolean[][] getBoardMatrix() {
    	return board.getBoardMatrix();
    }
}

