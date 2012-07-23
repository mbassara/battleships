package pl.mbassara.battleships;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class CreatingShipsBoard extends Board 
	implements OnCheckedChangeListener {
	
	private CreatingShipsButton[][] ships;
	private boolean placingShipsMode = false;
	private ShipsCounter shipsCounter;
	
	public CreatingShipsBoard(Context context) {
		super(context);
        
        ships = new CreatingShipsButton[10][10];
        for(int i = 0; i < 10; i++)
        	for(int j = 0; j < 10; j++) {
        		ships[i][j] = new CreatingShipsButton(context, i, j);
        		ships[i][j].setOnCheckedChangeListener(this);
        		ships[i][j].setEnabled(false);
        		rows[i].addView(ships[i][j]);
        	}
		
	}
	
	public void createShipsCounter(Activity activity) {
		shipsCounter = new ShipsCounter(activity);
	}
    
    private boolean isFieldPossible(int x, int y) {
//		System.out.println("checking: " + x + "x" + y);
		
		boolean possible = true;
		boolean onTheEdgeSoftSelected = false;
		
//		EDGES
		if(possible && (x+1)<10) {
			possible = ships[x+1][y].isHardSelected() ? false : possible;
			onTheEdgeSoftSelected = ships[x+1][y].isSoftSelected() ? true : onTheEdgeSoftSelected;
		}
		if(possible && (y+1)<10) {
			possible = ships[x][y+1].isHardSelected() ? false : possible;
			onTheEdgeSoftSelected = ships[x][y+1].isSoftSelected() ? true : onTheEdgeSoftSelected;
		}
		if(possible && (x-1)>=0) {
			possible = ships[x-1][y].isHardSelected() ? false : possible;
			onTheEdgeSoftSelected = ships[x-1][y].isSoftSelected() ? true : onTheEdgeSoftSelected;
		}
		if(possible && (y-1)>=0) {
			possible = ships[x][y-1].isHardSelected() ? false : possible;
			onTheEdgeSoftSelected = ships[x][y-1].isSoftSelected() ? true : onTheEdgeSoftSelected;
		}
		
//		CORNERS
		if(possible && (x+1)<10 && (y-1)>=0 && !ships[x+1][y-1].isNotSelected()) {
			possible = ships[x+1][y-1].isSoftSelected() ? onTheEdgeSoftSelected : false;
		}
		if(possible && (x+1)<10 && (y+1)<10 && !ships[x+1][y+1].isNotSelected()) {
			possible = ships[x+1][y+1].isSoftSelected() ? onTheEdgeSoftSelected : false;
		}
		if(possible && (x-1)>=0 && (y+1)<10 && !ships[x-1][y+1].isNotSelected()) {
			possible = ships[x-1][y+1].isSoftSelected() ? onTheEdgeSoftSelected : false;
		}
		if(possible && (x-1)>=0 && (y-1)>=0 && !ships[x-1][y-1].isNotSelected()) {
			possible = ships[x-1][y-1].isSoftSelected() ? onTheEdgeSoftSelected : false;
		}

		return possible;
    }
    
    private boolean canUnselectField(int x, int y){
    	boolean possible = true;
    	
    	int currentSize = shipSize(x, y);
    	boolean wasSoftBefore = ships[x][y].isSoftSelected();
    	ships[x][y].setNotSelected();

		if(possible && (x+1)<10 && !ships[x+1][y].isNotSelected())
			possible = currentSize == shipSize(x+1, y) + 1;
		else if(possible && (y+1)<10 && !ships[x][y+1].isNotSelected())
			possible = currentSize == shipSize(x, y+1) + 1;
		else if(possible && (x-1)>=0 && !ships[x-1][y].isNotSelected())
			possible = currentSize == shipSize(x-1, y) + 1;
		else if(possible && (y-1)>=0 && !ships[x][y-1].isNotSelected())
			possible = currentSize == shipSize(x, y-1) + 1;		
		
		if(wasSoftBefore)
			ships[x][y].setSoftSelected();
		else
			ships[x][y].setHardSelected();
		
    	return possible;
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
		
    	if((x+1)<10 && matrix[x+1][y] && !ships[x+1][y].isNotSelected())
    		size += shipSizeRecursive(x+1, y, matrix);
    	if((y+1)<10 && matrix[x][y+1] && !ships[x][y+1].isNotSelected())
    		size += shipSizeRecursive(x, y+1, matrix);
    	if((x-1)>=0 && matrix[x-1][y] && !ships[x-1][y].isNotSelected())
    		size += shipSizeRecursive(x-1, y, matrix);
    	if((y-1)>=0 && matrix[x][y-1] && !ships[x][y-1].isNotSelected())
    		size += shipSizeRecursive(x, y-1, matrix);

		return size + 1;
    }
    
    public void addShip(View view) {
    	placingShipsMode = ((ToggleButton) view).isChecked();
    	if(placingShipsMode) {
	        for(int i = 0; i < 10; i++)
	        	for(int j = 0; j < 10; j++) {
        			ships[i][j].setEnabled(true);
	        		if(ships[i][j].isNotSelected()) {
		        		if(isFieldPossible(i, j))
		        			ships[i][j].setBackgroundResource(R.drawable.ic_im_ship_possible);
		        		else
		        			ships[i][j].setBackgroundResource(R.drawable.ic_im_ship_impossible);
	        		}
	        	}
    	}
    	else {
	        for(int i = 0; i < 10; i++)
	        	for(int j = 0; j < 10; j++) {
	        		ships[i][j].setEnabled(false);
	        		if(ships[i][j].isNotSelected())
		        		ships[i][j].setBackgroundResource(R.drawable.ic_im_ship);
	        		else if(ships[i][j].isSoftSelected())
	        			ships[i][j].setHardSelected();
	        	}
    	}
    }
    
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		CreatingShipsButton ship = (CreatingShipsButton) buttonView;
		int x = ship.getFieldX();
		int y = ship.getFieldY();
		int size = shipSize(x, y);
		if(!isChecked && canUnselectField(x, y)) {
			ship.setNotSelected();
			shipsCounter.removeShip(size);
			shipsCounter.addShip(size-1);
			if(placingShipsMode)
				ship.setBackgroundResource(R.drawable.ic_im_ship_possible);
			else
				ship.setBackgroundResource(R.drawable.ic_im_ship);
		}
		if(isChecked) {
			boolean possible = isFieldPossible(x, y) && shipsCounter.canAddShip(size);
			
			buttonView.setChecked(possible);
			
			if(possible) {
				ship.setSoftSelected();
				shipsCounter.removeShip(size-1);
				shipsCounter.addShip(size);
				ship.setBackgroundResource(R.drawable.ic_im_ship_sel);
			}
		}
	}
	
	public boolean[][] getBoardMatrix() {
		boolean[][] matrix = new boolean[10][10];

		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				matrix[i][j] = ships[i][j].isHardSelected();
		
		return matrix;
	}
}
