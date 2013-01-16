package pl.mbassara.battleships;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.app.Application;

public class Global {
	public static final boolean LOGS_ENABLED = true;
	
	public static final boolean HOST_FIRST = false;
	public static final boolean CLIENT_FIRST = true;
	
	public static final int DEVICE_TYPE_ANDROID = 0;
	public static final int DEVICE_TYPE_WINDOWS = 1;
	public static int OPPONENT_DEVICE_TYPE = DEVICE_TYPE_WINDOWS;
	
	public static final String END_OF_PACKET = "end_168321_end";
	
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
	public static final int GAME_RESULT_ABORTED = 6;

	public static final int[] SHIPS_COUNER = {0, 0, 2, 2, 2, 1};
	
	public static final int WIFI_PORT = 57419;
	
	public static boolean SHOOTING_TIPS_ENABLED = true;
	
	public static String GAME_MODE = "Undefined";
	public static final String GAME_MODE_WIFI = "Wifi mode";
	public static final String GAME_MODE_BT = "Bluetooth mode";
	public static final String GAME_MODE_SINGLE = "Single player";
	public static final String GAME_MODE_HOST = "host";
	public static final String GAME_MODE_CLIENT = "client";

	public static final String UUID = "76b4c611-da5a-4672-af97-7eb2fb71597e";
	

	
	private static boolean[][] localUserBoardMatrix;
	public static void setLocalUserBoardMatrix(boolean[][] matrix) {
		if(matrix.length == 0)
			return;
		localUserBoardMatrix = new boolean[matrix.length][matrix[0].length];
		for(int i = 0; i < localUserBoardMatrix.length; i++)
			for(int j = 0; j < localUserBoardMatrix[i].length; j++)
				localUserBoardMatrix[i][j] = matrix[i][j];
	}
	public static boolean[][] getLocalUserBoardMatrix() {
		return localUserBoardMatrix;
	}
    
    public static byte[] objectToByteArray(Object obj)
    {
    	ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
    	try {
	    	ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
	    	objectOutputStream.writeObject(obj);
	    	objectOutputStream.flush();
	    	objectOutputStream.close();
	    	byteObject.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    	return byteObject.toByteArray();
	}
	
	public static String boolArrayToString(boolean[][] array) {
		String result = "";
		for(boolean[] subarray : array) {
			for(boolean val : subarray)
				result += val ? "1" : "0";
			result += ",";
		}
		
		return result.substring(0, result.length() - 1);
	}
	
	public static boolean[][] stringToBoolArray(String str) {
		String[] strArray = str.split(",");
		int size = strArray.length;
		boolean[][] result = new boolean[size][size];
		
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				result[i][j] = strArray[i].charAt(j) == '1';
		
		return result;
	}

}
