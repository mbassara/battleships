package pl.mbassara.battleships;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GameResult
	implements Serializable{
	
	private static final long serialVersionUID = 4596309422991271910L;

	private int result;
	
	public GameResult(int result) {
		this.result = result;
	}
	
	public boolean isWinner() {
		return result == Global.GAME_RESULT_WINNER;
	}
	
	public static Element getXMLelement(GameResult gameResult, Document doc) {
		Element element = doc.createElement("gameResult");
		
		if(gameResult == null) {
			element.setAttribute("isNull", "true");
			return element;
		}
		else
			element.setAttribute("isNull", "false");
		
		element.setAttribute("result", gameResult.result == Global.GAME_RESULT_WINNER ? "winner" : "looser");
		
		return element;
	}
}
