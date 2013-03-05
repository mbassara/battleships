package pl.mbassara.battleships.connections;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pl.mbassara.battleships.ArrayUtilities;
import pl.mbassara.battleships.Coordinates;
import pl.mbassara.battleships.ShotResult;
import pl.mbassara.battleships.enums.GamePacketType;
import pl.mbassara.battleships.enums.GameResult;
import pl.mbassara.battleships.enums.WhoStarts;


class GamePacketXMLHandler extends DefaultHandler {
	
	private GamePacket packet = null;
	private GamePacketType type;
	private String message = null;
	private Coordinates coordinates = null;
	private WhoStarts whoStarts;
	private ShotResult shotResult = null;
	private GameResult gameResult = null;
	private boolean[][] matrix;
	private boolean isHit;
	private boolean isSunk;
	private boolean isGameEnded;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attr) throws SAXException {
		
//System.out.println("start:\t" + qName);
		
		if(qName.equals("gamePacket")) {
			if(attr.getValue("type").equals(GamePacketType.WHO_STARTS.name())) {
				type = GamePacketType.WHO_STARTS;
				whoStarts = attr.getValue("whoStarts").equals("host") ?
								WhoStarts.HOST_STARTS :
								WhoStarts.CLIENT_STARTS;
			}
			else if(attr.getValue("type").equals(GamePacketType.TEXT_MESSAGE.name())) {
				type = GamePacketType.TEXT_MESSAGE;
				message = attr.getValue("message");
			}
			else if(attr.getValue("type").equals(GamePacketType.SHOT.name())) {
				type = GamePacketType.SHOT;
			}
			else if(attr.getValue("type").equals(GamePacketType.RESULT.name())) {
				type = GamePacketType.RESULT;
			}
			else if(attr.getValue("type").equals(GamePacketType.GAME_RESULT.name())) {
				type = GamePacketType.GAME_RESULT;
			}
			else if(attr.getValue("type").equals(GamePacketType.OPPONENT_DISCONNECTED.name())) {
				type = GamePacketType.OPPONENT_DISCONNECTED;
			}
		}
		else if(qName.equals("coordinates") && attr.getValue("isNull").equals("false")) {
			int x = Integer.parseInt(attr.getValue("x"));
			int y = Integer.parseInt(attr.getValue("y"));
			coordinates = new Coordinates(x, y);
		}
		else if(qName.equals("gameResult") && attr.getValue("isNull").equals("false")) {
			gameResult = attr.getValue("result").equals("winner") ? GameResult.WINNER : GameResult.LOOSER;
		}
		else if(qName.equals("shotResult") && attr.getValue("isNull").equals("false")) {
			isHit = Boolean.parseBoolean(attr.getValue("isHit"));
			isSunk = Boolean.parseBoolean(attr.getValue("isSunk"));
			isGameEnded = Boolean.parseBoolean(attr.getValue("isGameEnded"));
			String matrixStr = attr.getValue("matrix");
			if(!matrixStr.equals("null"))
				matrix = ArrayUtilities.stringToBoolArray(matrixStr);
		}
		
		super.startElement(uri, localName, qName, attr);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
//System.out.println("end:\t" + qName);

		if(qName.equals("gamePacket")) {
			switch (type) {
				case GAME_RESULT:
					packet = new GamePacket(gameResult);
					break;
				case RESULT:
					packet = new GamePacket(shotResult);
					break;
				case SHOT:
					packet = new GamePacket(coordinates);
					break;
				case TEXT_MESSAGE:
					packet = new GamePacket(message);
					break;
				case WHO_STARTS:
					packet = new GamePacket(whoStarts);
					break;
				default:
					break;
			}
		}
		else if(qName.equals("shotResult")) {
			shotResult = new ShotResult(isHit, isSunk, isGameEnded, coordinates, matrix);
		}
		
		super.endElement(uri, localName, qName);
	}
	
	public GamePacket getPacket() {
		return packet;
	}
	
}

public abstract class GamePacketSerialization {
	
	public static String serialize(GamePacket packet) {
		
		try
		{
		
		  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		  DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		  Document doc = docBuilder.newDocument();
		  
		  doc.appendChild(packet.getXMLelement(doc));
		  
		  TransformerFactory transformerFactory = TransformerFactory.newInstance();
		  Transformer transformer = transformerFactory.newTransformer();
		  DOMSource source = new DOMSource(doc);

		  StringWriter stringWriter = new StringWriter();
		  StreamResult result =  new StreamResult(stringWriter);
		  transformer.transform(source, result);
		  
		  return stringWriter.toString();
		  
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static GamePacket deserialize(String string) {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			GamePacketXMLHandler handler = new GamePacketXMLHandler();
			
			parser.parse(new ByteArrayInputStream(string.getBytes("UTF-8")), handler);
			return handler.getPacket();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

}
