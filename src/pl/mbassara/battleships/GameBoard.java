package pl.mbassara.battleships;

import android.content.Context;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GameBoard extends Board {
	
	private GameShipButton[][] ships;
	private boolean[][] shipsMatrix;

	public GameBoard(Context context) {
		this(context, null, SIZE_BIG);
	}
	
	public GameBoard(Context context, boolean[][] matrix, int size) {
		super(context);
		
		this.shipsMatrix = matrix;
		this.ships = new GameShipButton[10][10];
		int state;
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				state = (matrix != null && matrix[i][j]) ? GameShipButton.SHIP : GameShipButton.NOT_SHIP; 
				ships[i][j] = new GameShipButton(context, state, i, j, size);
				rows[i].addView(ships[i][j]);
			}
	}
	
	public boolean isNotShip(int x, int y) {
		return ships[x][y].isNotShip();
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				ships[i][j].setOnCheckedChangeListener(listener);
	}
	
	public void setEnabled(boolean enabled) {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				ships[i][j].setEnabled(enabled);
	}
	
	public void setShipLaF(int x, int y, int laf) {
		ships[x][y].setLaF(laf);
	}
	
	public void setShipSunk(boolean[][] matrix) {
		if(matrix != null)
	    	for(int i = 0; i < 10; i++)
				for(int j = 0; j < 10; j++)
					if(matrix[i][j])
						ships[i][j].setSunk();
	}
	
	public ShotResult shoot(int x, int y) {
		if(ships[x][y].shoot())
			return markShipAsSunkIfReallyIs(x, y);
		else
			return new ShotResult(false, false, (boolean[][]) null);
	}
	
	public void shotResult(int x, int y, boolean result) {
		if(result)
			ships[x][y].setHit();
		else
			ships[x][y].setMissed();
	}
    
    public ShotResult markShipAsSunkIfReallyIs(int x, int y) {
    	boolean[][] matrix = new boolean[10][10];

    	for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				matrix[i][j] = true;
    	
    	boolean result = isWholeShipSunkRecursive(x, y, matrix);
    	if(result) {
        	for(int i = 0; i < 10; i++)
    			for(int j = 0; j < 10; j++) {
    				matrix[i][j] = matrix[i][j] ^ true;		// toggling values
    				if(matrix[i][j])
    					ships[i][j].setSunk();
    			}
    	}
    	
    	return new ShotResult(true, result, matrix);
    }
    
    private boolean isWholeShipSunkRecursive(int x, int y, boolean[][] matrix) {
    	
    	boolean result = true;
    	matrix[x][y] = false;

    	if((x+1)<10 && matrix[x+1][y] && shipsMatrix[x+1][y])
    		result = result && ships[x+1][y].isHit() && isWholeShipSunkRecursive(x+1, y, matrix);
    	if((y+1)<10 && matrix[x][y+1] && shipsMatrix[x][y+1])
    		result = result && ships[x][y+1].isHit() && isWholeShipSunkRecursive(x, y+1, matrix);
    	if((x-1)>=0 && matrix[x-1][y] && shipsMatrix[x-1][y])
    		result = result && ships[x-1][y].isHit() && isWholeShipSunkRecursive(x-1, y, matrix);
    	if((y-1)>=0 && matrix[x][y-1] && shipsMatrix[x][y-1])
    		result = result && ships[x][y-1].isHit() && isWholeShipSunkRecursive(x, y-1, matrix);

		return result;
    }
	
	public boolean isGameEnded() {
		boolean ended = true;
		
		for(int i = 0; i < 10 && ended; i++)
			for(int j = 0; j < 10 && ended; j++)
				if(!ships[i][j].isFinished())
					ended = false;
		
		return ended;
	}
	
	public void setShootable(boolean shootable) {
		
		this.setEnabled(shootable);
		if(shootable) {
			for(int i = 0; i < 10; i++)
				for(int j = 0; j < 10; j++)
					if(ships[i][j].isNotShip()) {
						if(!Constants.SHOOTING_TIPS_ENABLED || isFieldShootable(i, j))
							ships[i][j].setLaF(ShipButton.LAF_SHOOTABLE);
						else
							ships[i][j].setLaF(ShipButton.LAF_NOT_SHOOTABLE);
					}
		}
		else {
			for(int i = 0; i < 10; i++)
				for(int j = 0; j < 10; j++)
					if(ships[i][j].isNotShip())
						ships[i][j].setNotShip();
		}
	}
    
    public boolean isFieldShootable(int x, int y) {

    	// CORNER CHECK
    	if((x+1)<10 && (y+1)<10 && (ships[x+1][y+1].isShip() || ships[x+1][y+1].isSunk()))
    		return false;
    	if((x-1)>=0 && (y+1)<10 && (ships[x-1][y+1].isShip() || ships[x-1][y+1].isSunk()))
    		return false;
    	if((x+1)<10 && (y-1)>=0 && (ships[x+1][y-1].isShip() || ships[x+1][y-1].isSunk()))
    		return false;
    	if((x-1)>=0 && (y-1)>=0 && (ships[x-1][y-1].isShip() || ships[x-1][y-1].isSunk()))
    		return false;
    	
    	// SIDES CHECK
    	if((y+1)<10 && (ships[x][y+1].isShip() || ships[x][y+1].isSunk()))
    		return false;
    	if((y-1)>=0 && (ships[x][y-1].isShip() || ships[x][y-1].isSunk()))
    		return false;
    	if((x+1)<10 && (ships[x+1][y].isShip() || ships[x+1][y].isSunk()))
    		return false;
    	if((x-1)>=0 && (ships[x-1][y].isShip() || ships[x-1][y].isSunk()))
    		return false;
    	
    	return true;	
    }
}
