package pl.mbassara.battleships;

public class Constants {
	public static final boolean LOGS_ENABLED = true;
	
	public static final boolean HOST_FIRST = false;
	public static final boolean CLIENT_FIRST = true;
	
	public static String USERS_NAME = "Player";

	public static final String GameMessagesHandler_KEY_X = "x";
	public static final String GameMessagesHandler_KEY_Y = "y";
	public static final String GameMessagesHandler_KEY_RESULT_IS_HIT = "isHit";
	public static final String GameMessagesHandler_KEY_RESULT_IS_SUNK = "isSunk";
	public static final String GameMessagesHandler_KEY_RESULT_IS_GAME_ENDED = "isGameEnded";
	public static final String GameMessagesHandler_KEY_TYPE = "type";
	public static final String GameMessagesHandler_KEY_SHOOT_BUTTON_SET_ENABLED = "setEnabled";
	public static final String GameMessagesHandler_KEY_WHO_STARTS = "whoStarts";
	public static final String GameMessagesHandler_KEY_USERS_NAME = "usersName";
	public static final int GameMessagesHandler_TYPE_SHOT = 1;
	public static final int GameMessagesHandler_TYPE_RESULT = 2;
	public static final int GameMessagesHandler_TYPE_SHOOT_BUTTON_SET_ENABLED = 3;
	public static final int GameMessagesHandler_TYPE_WHO_STARTS = 4;
	public static final int GameMessagesHandler_TYPE_USERS_NAME = 5;

	public static final String KEY_GAME_RESULT = "gameReslut";
	public static final int GAME_RESULT_WINNER = 4;
	public static final int GAME_RESULT_LOOSER = 5;

	public static final int[] SHIPS_COUNER = {0, 0, 2, 2, 2, 1};
	
	public static final int wifi_port = 57419;
	
	public static boolean SHOOTING_TIPS_ENABLED = true;
	
	public static String GAME_MODE = "Undefined";
	public static final String GAME_MODE_WIFI = "Wifi mode";
	public static final String GAME_MODE_BT = "Bluetooth mode";
	public static final String GAME_MODE_SINGLE = "Single player";
	public static final String GAME_MODE_HOST = "Host mode";
	public static final String GAME_MODE_CLIENT = "Client mode";

    public static final String UUID = "76b4c611-da5a-4672-af97-7eb2fb71597e";

}
