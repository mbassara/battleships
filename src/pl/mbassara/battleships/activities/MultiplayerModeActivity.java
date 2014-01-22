package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.enums.GameMode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MultiplayerModeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiplayer_mode);
	}

	public void chooseMode(View view) {
		Intent intent = new Intent(this, GameModeActivity.class);

		if (view.equals(findViewById(R.id.WiFiModeButton)))
			Global.getInstance().GAME_MODE = GameMode.WIFI;
		else if (view.equals(findViewById(R.id.bluetoothModeButton)))
			Global.getInstance().GAME_MODE = GameMode.BLUETOOTH;

		if (Global.getInstance().LOGS_ENABLED)
			System.out.println("Multiplayer mode: " + Global.getInstance().GAME_MODE);
		startActivity(intent);
	}

}
