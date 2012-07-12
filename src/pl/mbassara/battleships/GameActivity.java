package pl.mbassara.battleships;

import pl.mbassara.battleships.activities.CreatingShipsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GameActivity extends Activity {
	
	private CreatingShipsGameBoard board;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        board = CreatingShipsActivity.getBoard();
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game_layout);
        board = new CreatingShipsGameBoard(this);
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

    
}
