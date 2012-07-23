package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.GameBoard;
import pl.mbassara.battleships.GameShipButton;
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
import android.widget.RelativeLayout.LayoutParams;

public class GameActivity extends Activity
	implements OnCheckedChangeListener{
	
	private GameBoard board;
	private BluetoothService bluetoothService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game_layout);
        board = new GameBoard(this, CreatingShipsActivity.getBoardMatrix());
        
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        board.setLayoutParams(layoutParams);
        layout.addView(board);
        
        bluetoothService = BluetoothActivity.getBluetoothService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		System.out.println("button clicked!");
		GameShipButton button = (GameShipButton) buttonView;
		bluetoothService.send(new GamePacket(button.getFieldX(), button.getFieldY()));
	}

    
}
