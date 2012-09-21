package pl.mbassara.battleships;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import pl.mbassara.battleships.connections.GamePacket;

public class AIComputer {
	
	private Ship[][] ships;
	private ArrayList<Dim> opponentShips;
	private ArrayList<Dim> currentlyShotShip;
	private Random rand;

	public AIComputer() {
		this.ships = new Ship[10][10];
		boolean[][] matrix = generateMatrix();
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				ships[i][j] = new Ship(matrix[i][j] ? Ship.SHIP : Ship.NOT_SHIP);
		
		this.currentlyShotShip = new ArrayList<Dim>();
		this.opponentShips = new ArrayList<Dim>();
		this.rand = new Random();
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				opponentShips.add(Dim.get(i, j));
	}
	
	public static boolean[][] generateMatrix() {
		if(Global.LOGS_ENABLED) System.out.println("AIComputerBoard.generateMatrix()");
		boolean[][] matrix = new boolean[10][10];
		ArrayList<Dim> fields = new ArrayList<Dim>();
		ArrayList<Dim> tmp = new ArrayList<Dim>();
		
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++) {
				matrix[i][j] = false;
				fields.add(Dim.get(i, j));
			}
		
		Random rand = new Random();
		
		for(int size = Global.SHIPS_COUNER.length-1; size > 1; size--) {
			for(int no = 0; no < Global.SHIPS_COUNER[size]; no++) {
				Collections.shuffle(fields);
				for(Dim dim : fields) {
					boolean possible = true;
					boolean isHorizontal = rand.nextBoolean();
					
					tmp.clear();
					if(isHorizontal) {
						for(int i = 1; i < size; i++)
							if(dim.x+i < 10 && fields.contains(Dim.get(dim.x+i, dim.y)))
								tmp.add(Dim.get(dim.x+i, dim.y));
							else
								possible = false;
						if(!possible) {
							tmp.clear();
							for(int i = 1; i < size; i++)
								if(dim.x-i >= 0 && fields.contains(Dim.get(dim.x-i, dim.y)))
									tmp.add(Dim.get(dim.x-i, dim.y));
								else
									possible = false;
						}
					}
					else {
						for(int i = 1; i < size; i++)
							if(dim.y+i < 10 && fields.contains(Dim.get(dim.x, dim.y+i)))
								tmp.add(Dim.get(dim.x, dim.y+i));
							else
								possible = false;
						if(!possible) {
							tmp.clear();
							for(int i = 1; i < size; i++)
								if(dim.y-i >= 0 && fields.contains(Dim.get(dim.x, dim.y-i)))
									tmp.add(Dim.get(dim.x, dim.y-i));
								else
									possible = false;
						}
					}
					
					if(possible) {
						tmp.add(dim);
						for(Dim tmpDim : tmp) {
							matrix[tmpDim.x][tmpDim.y] = true; 
							fields.remove(tmpDim);
							fields.remove(Dim.get(tmpDim.x-1, tmpDim.y-1));
							fields.remove(Dim.get(tmpDim.x, tmpDim.y-1));
							fields.remove(Dim.get(tmpDim.x+1, tmpDim.y-1));
							fields.remove(Dim.get(tmpDim.x+1, tmpDim.y));
							fields.remove(Dim.get(tmpDim.x+1, tmpDim.y+1));
							fields.remove(Dim.get(tmpDim.x, tmpDim.y+1));
							fields.remove(Dim.get(tmpDim.x-1, tmpDim.y+1));
							fields.remove(Dim.get(tmpDim.x-1, tmpDim.y));
						}
						break;	// stop searching
					}
				}
			}
		}
		
		return matrix;
	}
	
	public GamePacket doShot() {
				
		if(Global.LOGS_ENABLED) System.out.println("currentlyShotShips:\n" + currentlyShotShip);
		
		Dim dim = null;
		try {
			Thread.sleep(1000);		// computer is thinking
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(currentlyShotShip.isEmpty()) {
			try {
				Thread.sleep((new Random()).nextInt(3000));		// computer is thinking longer
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			dim = opponentShips.get(rand.nextInt(opponentShips.size()));
		}
		else if(currentlyShotShip.size() == 1) {
			Dim tmpDim = currentlyShotShip.get(0);
			
			if(opponentShips.contains(Dim.get(tmpDim.x+1, tmpDim.y)))
				dim = Dim.get(tmpDim.x+1, tmpDim.y);
			else if(opponentShips.contains(Dim.get(tmpDim.x, tmpDim.y+1)))
				dim = Dim.get(tmpDim.x, tmpDim.y+1);
			else if(opponentShips.contains(Dim.get(tmpDim.x-1, tmpDim.y)))
				dim = Dim.get(tmpDim.x-1, tmpDim.y);
			else if(opponentShips.contains(Dim.get(tmpDim.x, tmpDim.y-1)))
				dim = Dim.get(tmpDim.x, tmpDim.y-1);
		}
		else {
			Dim dim1 = currentlyShotShip.get(0);
			Dim dim2 = currentlyShotShip.get(1);
			
			boolean isHorizontal = (dim1.x == dim2.x);
			
			if(isHorizontal) {
				for(int i = dim1.y; i < 10; i++) {
					if(!currentlyShotShip.contains(Dim.get(dim1.x, i)) && opponentShips.contains(Dim.get(dim1.x, i))) {
						dim = Dim.get(dim1.x, i);
						break;
					}
					else if(!currentlyShotShip.contains(Dim.get(dim1.x, i)) && !opponentShips.contains(Dim.get(dim1.x, i)))
						break;
				}
				
				if(dim == null)
					for(int i = dim1.y; i >= 0; i--) {
						if(!currentlyShotShip.contains(Dim.get(dim1.x, i)) && opponentShips.contains(Dim.get(dim1.x, i))) {
							dim = Dim.get(dim1.x, i);
							break;
						}
						else if(!currentlyShotShip.contains(Dim.get(dim1.x, i)) && !opponentShips.contains(Dim.get(dim1.x, i)))
							break;
					}
			}
			else {
				for(int i = dim1.x; i < 10; i++) {
					if(!currentlyShotShip.contains(Dim.get(i, dim1.y)) && opponentShips.contains(Dim.get(i, dim1.y))) {
						dim = Dim.get(i, dim1.y);
						break;
					}
					else if(!currentlyShotShip.contains(Dim.get(i, dim1.y)) && !opponentShips.contains(Dim.get(i, dim1.y)))
						break;
				}
				
				if(dim == null)
					for(int i = dim1.x; i >= 0; i--) {
						if(!currentlyShotShip.contains(Dim.get(i, dim1.y)) && opponentShips.contains(Dim.get(i, dim1.y))) {
							dim = Dim.get(i, dim1.y);
							break;
						}
						else if(!currentlyShotShip.contains(Dim.get(i, dim1.y)) && !opponentShips.contains(Dim.get(i, dim1.y)))
							break;
					}
			}
		}
		
		int x = dim.x;
		int y = dim.y;
		if(Global.LOGS_ENABLED) System.out.println("shot - field: " + x + "," + y);
		
		return new GamePacket(x, y);
	}
	
	public void receiveResult(ShotResult result) {
		if(result.isSunk()) {
			currentlyShotShip.clear();
			boolean[][] matrix = result.getMatrix();
			for(int i = 0; i < 10; i++)
				for(int j = 0; j < 10; j++)
					if(matrix[i][j]) {
						opponentShips.remove(Dim.get(i, j));
						opponentShips.remove(Dim.get(i-1, j-1));
						opponentShips.remove(Dim.get(i, j-1));
						opponentShips.remove(Dim.get(i+1, j-1));
						opponentShips.remove(Dim.get(i+1, j));
						opponentShips.remove(Dim.get(i+1, j+1));
						opponentShips.remove(Dim.get(i, j+1));
						opponentShips.remove(Dim.get(i-1, j+1));
						opponentShips.remove(Dim.get(i-1, j));
					}
		}
		else {
			int x = result.getCoordinates().getX();
			int y = result.getCoordinates().getY();
			
			if(result.isHit())
				currentlyShotShip.add(Dim.get(x, y));
			
			opponentShips.remove(Dim.get(x, y));
		}
	}
	
	public ShotResult receiveShot(int x, int y) {
		
		ShotResult result;
		if(ships[x][y].isShip()) {
			ships[x][y].setHit();
			result = markShipAsSunkIfReallyIs(x, y);
		}
		else {
			result = new ShotResult(false, false, isGameEnded(), new Coordinates(x, y), (boolean[][]) null);
			if(!ships[x][y].isShip())
				ships[x][y].setMissed();
		}

		if(Global.LOGS_ENABLED) {
	    	for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++)
					System.out.print("  " + ships[i][j]);
				System.out.println();
	    	}
			System.out.println();
		}
		
		return result;
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
    	
    	return new ShotResult(true, result, isGameEnded(), new Coordinates(x, y), matrix);
    }
    
    private boolean isWholeShipSunkRecursive(int x, int y, boolean[][] matrix) {
    	
    	boolean result = true;
    	matrix[x][y] = false;

    	if((x+1)<10 && matrix[x+1][y] && ships[x+1][y].isShip())
    		result = result && ships[x+1][y].isHit() && isWholeShipSunkRecursive(x+1, y, matrix);
    	if((y+1)<10 && matrix[x][y+1] && ships[x][y+1].isShip())
    		result = result && ships[x][y+1].isHit() && isWholeShipSunkRecursive(x, y+1, matrix);
    	if((x-1)>=0 && matrix[x-1][y] && ships[x-1][y].isShip())
    		result = result && ships[x-1][y].isHit() && isWholeShipSunkRecursive(x-1, y, matrix);
    	if((y-1)>=0 && matrix[x][y-1] && ships[x][y-1].isShip())
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

}

class Ship {
	public static final int NOT_SHIP = 0;
	public static final int SHIP = 1;
	public static final int HIT = 2;
	public static final int SUNK = 3;
	public static final int MISSED = 4;
	private int state = NOT_SHIP;
	
	public Ship(int state) {
		this.state = state;
	}
	
	public void setHit() {
		state = HIT;
	}
	
	public void setSunk() {
		state = SUNK;
	}
	
	public void setMissed() {
		state = MISSED;
	}
	
	public boolean isFinished() {	// sunk, missed or not ship
		return state == SUNK || state == MISSED || state == NOT_SHIP;
	}
	
	public boolean isShip() {
		return state != NOT_SHIP && state != MISSED;
	}
	
	public boolean isNotHit() {
		return state != HIT  && state != SUNK;
	}
	
	public boolean isHit() {
		return state == HIT;
	}
	
	public boolean isSunk() {
		return state == SUNK;
	}
	
	@Override
	public String toString() {
		String result = "·";
		switch (state) {
		case SHIP:
			result = "S";
			break;
		case SUNK:
			result = "X";
			break;
		case HIT:
			result = "H";
			break;
		case MISSED:
			result = "M";
			break;
		}
		
		return result;
	}
}

class Dim {
	public int x;
	public int y;
	private static Map<Integer, Dim> map = new TreeMap<Integer, Dim>();
	
	public static Dim get(int x, int y) {
		if(!map.containsKey(x + y * 10))
			map.put(x + y * 10, new Dim(x, y));
			
		return map.get(x + y * 10);
	}
	
	private Dim(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}