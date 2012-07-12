package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.GameActivity;
import pl.mbassara.battleships.CreatingShipsGameBoard;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CreatingShipsActivity extends Activity{

	private static CreatingShipsGameBoard board = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_ships);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.creating_ships_layout);
        board = new CreatingShipsGameBoard(this);
        board.createShipsCounter(this);
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
    
    public void addShip(View view){
    	board.addShip(view);
    }
    
    public void next(View view) {
    	Intent intent = new Intent(this, GameActivity.class);
    	startActivity(intent);
	}
    
    public static CreatingShipsGameBoard getBoard() {
    	return board;
    }
}

