package pl.mbassara.battleships.connections;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.enums.GamePacketType;
import pl.mbassara.battleships.enums.GameResult;
import pl.mbassara.battleships.enums.WhoStarts;

public class GamePacket implements Serializable{
	
	private static final long serialVersionUID = 5504703199854898469L;
	private GamePacketType type;
	private String message = null;
	private String userName = null;
	private Coordinates coordinates = null;
	private WhoStarts whoStarts = WhoStarts.HOST_STARTS;
	private ShotResult shotResult = null;
	private GameResult gameResult = null;
	
	public GamePacket(GamePacketType type, Object value) {
		this.type = type;
		switch (type) {
			case WHO_STARTS:
				this.whoStarts = (WhoStarts) value;
				break;
			case SHOT:
				this.coordinates = (Coordinates) value;
				break;
			case RESULT:
				this.shotResult = (ShotResult) value;
				break;
			case TEXT_MESSAGE:
				this.message = (String) value;
				break;
			case GAME_RESULT:
				this.gameResult = (GameResult) value;
				break;
			case USER_NAME:
				this.userName = (String) value;
				break;
			case OPPONENT_DISCONNECTED:
				break;
	
			default:
				this.type = GamePacketType.PACKET_BROKEN;
				break;
		}
	}
	
	public GamePacket(WhoStarts whoStarts) {
		this.whoStarts = whoStarts;
		type = GamePacketType.WHO_STARTS;
	}
	
	public GamePacket(ShotResult result) {
		this.shotResult = result;
		type = GamePacketType.RESULT;
	}
	
	public GamePacket(GameResult result) {
		this.gameResult = result;
		type = GamePacketType.GAME_RESULT;
	}
	
	public GamePacket(Coordinates coordinates) {
		this.coordinates = coordinates;
		type = GamePacketType.SHOT;
	}
	
	public GamePacket(int x, int y) {
		coordinates = new Coordinates(x, y);
		type = GamePacketType.SHOT;
	}
	
	public GamePacket(String message) {
		this.message = message;
		type = GamePacketType.TEXT_MESSAGE;
	}

	public GamePacketType getType() {
		return type;
	}
	
	public WhoStarts getWhoStarts() {
		return whoStarts;
	}
	
	public String getMessage() {
		return message;
	}
	
	public ShotResult getShotResult() {
		return shotResult;
	}
	
	public GameResult getGameResult() {
		return gameResult;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	public Element getXMLelement(Document doc) {
		Element element = doc.createElement("gamePacket");
		
		switch (type) {
			case GAME_RESULT:
				element.appendChild(GameResult.getXMLelement(gameResult, doc));
				break;
			case RESULT:
				element.appendChild(ShotResult.getXMLelement(shotResult, doc));
				break;
			case SHOT:
				element.appendChild(Coordinates.getXMLelement(coordinates, doc));
				break;
			case TEXT_MESSAGE:
				element.setAttribute("message", message);
				break;
			case WHO_STARTS:
				element.setAttribute("whoStarts", whoStarts == WhoStarts.HOST_STARTS ? "host" : "client");
				break;
			default:
				break;
		}
		
		element.setAttribute("type", type.name());
		
		return element;
	}
	
}
