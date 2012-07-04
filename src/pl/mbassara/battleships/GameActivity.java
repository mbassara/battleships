package pl.mbassara.battleships;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TableRow;

@TargetApi(16)
public class GameActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Context context = getApplicationContext();
        
        TableRow[] rows = {
        		(TableRow) findViewById(R.id.tableRow0),
        		(TableRow) findViewById(R.id.tableRow1),
        		(TableRow) findViewById(R.id.tableRow2),
        		(TableRow) findViewById(R.id.tableRow3),
        		(TableRow) findViewById(R.id.tableRow4),
        		(TableRow) findViewById(R.id.tableRow5),
        		(TableRow) findViewById(R.id.tableRow6),
        		(TableRow) findViewById(R.id.tableRow7),
        		(TableRow) findViewById(R.id.tableRow8),
        		(TableRow) findViewById(R.id.tableRow9),
        };

        ShipButton[][] ships = new ShipButton[10][10];
        for(int i = 0; i < 10; i++)
        	for(int j = 0; j < 10; j++) {
        		ships[i][j] = new ShipButton(context, i*10 + j);
        		rows[i].addView(ships[i][j]);
        	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }

    
}
