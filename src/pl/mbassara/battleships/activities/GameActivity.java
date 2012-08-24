package pl.mbassara.battleships.activities;

import java.util.Random;

import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.GameResult;
import pl.mbassara.battleships.GameShipButton;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.ShipButton;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.Vibra;
import pl.mbassara.battleships.activities.CreatingShipsActivity;
import pl.mbassara.battleships.connections.bluetooth.BluetoothService;
import pl.mbassara.battleships.connections.bluetooth.GamePacket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ToggleButton;

public class GameActivity extends Activity
	implements OnCheckedChangeListener {
	
	public static final String KEY_GAME_RESULT = "gameReslut"; 
	
	private GameBoard mainBoard;
	private GameBoard previewBoard;
	private BluetoothService bluetoothService;
	private boolean meStartFirst = true;
	private int gameMode;
	private ToggleButton shotButton;
	private Coordinates field = null;
	private boolean[][] matrix = null;
	private Vibra vibra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

		this.vibra = Vibra.getInstance(this);
        bluetoothService = BluetoothActivity.getBluetoothService();
        gameMode = GameModeActivity.getMode();

      if(gameMode == GameModeActivity.HOST_MODE) {
      	boolean whoStarts = (new Random(System.currentTimeMillis())).nextBoolean();
      	meStartFirst = whoStarts == GamePacket.HOST_FIRST;
	        GamePacket packet = new GamePacket(whoStarts);
	        bluetoothService.send(packet);
      }
      else {
      	GamePacket packet = null;
      	while(packet == null) {		// TODO: Screen freezes while waiting for host to start game!
      		packet = bluetoothService.receive();
      		try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
      		
      		if(packet != null && (packet.getType() != GamePacket.TYPE_WHO_STARTS))
      			packet = null;
      	}
      	meStartFirst = packet.getWhoStarts() == GamePacket.CLIENT_FIRST;
      }
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game_layout);
        shotButton = (ToggleButton) findViewById(R.id.shotButton);
        shotButton.setOnCheckedChangeListener(this);
        previewBoard = new GameBoard(this, CreatingShipsActivity.getBoardMatrix(), GameBoard.SIZE_SMALL);
        previewBoard.setEnabled(false);
        mainBoard = new GameBoard(this, null, GameBoard.SIZE_BIG);
        mainBoard.setOnCheckedChangeListener(this);
        
        LayoutParams previewBoardParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        previewBoardParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        previewBoardParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        previewBoard.setLayoutParams(previewBoardParams);
        previewBoard.setEnabled(false);
        
        LayoutParams mainBoardParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mainBoardParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mainBoardParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mainBoard.setLayoutParams(mainBoardParams);
        mainBoard.setEnabled(false);
        
        layout.addView(previewBoard);
        layout.addView(mainBoard);
        
        shotButton.setEnabled(meStartFirst);
        if(meStartFirst)
        	Toast.makeText(this, getString(R.string.you_start), Toast.LENGTH_SHORT).show();
        else
        	Toast.makeText(this, getString(R.string.opponent_starts), Toast.LENGTH_SHORT).show();
        
        receivingThread.start();
    }

	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		vibra.beep();
		if(buttonView.equals(shotButton)) {
			mainBoard.setEnabled(isChecked);
			if(!isChecked && field != null) {	// shot
				bluetoothService.send(new GamePacket(field));
				buttonView.setEnabled(false);
			}
		}
		else {
			if(field != null) {
				mainBoard.setShipLaF(field.getX(), field.getY(), GameShipButton.LAF_NORMAL);
			}
			GameShipButton button = (GameShipButton) buttonView;
			int x = button.getFieldX();
			int y = button.getFieldY();
			if(mainBoard.isNotShip(x, y)) {
				if(Constants.LOGS_ENABLED) System.out.println("button (" + x + "," + y + ") clicked");
				field = new Coordinates(x, y);
				button.setLaF(ShipButton.LAF_POSSIBLE);
			}
		}
	}
	
	private void endGame(boolean result) {
		Intent intent = new Intent(this, MainMenu.class);
		intent.putExtra(Constants.KEY_GAME_RESULT, result ? Constants.GAME_RESULT_WINNER : Constants.GAME_RESULT_LOOSER);
		
		startActivity(intent);
		this.finish();
	}
	
	private Thread receivingThread = new Thread(new Runnable() {
		private GameMessagesHandler handler = new GameMessagesHandler();
		
		
		public void run() {
			GamePacket packet;
			while (true) {
				packet = bluetoothService.receive();
				if(packet != null) {
					if(Constants.LOGS_ENABLED) System.out.println("packet received");
					Bundle bundle = new Bundle();
					Message message = new Message();
					
					if(packet.getType() == GamePacket.TYPE_SHOT) {
						int x = packet.getCoordinates().getX();
						int y = packet.getCoordinates().getY();
						if(Constants.LOGS_ENABLED) System.out.println("shot - field: " + x + "," + y);
						
						bundle.putInt(GameMessagesHandler.KEY_TYPE, GameMessagesHandler.TYPE_SHOT);
						bundle.putInt(GameMessagesHandler.KEY_X, x);
						bundle.putInt(GameMessagesHandler.KEY_Y, y);
					}
					else if(packet.getType() == GamePacket.TYPE_RESULT) {
						bundle.putInt(GameMessagesHandler.KEY_TYPE, GameMessagesHandler.TYPE_RESULT);
						bundle.putBoolean(GameMessagesHandler.KEY_RESULT_IS_HIT, packet.getShotResult().isHit());
						bundle.putBoolean(GameMessagesHandler.KEY_RESULT_IS_SUNK, packet.getShotResult().isSunk());
						matrix = packet.getShotResult().getMatrix();
						if(Constants.LOGS_ENABLED) System.out.println("shot result received");
					}
					else if(packet.getType() == GamePacket.TYPE_GAME_RESULT) {
						endGame(packet.getGameResult().isWinner());
						if(Constants.LOGS_ENABLED) System.out.println("game result received");
					}

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
	
	@SuppressLint("HandlerLeak")
	class GameMessagesHandler extends Handler {
		public static final String KEY_X = "x";
		public static final String KEY_Y = "y";
		public static final String KEY_RESULT_IS_HIT = "isHit";
		public static final String KEY_RESULT_IS_SUNK = "isSunk";
		public static final String KEY_TYPE = "type";
		public static final int TYPE_SHOT = 1;
		public static final int TYPE_RESULT = 2;
		
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if(data.getInt(KEY_TYPE) == TYPE_SHOT) {
				if(Constants.LOGS_ENABLED) System.out.println("shot info handled");
				int x = msg.getData().getInt(KEY_X);
				int y = msg.getData().getInt(KEY_Y);
				ShotResult result = previewBoard.shoot(x, y);
				if(previewBoard.isGameEnded()) {
					bluetoothService.send(new GamePacket(new GameResult(GameResult.RESULT_WINNER)));	// opponent is winner because he sunk all of my ships
					endGame(false);
				}
				else {
					bluetoothService.send(new GamePacket(result));
					if(!result.isHit() || result.isSunk())
						shotButton.setEnabled(true);
				}
			}
			else if(data.getInt(KEY_TYPE) == TYPE_RESULT) {
				if(Constants.LOGS_ENABLED) System.out.println("shot result info handled");
				boolean result = data.getBoolean(KEY_RESULT_IS_HIT);
				mainBoard.shotResult(field.getX(), field.getY(), result);
				shotButton.setEnabled(result && !data.getBoolean(KEY_RESULT_IS_SUNK));
				field = null;
				if(data.getBoolean(KEY_RESULT_IS_SUNK)) {
					if(Constants.LOGS_ENABLED) System.out.println("ship is sunked");
					mainBoard.setShipSunk(matrix);
				}
			}
		}
	};
    
}
