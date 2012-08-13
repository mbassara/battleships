package pl.mbassara.battleships;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CreatingShipsBoard extends Board 
	implements OnCheckedChangeListener {
	
	private CreatingShipsButton[][] ships;
	private ToggleButton placeShipsButton;
	private Activity activity;
	
	public CreatingShipsBoard(Context context) {
		this(context, SIZE_BIG);
	}
	
	public CreatingShipsBoard(Context context, int size) {
		super(context);
        
		activity = (Activity) context;
        ships = new CreatingShipsButton[10][10];
        placeShipsButton = (ToggleButton) activity.findViewById(R.id.place_ships_button);
        placeShipsButton.setEnabled(false);
        
        for(int i = 0; i < 10; i++)
        	for(int j = 0; j < 10; j++) {
        		ships[i][j] = new CreatingShipsButton(context, i, j, size);
        		ships[i][j].setOnCheckedChangeListener(this);
        		ships[i][j].setEnabled(false);
        		rows[i].addView(ships[i][j]);
        	}
        
        placeShipsButton.setEnabled(true);
	}
	
	public void setBoard(boolean[][] matrix) {
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				if(matrix[i][j])
					ships[i][j].setSelected();
				else if(placeShipsButton != null)
					ships[i][j].setNotSelected(placeShipsButton.isChecked());
				else
					ships[i][j].setNotSelected(false);
		
	}
    
    private int shipSize(int x, int y) {
    	boolean[][] matrix = new boolean[10][10];
    	
    	for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				matrix[i][j] = true;
    	
    	return shipSizeRecursive(x, y, matrix);
    }
    
    private int shipSizeRecursive(int x, int y, boolean[][] matrix) {
    	
    	int size = 0;
    	matrix[x][y] = false;
		
    	if((x+1)<10 && matrix[x+1][y] && ships[x+1][y].isSelected())
    		size += shipSizeRecursive(x+1, y, matrix);
    	if((y+1)<10 && matrix[x][y+1] && ships[x][y+1].isSelected())
    		size += shipSizeRecursive(x, y+1, matrix);
    	if((x-1)>=0 && matrix[x-1][y] && ships[x-1][y].isSelected())
    		size += shipSizeRecursive(x-1, y, matrix);
    	if((y-1)>=0 && matrix[x][y-1] && ships[x][y-1].isSelected())
    		size += shipSizeRecursive(x, y-1, matrix);

		return size + 1;
    }
    
    public boolean checkShipsBoard() {
    	boolean[][] matrix = new boolean[10][10];
    	
    	for (int i = 0; i < 10; i++)
    		for (int j = 0; j < 10; j++)
    			matrix[i][j] = true;

    	boolean result = true;
    	
    	for (int i = 0; i < 10; i++)
    		for (int j = 0; j < 10; j++)
    			if (result && matrix[i][j] && ships[i][j].isSelected())
    				result = result && !checkShip(matrix, i, j).isUndefined();
    	
    	if(Constants.LOGS_ENABLED) System.out.println("checking ships result: " + result);
		return result;
    }
    
    public Direction checkShip(boolean[][] matrix, int x, int y) {

    	boolean anotherShipOnTheCorner = false;
    	if((x+1)<10 && (y+1)<10 && ships[x+1][y+1].isSelected())
    		anotherShipOnTheCorner = true;
    	if((x-1)>=0 && (y+1)<10 && ships[x-1][y+1].isSelected())
    		anotherShipOnTheCorner = true;
    	if((x+1)<10 && (y-1)>=0 && ships[x+1][y-1].isSelected())
    		anotherShipOnTheCorner = true;
    	if((x-1)>=0 && (y-1)>=0 && ships[x-1][y-1].isSelected())
    		anotherShipOnTheCorner = true;
    	
    	if(anotherShipOnTheCorner)
    		return new Direction(false, false);
    	
    	matrix[x][y] = false;
    	Direction dir = new Direction();
		
    	if((x+1)<10 && ships[x+1][y].isSelected()) {
    		dir.setHorizontal();
    		if (matrix[x+1][y])
    			dir.concatenate(checkShip(matrix, x+1, y));
    	}
    	if((y+1)<10 && ships[x][y+1].isSelected()) {
    		dir.setVertical();
    		if (matrix[x][y+1])
    			dir.concatenate(checkShip(matrix, x, y+1));
    	}
    	if((x-1)>=0 && ships[x-1][y].isSelected()) {
    		dir.setHorizontal();
    		if (matrix[x-1][y])
    			dir.concatenate(checkShip(matrix, x-1, y));
    	}
    	if((y-1)>=0 && ships[x][y-1].isSelected()) {
    		dir.setVertical();
    		if (matrix[x][y-1])
    			dir.concatenate(checkShip(matrix, x, y-1));
    	}
    	
    	if(Constants.LOGS_ENABLED) System.out.println("\tchecking ship " + x + "," + y + " result: " + dir);
    	
    	return dir;	
    }
    
    public boolean togglePlacingShipsMode(ToggleButton button) {
    	if(button.isChecked()) {
	        for(int i = 0; i < 10; i++)
	        	for(int j = 0; j < 10; j++) {
        			ships[i][j].setEnabled(true);
	        		if(ships[i][j].isNotSelected())
	        			ships[i][j].setLaF(ShipButton.LAF_POSSIBLE);
	        	}
	        return true;
    	}
    	else {
    		if(checkShipsBoard()) {
		        for(int i = 0; i < 10; i++)
		        	for(int j = 0; j < 10; j++) {
		        		ships[i][j].setEnabled(false);
		        		if(ships[i][j].isNotSelected())
		        			ships[i][j].setLaF(ShipButton.LAF_NORMAL);
		        	}
		        return true;
    		}
    		else {
    			placeShipsButton.setChecked(true);
    			Toast.makeText(activity, activity.getString(R.string.placing_ships_error), Toast.LENGTH_SHORT).show();
    			return false;
    		}
    	}
    }
	
	public boolean[][] getBoardMatrix() {
		boolean[][] matrix = new boolean[10][10];

		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				matrix[i][j] = ships[i][j].isSelected();
		
		return matrix;
	}
    
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		CreatingShipsButton ship = (CreatingShipsButton) buttonView;
		if(isChecked) {
			ship.setChecked(true);
			ship.setSelected();
   			ship.setLaF(ShipButton.LAF_SELECTED);
		}
		else {
			ship.setNotSelected(placeShipsButton.isChecked());
		}
	}
}

class Direction {
	static final int UNDEFINED = 0;
	static final int HORIZONTAL = 1;
	static final int VERTICAL = 2;
	
	private boolean horizontal;
	private boolean vertical;
	
	public Direction() {
		this(false, false);
	}
	
	public Direction(boolean horizontal, boolean vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	
	public void concatenate(Direction dir) {
		if (dir.getState()[0])
			horizontal = dir.getState()[0];
		if (dir.getState()[1])
			vertical = dir.getState()[1];
	}
	
	public boolean[] getState() {
		boolean[] vals = new boolean[2];
		vals[0] = horizontal;
		vals[1] = vertical;
		
		return vals;
	}
	
	public boolean isHorizontal() {
		return horizontal && !vertical;
	}
	
	public boolean isVertical() {
		return vertical && !horizontal;
	}
	
	public boolean isUndefined() {
		return !(vertical ^ horizontal);
	}
	
	public void setHorizontal() {
		horizontal = true;
	}
	
	public void setVertical() {
		vertical = true;
	}
	
	@Override
	public String toString() {
		String string = "";
		if(isUndefined())
			string = "undefined";
		else if(isHorizontal())
			string = "horizontal";
		else if(isVertical())
			string = "vertical";
		return string;
	}
}