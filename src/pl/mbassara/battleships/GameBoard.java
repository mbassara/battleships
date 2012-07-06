package pl.mbassara.battleships;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

public class GameBoard extends TableLayout 
	implements OnCheckedChangeListener {
	
	private ShipButton[][] ships;
	private boolean[][] multipurposeMatrix = new boolean[10][10]; 
	private boolean placingShipsMode = false;
	private ShipsCounter shipsCounter;

	public GameBoard(Context context, View parent) {
		super(context);
		shipsCounter = new ShipsCounter(parent);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
														LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.setLayoutParams(layoutParams);
        
        TableRow[] rows = new TableRow[10];
        for(int i = 0; i < 10; i++) {
        	rows[i] = new TableRow(context);
        	rows[i].setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        	this.addView(rows[i]);
        }
        
        ships = new ShipButton[10][10];
        for(int i = 0; i < 10; i++)
        	for(int j = 0; j < 10; j++) {
        		ships[i][j] = new ShipButton(context, i, j);
        		ships[i][j].setOnCheckedChangeListener(this);
        		rows[i].addView(ships[i][j]);
        	}
		
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
    	for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				multipurposeMatrix[i][j] = true;
    	
    	return shipSizeRecursive(x, y);
    }
    
    private int shipSizeRecursive(int x, int y) {
    	
    	int size = 0;
    	multipurposeMatrix[x][y] = false;
		
    	if((x+1)<10 && multipurposeMatrix[x+1][y] && !ships[x+1][y].isNotSelected())
    		size += shipSizeRecursive(x+1, y);
    	if((y+1)<10 && multipurposeMatrix[x][y+1] && !ships[x][y+1].isNotSelected())
    		size += shipSizeRecursive(x, y+1);
    	if((x-1)>=0 && multipurposeMatrix[x-1][y] && !ships[x-1][y].isNotSelected())
    		size += shipSizeRecursive(x-1, y);
    	if((y-1)>=0 && multipurposeMatrix[x][y-1] && !ships[x][y-1].isNotSelected())
    		size += shipSizeRecursive(x, y-1);

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
		ShipButton ship = (ShipButton) buttonView;
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
}
