package pl.mbassara.battleships.activities;

import java.util.Random;

import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.activities.CreatingShipsActivity;
import pl.mbassara.battleships.bluetooth.BluetoothService;
import pl.mbassara.battleships.bluetooth.GamePacket;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ToggleButton;

public class GameActivity extends Activity
	implements OnCheckedChangeListener {
	private GameBoard board;
	private BluetoothService bluetoothService;
	private boolean meStartFirst;
	private int gameMode;
	private ToggleButton shotButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game_layout);
        shotButton = (ToggleButton) findViewById(R.id.shotButton);
        shotButton.setOnCheckedChangeListener(this);
        board = new GameBoard(this, CreatingShipsActivity.getBoardMatrix());
        
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        board.setLayoutParams(layoutParams);
        board.setEnabled(false);
        layout.addView(board);
        
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
        	while(packet == null) {
        		packet = bluetoothService.receive();
        		try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        		
        		if(packet != null && (packet.getType() != GamePacket.WHO_STARTS))
        			packet = null;
        	}
        	meStartFirst = packet.getWhoStarts() == GamePacket.CLIENT_FIRST;
        }
        
        if(meStartFirst)
        	Toast.makeText(this, getString(R.string.you_start), Toast.LENGTH_SHORT).show();
        else
        	Toast.makeText(this, getString(R.string.opponent_starts), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.equals(shotButton)) {
			board.setEnabled(isChecked);
		}
	}
    
}
