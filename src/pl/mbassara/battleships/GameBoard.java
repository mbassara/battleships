package pl.mbassara.battleships;

import pl.mbassara.battleships.activities.BluetoothActivity;
import pl.mbassara.battleships.bluetooth.BluetoothService;
import pl.mbassara.battleships.bluetooth.GamePacket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GameBoard extends Board
	implements OnCheckedChangeListener{
	
	public static String X = "x";
	public static String Y = "y";
	
	private GameShipButton[][] ships;
	private BluetoothService bluetoothService;
	private final Handler handler = new Handler() {
		public void handleMessage(Message message) {
			int x = message.getData().getInt(X);
			int y = message.getData().getInt(Y);
			ships[x][y].setSunk();
		}
	};

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
				ships[i][j].setOnCheckedChangeListener(this);
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
						
						Bundle bundle = new Bundle();
						bundle.putInt(X, x);
						bundle.putInt(Y, y);
						Message message = new Message();
						message.setData(bundle);
						
						handler.sendMessage(message);
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		GameShipButton button = (GameShipButton) buttonView;
		if(Log.enabled) System.out.println("button (" + button.getFieldX() + "," + button.getFieldY() + ") clicked");
		bluetoothService.send(new GamePacket(button.getFieldX(), button.getFieldY()));
	}

}
