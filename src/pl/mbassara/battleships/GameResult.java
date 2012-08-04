package pl.mbassara.battleships;

import java.io.Serializable;

public class GameResult
	implements Serializable{
	
	private static final long serialVersionUID = 4596309422991271910L;
	public static final int RESULT_WINNER = 1;
	public static final int RESULT_LOOSER = 1;

	private int result;
	
	public GameResult(int result) {
		this.result = result;
	}
	
	public boolean isWinner() {
		return result == RESULT_WINNER;
	}
}
