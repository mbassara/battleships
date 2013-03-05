package pl.mbassara.battleships;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public abstract class ArrayUtilities {
    
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
