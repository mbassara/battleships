package pl.mbassara.battleships;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.ToggleButton;

@TargetApi(16)
public class GameActivity extends Activity
	implements OnCheckedChangeListener{
	
	private ShipButton[][] ships;
	
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

        ships = new ShipButton[10][10];
        for(int i = 0; i < 10; i++)
        	for(int j = 0; j < 10; j++) {
        		ships[i][j] = new ShipButton(context, i, j);
        		ships[i][j].setOnCheckedChangeListener(this);
        		rows[i].addView(ships[i][j]);
        	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
    
    private boolean isFieldPossible(int x, int y) {
//		System.out.println("checking: " + x + "x" + y);
		
		boolean possible = true;
		
		if(possible && (x+1)<10 && (y-1)>=0 && ships[x+1][y-1].isChecked()) {
			possible = false;
//			System.out.println("Lower-left");
		}
		else if(possible && (x+1)<10 && ships[x+1][y].isChecked()) {
			possible = false;
//			System.out.println("Lower");
		}
		else if(possible && (x+1)<10 && (y+1)<10 && ships[x+1][y+1].isChecked()) {
			possible = false;
//			System.out.println("Lower-right");
		}
		else if(possible && (y+1)<10 && ships[x][y+1].isChecked()) {
			possible = false;
//			System.out.println("Right");
		}
		else if(possible && (x-1)>=0 && (y+1)<10 && ships[x-1][y+1].isChecked()) {
			possible = false;
//			System.out.println("Upper-right");
		}
		else if(possible && (x-1)>=0 && ships[x-1][y].isChecked()) {
			possible = false;
//			System.out.println("Upper");
		}
		else if(possible && (x-1)>=0 && (y-1)>=0 && ships[x-1][y-1].isChecked()) {
			possible = false;
//			System.out.println("Upper-left");
		}
		else if(possible && (y-1)>=0 && ships[x][y-1].isChecked()) {
			possible = false;
//			System.out.println("Left");
		}
		
		return possible;
    }
    
    public void addShip(View view) {
    	boolean adding_mode = ((ToggleButton) view).isChecked();
    	if(adding_mode) {
	        for(int i = 0; i < 10; i++)
	        	for(int j = 0; j < 10; j++)
	        		if(!ships[i][j].isChecked()) {
		        		if(isFieldPossible(i, j)) {
		        			ships[i][j].setEnabled(true);
		        			ships[i][j].setBackgroundResource(R.drawable.ic_im_ship_possible);
		        		}
		        		else
		        			ships[i][j].setBackgroundResource(R.drawable.ic_im_ship_impossible);
	        		}
    	}
    	else {
	        for(int i = 0; i < 10; i++)
	        	for(int j = 0; j < 10; j++) {
	        		ships[i][j].setEnabled(false);
	        		if(!ships[i][j].isChecked())
		        		ships[i][j].setBackgroundResource(R.drawable.ic_im_ship);
	        	}
    	}
    }
    
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int x = ((ShipButton) buttonView).getFieldX();
		int y = ((ShipButton) buttonView).getFieldY();
		if(!isChecked) {
			System.out.println("not is checked: " + x + "x" + y);
			buttonView.setBackgroundResource(R.drawable.ic_im_ship);
		}
		if(isChecked) {
			boolean possible = isFieldPossible(x, y);
			
			buttonView.setChecked(possible);
			
			if(possible)
				buttonView.setBackgroundResource(R.drawable.ic_im_ship_sel);
		}
	}
}

