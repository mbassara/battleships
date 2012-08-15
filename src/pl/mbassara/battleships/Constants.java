package pl.mbassara.battleships;

public class Constants {
	public static final boolean LOGS_ENABLED = true;
	
	public static final String GAME_TYPE = "pl.mbassara.battleships.GAME_TYPE";
	public static final String SINGLEPLAYER = "pl.mbassara.battleships.SINGLEPLAYER";
	public static final String MULTIPLAYER = "pl.mbassara.battleships.MULTIPLAYER";

	public static final String GameMessagesHandler_KEY_X = "x";
	public static final String GameMessagesHandler_KEY_Y = "y";
	public static final String GameMessagesHandler_KEY_RESULT_IS_HIT = "isHit";
	public static final String GameMessagesHandler_KEY_RESULT_IS_SUNK = "isSunk";
	public static final String GameMessagesHandler_KEY_TYPE = "type";
	public static final String GameMessagesHandler_KEY_SHOOT_BUTTON_SET_ENABLED = "setEnabled";
	public static final int GameMessagesHandler_TYPE_SHOT = 1;
	public static final int GameMessagesHandler_TYPE_RESULT = 2;
	public static final int GameMessagesHandler_TYPE_SHOOT_BUTTON_SET_ENABLED = 3;

	public static final String KEY_GAME_RESULT = "gameReslut";
	public static final int GAME_RESULT_WINNER = 4;
	public static final int GAME_RESULT_LOOSER = 5;

	public static final int[] SHIPS_COUNER = {0, 0, 2, 2, 2, 1};

    public static final String UUID = "76b4c611-da5a-4672-af97-7eb2fb71597e";

}
