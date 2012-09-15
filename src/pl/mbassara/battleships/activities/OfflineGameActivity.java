package pl.mbassara.battleships.activities;

import java.util.Random;

import pl.mbassara.battleships.AIComputer;
import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.GameShipButton;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.ScoreBoard;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.activities.CreatingShipsActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class OfflineGameActivity extends Activity
	implements OnCheckedChangeListener {
	
	private GameBoard mainBoard;
	private GameBoard previewBoard;
	private AIComputer aiComputer;
	private boolean meStartFirst = true;
	private GameShipButton currentTarget = null;
	private boolean[][] matrix = null;
	private GameMessagesHandler handler;
	private ScoreBoard scoreBoard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_game);
        handler = new GameMessagesHandler();
        
        meStartFirst = (new Random(System.currentTimeMillis())).nextBoolean();
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_offline_game_layout);
        previewBoard = new GameBoard(this, CreatingShipsActivity.getBoardMatrix(), GameBoard.SIZE_SMALL);
        mainBoard = new GameBoard(this, null, GameBoard.SIZE_BIG);
        mainBoard.setOnCheckedChangeListener(this);
        aiComputer = new AIComputer(handler);

        TextView playerScore = (TextView) findViewById(R.id.yourScoreTextView);
        TextView oppScore = (TextView) findViewById(R.id.opponentScoreTextView);
        scoreBoard = new ScoreBoard(this, playerScore, oppScore);
        
        LayoutParams previewBoardParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        previewBoardParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        previewBoardParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        previewBoard.setLayoutParams(previewBoardParams);
        previewBoard.setEnabled(false);
        
        LayoutParams mainBoardParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mainBoardParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mainBoardParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mainBoard.setLayoutParams(mainBoardParams);
        
        layout.addView(previewBoard);
        layout.addView(mainBoard);
        
        mainBoard.setShootable(meStartFirst);
        if(meStartFirst)
        	Toast.makeText(this, getString(R.string.you_start), Toast.LENGTH_SHORT).show();
        else {
        	Toast.makeText(this, getString(R.string.opponent_starts), Toast.LENGTH_SHORT).show();
        	aiComputer.doShot();
        }
        
    }

	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		GameShipButton button = (GameShipButton) buttonView;
		if(!button.isTarget()) {
			int x = button.getFieldX();
			int y = button.getFieldY();
			if(button.isNotShip()) {
				if(currentTarget != null)
					currentTarget.setTarget(false);
				
				if(Constants.LOGS_ENABLED) System.out.println("button (" + x + "," + y + ") clicked");
				currentTarget = button;
				button.setTarget(true);
			}
		}
		else if(currentTarget != null) {	// shot
			button.setTarget(false);
			matrix = aiComputer.receiveShot(currentTarget.getFieldX(), currentTarget.getFieldY()).getMatrix();
		}
	}
	
	private void endGame(boolean result) {
		Intent intent = new Intent(this, MainMenu.class);
		intent.putExtra(Constants.KEY_GAME_RESULT, result ? Constants.GAME_RESULT_WINNER : Constants.GAME_RESULT_LOOSER);
		
		startActivity(intent);
		this.finish();
	}
	
	@SuppressLint("HandlerLeak")
	class GameMessagesHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if(data.getInt(Constants.GameMessagesHandler_KEY_TYPE) == Constants.GameMessagesHandler_TYPE_SHOT) {
				if(Constants.LOGS_ENABLED) System.out.println("shot info handled");
				int x = msg.getData().getInt(Constants.GameMessagesHandler_KEY_X);
				int y = msg.getData().getInt(Constants.GameMessagesHandler_KEY_Y);
				ShotResult result = previewBoard.shoot(x, y);
				if(previewBoard.isGameEnded()) {
					endGame(false);
				}
				else {
					result.setCoordinates(new Coordinates(x, y));
					aiComputer.receiveResult(result);
					if(!result.isHit() || result.isSunk())
						mainBoard.setShootable(true);
					if(result.isSunk())
						scoreBoard.remotePlayerScoreUp();
				}
			}
			else if(data.getInt(Constants.GameMessagesHandler_KEY_TYPE) == Constants.GameMessagesHandler_TYPE_RESULT) {
				if(Constants.LOGS_ENABLED) System.out.println("shot result info handled");
				boolean result = data.getBoolean(Constants.GameMessagesHandler_KEY_RESULT_IS_HIT);
				mainBoard.shotResult(currentTarget.getFieldX(), currentTarget.getFieldY(), result);
				boolean nextShot = result && !data.getBoolean(Constants.GameMessagesHandler_KEY_RESULT_IS_SUNK);
				mainBoard.setShootable(nextShot);
				
				currentTarget = null;
				if(data.getBoolean(Constants.GameMessagesHandler_KEY_RESULT_IS_SUNK)) {
					if(Constants.LOGS_ENABLED) System.out.println("ship is sunked");
					scoreBoard.localPlayerScoreUp();
					mainBoard.setShipSunk(matrix);
					if(aiComputer.isGameEnded()) {
						nextShot = true;	// to prevent aiComputer from shooting when game is ended
						endGame(true);
					}
				}
				
				if(!nextShot)
					aiComputer.doShot();
			}
		}
	};
    
}
