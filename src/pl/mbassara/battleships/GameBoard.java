package pl.mbassara.battleships;

import pl.mbassara.battleships.activities.BluetoothActivity;
import pl.mbassara.battleships.activities.GameActivity;
import pl.mbassara.battleships.bluetooth.BluetoothService;
import pl.mbassara.battleships.bluetooth.GamePacket;
import android.content.Context;

public class GameBoard extends Board {
	
	private GameShipButton[][] ships;
	private BluetoothService bluetoothService;

	public GameBoard(Context context) {
		this(context, null);
	}
	
	public GameBoard(Context context, boolean[][] matrix) {
		super(context);
		
		this.ships = new GameShipButton[10][10];
		int state;
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				state = matrix[i][j] ? GameShipButton.SHIP : GameShipButton.NOT_SHIP; 
				ships[i][j] = new GameShipButton(context, state, i, j);
				ships[i][j].setOnCheckedChangeListener((GameActivity) context);
				rows[i].addView(ships[i][j]);
			}
		
		bluetoothService = BluetoothActivity.getBluetoothService();
        
        Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				GamePacket packet;
				while (true) {
					packet = bluetoothService.receive();
					if(packet != null) {
						int x = packet.getCoordinates().getX();
						int y = packet.getCoordinates().getY();
						System.out.println("X = " + x);
						System.out.println("Y = " + y);
						ships[x][y].setSunk();					// TODO: nie można zmieniać Views z innego wątku niż ten który je stworzył
					}
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
        
        thread.start();
	}

}
