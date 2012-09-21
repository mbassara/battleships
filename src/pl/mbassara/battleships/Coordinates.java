package pl.mbassara.battleships;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Coordinates implements Serializable {
	private static final long serialVersionUID = -8267406114346251730L;
	private int x;
	private int y;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public static Element getXMLelement(Coordinates coordinates, Document doc) {
		Element element = doc.createElement("coordinates");
		
		if(coordinates == null) {
			element.setAttribute("isNull", "true");
			return element;
		}
		else
			element.setAttribute("isNull", "false");
		
		element.setAttribute("x", String.valueOf(coordinates.getX()));
		
		element.setAttribute("y", String.valueOf(coordinates.getY()));
		
		return element;
	}
}