package pl.mbassara.battleships;

import android.content.Context;
import android.os.Vibrator;

public class Vibra {
	
	private Vibrator vibrator;
	private static Vibra instance = null;
	private static boolean isEnabled = true;
	
	private Vibra(Context context) {
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public static Vibra getInstance(Context context) {
		if(instance == null)
			instance = new Vibra(context);
		
		return instance;
	}
	
	public static void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
	
	public static boolean isEnabled() {
		return isEnabled;
	}

	public void start(long[] pattern, int repeat) {
		if(vibrator != null && isEnabled)
			vibrator.vibrate(pattern, repeat);
	}
	
	public void beep() {
		long[] pattern = {0, 50};
		start(pattern, -1);
	}
	
	public void beepLong() {
		long[] pattern = {0, 75};
		start(pattern, -1);
	}
	
	public void beepDouble() {
		long[] pattern = {0, 75, 150, 75};
		start(pattern, -1);
	}
	
	public void beepTriple() {
		long[] pattern = {0, 75, 150, 75, 150, 75};
		start(pattern, -1);
	}
	
}
