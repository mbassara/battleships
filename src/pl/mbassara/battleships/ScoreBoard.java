package pl.mbassara.battleships;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class ScoreBoard {

	private TextView localPlayerScore;
	private TextView remotePlayerScore;
	private String localPlayerName;
	private String remotePlayerName;
	private int localPlayerScoreInt;
	private int remotePlayerScoreInt;

	public ScoreBoard(Context context, TextView localPlayerScore, TextView remotePlayerScore) {
		this.localPlayerScore = localPlayerScore;
		this.remotePlayerScore = remotePlayerScore;

		localPlayerName = context.getString(R.string.score_user_name);
		remotePlayerName = context.getString(R.string.score_opponent_name);

		localPlayerScoreInt = 0;
		remotePlayerScoreInt = 0;

		localPlayerScore.setText(localPlayerName + ":\t" + localPlayerScoreInt);
		remotePlayerScore.setText(remotePlayerName + ":\t" + remotePlayerScoreInt);

		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SF_Collegiate.ttf");
		localPlayerScore.setTypeface(typeface);
		remotePlayerScore.setTypeface(typeface);
	}

	public void setLocalPlayersName(String name) {
		localPlayerName = name;
		localPlayerScore.setText(localPlayerName + ":\t" + localPlayerScoreInt);
	}

	public void setRemotePlayersName(String name) {
		remotePlayerName = name;
		remotePlayerScore.setText(remotePlayerName + ":\t" + remotePlayerScoreInt);
	}

	public String getLocalPlayerName() {
		return localPlayerName;
	}

	public String getRemotePlayerName() {
		return remotePlayerName;
	}

	public void localPlayerScoreUp() {
		localPlayerScoreInt++;
		localPlayerScore.setText(localPlayerName + ":\t" + localPlayerScoreInt);
	}

	public void remotePlayerScoreUp() {
		remotePlayerScoreInt++;
		remotePlayerScore.setText(remotePlayerName + ":\t" + remotePlayerScoreInt);
	}

}
