package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.R;
import pl.mbassara.battleships.activities.connections.bluetooth.BluetoothClientActivity;
import pl.mbassara.battleships.activities.connections.bluetooth.BluetoothHostActivity;
import pl.mbassara.battleships.activities.connections.wifi.WiFiClientActivity;
import pl.mbassara.battleships.activities.connections.wifi.WiFiHostActivity;
import pl.mbassara.battleships.enums.GameMode;
import pl.mbassara.battleships.enums.MultiplayerGameMode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameModeActivity extends Activity {

	private static MultiplayerGameMode mode = MultiplayerGameMode.HOST;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_mode);
	}

	public static MultiplayerGameMode getMode() {
		return mode;
	}

	public void gameModeClient(View view) {
		mode = MultiplayerGameMode.CLIENT;
		Intent intent = null;
		if (Global.getInstance().GAME_MODE == GameMode.BLUETOOTH)
			intent = new Intent(this, BluetoothClientActivity.class);
		else if (Global.getInstance().GAME_MODE == GameMode.WIFI)
			intent = new Intent(this, WiFiClientActivity.class);

		if (intent != null)
			startActivity(intent);
	}

	public void gameModeHost(View view) {
		mode = MultiplayerGameMode.HOST;
		Intent intent = null;
		if (Global.getInstance().GAME_MODE == GameMode.BLUETOOTH)
			intent = new Intent(this, BluetoothHostActivity.class);
		else if (Global.getInstance().GAME_MODE == GameMode.WIFI)
			intent = new Intent(this, WiFiHostActivity.class);

		if (intent != null)
			startActivity(intent);
	}

}
