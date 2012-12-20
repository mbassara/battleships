package pl.mbassara.battleships.activities;

import java.util.Random;

import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.GameResult;
import pl.mbassara.battleships.GameShipButton;
import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.ScoreBoard;
import pl.mbassara.battleships.ShipButton;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.activities.connections.bluetooth.BluetoothActivity;
import pl.mbassara.battleships.activities.connections.wifi.WiFiActivity;
import pl.mbassara.battleships.connections.GamePacket;
import pl.mbassara.battleships.connections.RemoteService;
import pl.mbassara.battleships.connections.local.LocalService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity
	implements OnCheckedChangeListener {
	
	private GameBoard mainBoard;
	private GameBoard previewBoard;
	private RemoteService remoteService;
	private boolean meStartFirst = true;
	private String gameMode;
	private GameShipButton currentTarget = null;
	private boolean[][] matrix = null;
	private ScoreBoard scoreBoard;
	private final GameMessagesHandler handler = new GameMessagesHandler();
	private final Context currentContext = this;
	
	private Toast youStartToast;
	private Toast opponentStartToast;
	private Toast waitingForOpponentToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if(Global.GAME_MODE.equals(Global.GAME_MODE_BT)) {
			remoteService = BluetoothActivity.getRemoteService();
	        gameMode = GameModeActivity.getMode();
		}
		else if(Global.GAME_MODE.equals(Global.GAME_MODE_WIFI)) {
			remoteService = WiFiActivity.getRemoteService();
	        gameMode = GameModeActivity.getMode();
		}
		else if(Global.GAME_MODE.equals(Global.GAME_MODE_SINGLE)) {
			remoteService = new LocalService();
			remoteService.connect();
	        gameMode = Global.GAME_MODE_HOST;
		}
		else
			this.finish();
		
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game_layout);
        previewBoard = new GameBoard(this, CreatingShipsActivity.getBoardMatrix(), GameBoard.SIZE_SMALL);
        mainBoard = new GameBoard(this, null, GameBoard.SIZE_BIG);
        mainBoard.setOnCheckedChangeListener(this);

        TextView playerScore = (TextView) findViewById(R.id.yourScoreTextView);
        TextView oppScore = (TextView) findViewById(R.id.opponentScoreTextView);
        scoreBoard = new ScoreBoard(this, playerScore, oppScore);
        scoreBoard.setLocalPlayersName(Global.USERS_NAME);
        remoteService.send(new GamePacket(GamePacket.TYPE_USER_NAME, Global.USERS_NAME));
        
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
        
    	youStartToast = Toast.makeText(currentContext, getString(R.string.you_start), Toast.LENGTH_SHORT);
    	opponentStartToast = Toast.makeText(currentContext, getString(R.string.opponent_starts), Toast.LENGTH_SHORT);
    	waitingForOpponentToast = Toast.makeText(currentContext, getString(R.string.wait_for_opponent), Toast.LENGTH_SHORT);

        if(gameMode == Global.GAME_MODE_HOST) {
        	boolean whoStarts = (new Random(System.currentTimeMillis())).nextBoolean();
        	meStartFirst = whoStarts == Global.HOST_FIRST;
  	        GamePacket packet = new GamePacket(whoStarts);
  	        remoteService.send(packet);
  	        
  	        mainBoard.setShootable(meStartFirst);
  	        if(meStartFirst)
  	        	youStartToast.show();
  	        else
  	        	opponentStartToast.show();
        }
        else {
        	mainBoard.setShootable(false);
        	waitingForOpponentToast.show();
        }
        
        receivingThread.start();
    }

	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		GameShipButton button = (GameShipButton) buttonView;
		if(!button.isTarget()) {
			int x = button.getFieldX();
			int y = button.getFieldY();
			if(button.isNotShip()) {
				if(currentTarget != null)
					currentTarget.setTarget(false);
				
				if(Global.LOGS_ENABLED) System.out.println("button (" + x + "," + y + ") clicked");
				currentTarget = button;
				button.setTarget(true);
			}
		}
		else if(currentTarget != null) {	// shot
			button.setTarget(false);
			button.setLaF(ShipButton.LAF_SHOT);
			mainBoard.setEnabled(false);
			remoteService.send(new GamePacket(currentTarget.getFieldX(), currentTarget.getFieldY()));
		}
	}
	
	private void endGame(boolean result) {
		Intent intent = new Intent(this, MainMenu.class);
		intent.putExtra(Global.KEY_GAME_RESULT, result ? Global.GAME_RESULT_WINNER : Global.GAME_RESULT_LOOSER);
		
		startActivity(intent);
		this.finish();
	}
	
	private Thread receivingThread = new Thread(new Runnable() {

		public void run() {
			GamePacket packet;
			while (true) {
				packet = remoteService.receive();
				if(packet != null) {
					if(Global.LOGS_ENABLED) System.out.println("packet received, size = " + Global.objectToByteArray(packet).length);
					Bundle bundle = new Bundle();
					Message message = new Message();
					
					if(packet.getType() == GamePacket.TYPE_WHO_STARTS) {
						bundle.putInt(Global.GameMessagesHandler_KEY_TYPE, Global.GameMessagesHandler_TYPE_WHO_STARTS);
						bundle.putBoolean(Global.GameMessagesHandler_KEY_WHO_STARTS, packet.getWhoStarts());
					}
					else if (packet.getType() == GamePacket.TYPE_USER_NAME) {
						bundle.putInt(Global.GameMessagesHandler_KEY_TYPE, Global.GameMessagesHandler_TYPE_USERS_NAME);
						bundle.putString(Global.GameMessagesHandler_KEY_USERS_NAME, packet.getUserName());
					}
					else if(packet.getType() == GamePacket.TYPE_SHOT) {
						int x = packet.getCoordinates().getX();
						int y = packet.getCoordinates().getY();
						if(Global.LOGS_ENABLED) System.out.println("shot - field: " + x + "," + y);
						
						bundle.putInt(Global.GameMessagesHandler_KEY_TYPE, Global.GameMessagesHandler_TYPE_SHOT);
						bundle.putInt(Global.GameMessagesHandler_KEY_X, x);
						bundle.putInt(Global.GameMessagesHandler_KEY_Y, y);
					}
					else if(packet.getType() == GamePacket.TYPE_RESULT) {
						bundle.putInt(Global.GameMessagesHandler_KEY_TYPE, Global.GameMessagesHandler_TYPE_RESULT);
						bundle.putBoolean(Global.GameMessagesHandler_KEY_RESULT_IS_HIT, packet.getShotResult().isHit());
						bundle.putBoolean(Global.GameMessagesHandler_KEY_RESULT_IS_SUNK, packet.getShotResult().isSunk());
						matrix = packet.getShotResult().getMatrix();
						if(Global.LOGS_ENABLED) System.out.println("shot result received");
					}
					else if(packet.getType() == GamePacket.TYPE_GAME_RESULT) {
						endGame(packet.getGameResult().isWinner());
						if(Global.LOGS_ENABLED) System.out.println("game result received");
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
		
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if(data.getInt(Global.GameMessagesHandler_KEY_TYPE) == Global.GameMessagesHandler_TYPE_WHO_STARTS) {
				waitingForOpponentToast.cancel();
	        	meStartFirst = data.getBoolean(Global.GameMessagesHandler_KEY_WHO_STARTS) == Global.CLIENT_FIRST;
	            mainBoard.setShootable(meStartFirst);
	            if(meStartFirst)
	            	youStartToast.show();
	            else
	            	opponentStartToast.show();
			}
			else if(data.getInt(Global.GameMessagesHandler_KEY_TYPE) == Global.GameMessagesHandler_TYPE_USERS_NAME) {
				scoreBoard.setRemotePlayersName(data.getString(Global.GameMessagesHandler_KEY_USERS_NAME));
			}
			else if(data.getInt(Global.GameMessagesHandler_KEY_TYPE) == Global.GameMessagesHandler_TYPE_SHOT) {
				if(Global.LOGS_ENABLED) System.out.println("shot info handled");
				int x = msg.getData().getInt(Global.GameMessagesHandler_KEY_X);
				int y = msg.getData().getInt(Global.GameMessagesHandler_KEY_Y);
				ShotResult result = previewBoard.shoot(x, y);
				if(previewBoard.isGameEnded()) {
					remoteService.send(new GamePacket(new GameResult(Global.GAME_RESULT_WINNER)));	// opponent is winner because he sunk all of my ships
					endGame(false);
				}
				else {
					remoteService.send(new GamePacket(result));
					if(!result.isHit() || result.isSunk())
						mainBoard.setShootable(true);
					if(result.isSunk())
						scoreBoard.remotePlayerScoreUp();
				}
			}
			else if(data.getInt(Global.GameMessagesHandler_KEY_TYPE) == Global.GameMessagesHandler_TYPE_RESULT) {
				if(Global.LOGS_ENABLED) System.out.println("shot result info handled");
				boolean result = data.getBoolean(Global.GameMessagesHandler_KEY_RESULT_IS_HIT);
				mainBoard.shotResult(currentTarget.getFieldX(), currentTarget.getFieldY(), result);
				mainBoard.setShootable(result && !data.getBoolean(Global.GameMessagesHandler_KEY_RESULT_IS_SUNK));
				
				currentTarget = null;
				if(data.getBoolean(Global.GameMessagesHandler_KEY_RESULT_IS_SUNK)) {
					if(Global.LOGS_ENABLED) System.out.println("ship is sunked");
					scoreBoard.localPlayerScoreUp();
					mainBoard.setShipSunk(matrix);
				}
			}
		}
	};
    
}
