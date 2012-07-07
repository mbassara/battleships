package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.R.id;
import pl.mbassara.battleships.R.layout;
import pl.mbassara.battleships.R.menu;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

@TargetApi(16)
public class GameActivity extends Activity{
	
	GameBoard board;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Context context = getApplicationContext();
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game_layout);
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

