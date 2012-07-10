package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CreatingShipsActivity extends Activity{
	
	GameBoard board;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_ships);
        Context context = getApplicationContext();
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.creating_ships_layout);
        board = new GameBoard(context, layout);
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
}

