package pl.mbassara.battleships;

import pl.mbassara.battleships.connections.RemoteService;
import pl.mbassara.battleships.enums.DeviceType;
import pl.mbassara.battleships.enums.GameMode;
import pl.mbassara.battleships.enums.MultiplayerGameMode;

public class Global {

	private static Global instance = null;

	public static Global getInstance() {
		if (instance == null)
			instance = new Global();

		return instance;
	}

	public final boolean LOGS_ENABLED = true;
	public final String END_OF_PACKET = "end_168321_end";
	public final int[] SHIPS_COUNER = { 0, 0, 2, 2, 2, 1 };
	public final int WIFI_PORT = 57419;
	public final String UUID = "76b4c611-da5a-4672-af97-7eb2fb71597e";

	public boolean SHOOTING_TIPS_ENABLED = true;
	public GameMode GAME_MODE = GameMode.UNDEFINED;
	public MultiplayerGameMode MULTIPLAYER_GAME_MODE = MultiplayerGameMode.UNDEFINED;
	public RemoteService remoteService = null;
	public DeviceType OPPONENT_DEVICE_TYPE = DeviceType.WINDOWS;
	private boolean[][] localUserBoardMatrix;

	public void setLocalUserBoardMatrix(boolean[][] matrix) {
		if (matrix.length == 0)
			return;
		localUserBoardMatrix = new boolean[matrix.length][matrix[0].length];
		for (int i = 0; i < localUserBoardMatrix.length; i++)
			for (int j = 0; j < localUserBoardMatrix[i].length; j++)
				localUserBoardMatrix[i][j] = matrix[i][j];
	}

	public boolean[][] getLocalUserBoardMatrix() {
		return localUserBoardMatrix;
	}

}
