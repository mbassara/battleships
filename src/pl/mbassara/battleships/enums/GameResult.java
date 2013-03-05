package pl.mbassara.battleships.enums;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public enum GameResult {
	WINNER,
	LOOSER,
	OPPONENT_DISCONNECTED,
	ABORTED;
	
	public static Element getXMLelement(GameResult gameResult, Document doc) {
		Element element = doc.createElement("gameResult");
		
		if(gameResult == null) {
			element.setAttribute("isNull", "true");
			return element;
		}
		else
			element.setAttribute("isNull", "false");
		
		element.setAttribute("result", gameResult == GameResult.WINNER ? "winner" : "looser");
		
		return element;
	}
}
