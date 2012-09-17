package pl.mbassara.battleships;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class ScoreBoard {

	private TextView localPlayerScore;
	private TextView remotePlayerScore;
	private String localPlayerScoreString;
	private String remotePlayerScoreString;
	private int localPlayerScoreInt;
	private int remotePlayerScoreInt;
	
	public ScoreBoard(Context context, TextView localPlayerScore, TextView remotePlayerScore) {
		this.localPlayerScore = localPlayerScore;
		this.remotePlayerScore = remotePlayerScore;
		
		localPlayerScoreString = context.getString(R.string.your_score);
		remotePlayerScoreString = context.getString(R.string.opponents_score);
		
		localPlayerScoreInt = 0;
		remotePlayerScoreInt = 0;

		localPlayerScore.setText(localPlayerScoreString + ":\t" + localPlayerScoreInt);
		remotePlayerScore.setText(remotePlayerScoreString + ":\t" + remotePlayerScoreInt);
		
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SF_Collegiate.ttf");
		localPlayerScore.setTypeface(typeface);
		remotePlayerScore.setTypeface(typeface);
	}
	
	public void setLocalPlayersName(String name) {
		localPlayerScoreString = name;
		localPlayerScore.setText(localPlayerScoreString + ":\t" + localPlayerScoreInt);
	}
	
	public void setRemotePlayersName(String name) {
		remotePlayerScoreString = name;
		remotePlayerScore.setText(remotePlayerScoreString + ":\t" + remotePlayerScoreInt);
	}
	
	public void localPlayerScoreUp() {
		localPlayerScoreInt++;
		localPlayerScore.setText(localPlayerScoreString + ":\t" + localPlayerScoreInt);
	}
	
	public void remotePlayerScoreUp() {
		remotePlayerScoreInt++;
		remotePlayerScore.setText(remotePlayerScoreString + ":\t" + remotePlayerScoreInt);
	}
	
}
