package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.ArrayUtilities;
import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.GameShipButton;
import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.ScoreBoard;
import pl.mbassara.battleships.ShipButton.LafType;
import pl.mbassara.battleships.ShipButton.Size;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.connections.GamePacket;
import pl.mbassara.battleships.connections.RemoteService;
import pl.mbassara.battleships.connections.local.LocalService;
import pl.mbassara.battleships.enums.GamePacketType;
import pl.mbassara.battleships.enums.GameResult;
import pl.mbassara.battleships.enums.MultiplayerGameMode;
import pl.mbassara.battleships.enums.WhoStarts;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnCheckedChangeListener {

	private Global global = Global.getInstance();
	private GameBoard mainBoard;
	private GameBoard previewBoard;
	private RemoteService remoteService;
	private boolean meStartFirst = true;
	private MultiplayerGameMode gameMode;
	private GameShipButton currentTarget = null;
	private boolean[][] matrix = null;
	private ScoreBoard scoreBoard;
	private final GameMessagesHandler handler = new GameMessagesHandler();
	private final Context currentContext = this;

	private boolean isGameInProgress = false;

	private Toast youStartToast;
	private Toast opponentStartToast;
	private Toast waitingForOpponentToast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);

		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		switch (global.GAME_MODE) {
		case BLUETOOTH:
			remoteService = global.remoteService;
			gameMode = global.MULTIPLAYER_GAME_MODE;
			break;
		case WIFI:
			remoteService = global.remoteService;
			gameMode = global.MULTIPLAYER_GAME_MODE;
			break;
		case SINGLE:
			remoteService = new LocalService(this);
			remoteService.connect();
			gameMode = MultiplayerGameMode.HOST;
			break;

		default:
			this.finish();
			break;
		}

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game_layout);
		previewBoard = new GameBoard(this, global.getLocalUserBoardMatrix(), Size.SMALL);
		mainBoard = new GameBoard(this, null, Size.BIG);
		mainBoard.setOnCheckedChangeListener(this);

		TextView playerScore = (TextView) findViewById(R.id.yourScoreTextView);
		TextView oppScore = (TextView) findViewById(R.id.opponentScoreTextView);
		scoreBoard = new ScoreBoard(this, playerScore, oppScore);
		scoreBoard.setLocalPlayersName(global.USER_NAME);
		remoteService.send(new GamePacket(GamePacketType.USER_NAME, global.USER_NAME));

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

		if (gameMode == MultiplayerGameMode.HOST) {
			WhoStarts whoStarts = WhoStarts.getRandom();
			meStartFirst = whoStarts == WhoStarts.HOST_STARTS;
			GamePacket packet = new GamePacket(whoStarts);
			remoteService.send(packet);

			mainBoard.setShootable(meStartFirst);
			if (meStartFirst)
				youStartToast.show();
			else
				opponentStartToast.show();
		} else {
			mainBoard.setShootable(false);
			waitingForOpponentToast.show();
		}

		receivingThread.start();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		GameShipButton button = (GameShipButton) buttonView;
		if (!button.isTarget()) {
			int x = button.getFieldX();
			int y = button.getFieldY();
			if (button.isNotShip()) {
				if (currentTarget != null)
					currentTarget.setTarget(false);

				if (global.LOGS_ENABLED)
					System.out.println("button (" + x + "," + y + ") clicked");
				currentTarget = button;
				button.setTarget(true);
			}
		} else if (currentTarget != null) { // shot
			button.setTarget(false);
			button.setLaF(LafType.SHOT);
			mainBoard.setEnabled(false);
			remoteService.send(new GamePacket(currentTarget.getFieldX(), currentTarget.getFieldY()));
		}
	}

	private void endGame(GameResult result) {

		switch (result) {
		case WINNER:
			Toast.makeText(this, getString(R.string.result_winner), Toast.LENGTH_LONG).show();
			break;
		case LOOSER:
			Toast.makeText(this, getString(R.string.result_looser), Toast.LENGTH_LONG).show();
			break;
		case OPPONENT_DISCONNECTED:
			Toast.makeText(this, getString(R.string.opponent_disconnected), Toast.LENGTH_LONG).show();
			break;
		case ABORTED:
			remoteService.send(new GamePacket(GamePacketType.OPPONENT_DISCONNECTED, null));
			break;
		default:
			break;
		}

		isGameInProgress = false;
		remoteService.stop();

		this.finish();
	}

	private Thread receivingThread = new Thread(new Runnable() {

		public void run() {
			GamePacket packet;
			isGameInProgress = true;
			while (isGameInProgress) {
				packet = remoteService.receive();
				if (packet != null) {
					if (global.LOGS_ENABLED)
						System.out.println("packet received, size = " + ArrayUtilities.objectToByteArray(packet).length);
					Bundle bundle = new Bundle();
					Message message = new Message();

					if (packet.getType() == GamePacketType.WHO_STARTS) {
						bundle.putInt(HandlerKey.TYPE.name(), GamePacketType.WHO_STARTS.ordinal());
						bundle.putInt(HandlerKey.WHO_STARTS.name(), packet.getWhoStarts().ordinal());
					} else if (packet.getType() == GamePacketType.USER_NAME) {
						bundle.putInt(HandlerKey.TYPE.name(), GamePacketType.USER_NAME.ordinal());
						bundle.putString(HandlerKey.USER_NAME.name(), packet.getUserName());
					} else if (packet.getType() == GamePacketType.SHOT) {
						int x = packet.getCoordinates().getX();
						int y = packet.getCoordinates().getY();
						if (global.LOGS_ENABLED)
							System.out.println("shot - field: " + x + "," + y);

						bundle.putInt(HandlerKey.TYPE.name(), GamePacketType.SHOT.ordinal());
						bundle.putInt(HandlerKey.X.name(), x);
						bundle.putInt(HandlerKey.Y.name(), y);
					} else if (packet.getType() == GamePacketType.RESULT) {
						bundle.putInt(HandlerKey.TYPE.name(), GamePacketType.RESULT.ordinal());
						bundle.putBoolean(HandlerKey.RESULT_IS_HIT.name(), packet.getShotResult().isHit());
						bundle.putBoolean(HandlerKey.RESULT_IS_SUNK.name(), packet.getShotResult().isSunk());
						matrix = packet.getShotResult().getMatrix();
						if (global.LOGS_ENABLED)
							System.out.println("shot result received");
					} else if (packet.getType() == GamePacketType.GAME_RESULT) {
						bundle.putInt(HandlerKey.TYPE.name(), GamePacketType.GAME_RESULT.ordinal());
						bundle.putInt(HandlerKey.GAME_RESULT.name(), packet.getGameResult().ordinal());
						if (global.LOGS_ENABLED)
							System.out.println("game result received");
					} else if (packet.getType() == GamePacketType.OPPONENT_DISCONNECTED) {
						bundle.putInt(HandlerKey.TYPE.name(), GamePacketType.OPPONENT_DISCONNECTED.ordinal());
						if (global.LOGS_ENABLED)
							System.out.println("OPPONENT_DISCONNECTED info received");
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
			if (data.getInt(HandlerKey.TYPE.name()) == GamePacketType.WHO_STARTS.ordinal()) {
				waitingForOpponentToast.cancel();
				meStartFirst = data.getInt(HandlerKey.WHO_STARTS.name()) == WhoStarts.CLIENT_STARTS.ordinal();
				mainBoard.setShootable(meStartFirst);
				if (meStartFirst)
					youStartToast.show();
				else
					opponentStartToast.show();
			} else if (data.getInt(HandlerKey.TYPE.name()) == GamePacketType.USER_NAME.ordinal()) {
				scoreBoard.setRemotePlayersName(data.getString(HandlerKey.USER_NAME.name()));
			} else if (data.getInt(HandlerKey.TYPE.name()) == GamePacketType.GAME_RESULT.ordinal()) {
				endGame(GameResult.values()[data.getInt(HandlerKey.GAME_RESULT.name())]);
			} else if (data.getInt(HandlerKey.TYPE.name()) == GamePacketType.SHOT.ordinal()) {
				if (global.LOGS_ENABLED)
					System.out.println("shot info handled");
				int x = msg.getData().getInt(HandlerKey.X.name());
				int y = msg.getData().getInt(HandlerKey.Y.name());
				ShotResult result = previewBoard.shoot(x, y);
				if (previewBoard.isGameEnded()) {
					remoteService.send(new GamePacket(GameResult.WINNER)); // opponent is winner because he sunk all of my ships
					endGame(GameResult.LOOSER);
				} else {
					remoteService.send(new GamePacket(result));
					if (!result.isHit() || result.isSunk())
						mainBoard.setShootable(true);
					if (result.isSunk())
						scoreBoard.remotePlayerScoreUp();
				}
			} else if (data.getInt(HandlerKey.TYPE.name()) == GamePacketType.RESULT.ordinal()) {
				if (global.LOGS_ENABLED)
					System.out.println("shot result info handled");
				boolean result = data.getBoolean(HandlerKey.RESULT_IS_HIT.name());
				mainBoard.shotResult(currentTarget.getFieldX(), currentTarget.getFieldY(), result);
				mainBoard.setShootable(result && !data.getBoolean(HandlerKey.RESULT_IS_SUNK.name()));

				currentTarget = null;
				if (data.getBoolean(HandlerKey.RESULT_IS_SUNK.name())) {
					if (global.LOGS_ENABLED)
						System.out.println("ship is sunked");
					scoreBoard.localPlayerScoreUp();
					mainBoard.setShipSunk(matrix);
				}
			} else if (data.getInt(HandlerKey.TYPE.name()) == GamePacketType.OPPONENT_DISCONNECTED.ordinal()) {
				endGame(GameResult.OPPONENT_DISCONNECTED);
			}
		}
	};

	private enum HandlerKey {
		X, Y, RESULT_IS_HIT, RESULT_IS_SUNK, RESULT_IS_GAME_ENDED, TYPE, SHOOT_BUTTON_SET_ENABLED, WHO_STARTS, GAME_RESULT, USER_NAME
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage(R.string.sure_wanna_quit)
				.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						endGame(GameResult.ABORTED);
					}
				}).setNegativeButton(R.string.negative_button, null).show();
	}

}
