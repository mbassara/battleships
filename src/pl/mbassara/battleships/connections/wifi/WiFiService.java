package pl.mbassara.battleships.connections.wifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;

import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.connections.GamePacket;
import pl.mbassara.battleships.connections.GamePacketSerialization;
import pl.mbassara.battleships.connections.RemoteService;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public abstract class WiFiService implements RemoteService {
	
	protected Socket socket = null;

	protected String UUID;
	
	private boolean isConnected = false;
	private LinkedList<GamePacket> toSendQueue;
	private LinkedList<GamePacket> receivedQueue;
	private SendingThread sendingThread;
	private ReceivingThread receivingThread;
	private ConnectingThread connectingThread;
	
	public WiFiService(Context parentContext) {
        UUID = Global.UUID;
        toSendQueue = new LinkedList<GamePacket>();
        receivedQueue = new LinkedList<GamePacket>();
        
        sendingThread = new SendingThread();
        receivingThread = new ReceivingThread();
        connectingThread = new ConnectingThread();
	}
	
	abstract public Socket connectSpecific();

	abstract public void cancelSpecific();
	
	public void connect() {
		if(Global.LOGS_ENABLED) System.out.println("WiFiService.connect()");
		if(!connectingThread.isAlive()) connectingThread.start();
	}
	
	public void stop() {
		while(sendingThread.isAlive() || receivingThread.isAlive() || connectingThread.isAlive()) {
			
			if(sendingThread != null && sendingThread.isAlive()) sendingThread.cancel();
			if(receivingThread != null && receivingThread.isAlive()) receivingThread.cancel();
			if(connectingThread != null && connectingThread.isAlive()) connectingThread.cancel();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected() {
		if(socket != null)
			return isConnected;
		else
			return false;
	}
	
	public void send(GamePacket gamePacket) {
		toSendQueue.offer(gamePacket);
	}
	
	public GamePacket receive() {
		if(!receivedQueue.isEmpty())
			return receivedQueue.poll();
		else
			return null;
	}

	public static String getHostAdress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		int ip = info.getIpAddress();
		@SuppressWarnings("deprecation")
		String ipText = Formatter.formatIpAddress(ip);
		
		return ipText;
	}

	class SendingThread extends Thread {
		private boolean isAlive = false;
		private OutputStream outputStream;
		private ObjectOutputStream objectOutputStream;
		private OutputStreamWriter outputStreamWriter;
		
		@Override
		public void run() {
			if(Global.LOGS_ENABLED) System.out.println("SendingThread.run()");
			isAlive = true;
			try {
				while(!isConnected && isAlive)
					Thread.sleep(250);
				
				if(isAlive) {
					outputStream = socket.getOutputStream();
					if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_ANDROID)
						objectOutputStream = new ObjectOutputStream(outputStream);
					if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_WINDOWS)
						outputStreamWriter = new OutputStreamWriter(outputStream);
				}
				GamePacket packet;
			
				while(isAlive) {
					if(!toSendQueue.isEmpty()) {
						packet = toSendQueue.poll();
						if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_ANDROID) {
							objectOutputStream.writeObject(packet);
							objectOutputStream.flush();
						}
						else if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_WINDOWS) {
							String serializedPacket = GamePacketSerialization.serialize(packet);
							outputStreamWriter.write(serializedPacket + Global.END_OF_PACKET);
							outputStreamWriter.flush();
						}
					}
					
					Thread.sleep(500);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				isAlive = false;
			}
		}
		
		public void cancel() {
			if(Global.LOGS_ENABLED) System.out.println("SendingThread.cancel()");
			isAlive = false;
			try {
				if(outputStream != null) outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ReceivingThread extends Thread {
		private boolean isAlive = false;
		private InputStream inputStream;
		private ObjectInputStream objectInputStream;
		private InputStreamReader inputStreamReader;
		
		@Override
		public void run() {
			if(Global.LOGS_ENABLED) System.out.println("ReceivingThread.run()");
			isAlive = true;
			try {
				while(!isConnected && isAlive)
					Thread.sleep(250);

				if(isAlive) {
					inputStream = socket.getInputStream();
					if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_ANDROID)
						objectInputStream = new ObjectInputStream(inputStream);
					if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_WINDOWS)
						inputStreamReader = new InputStreamReader(inputStream);
				}
				GamePacket packet = null;
				
				while(isAlive) {
					if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_ANDROID)
						packet = (GamePacket) objectInputStream.readObject();
					else if(Global.OPPONENT_DEVICE_TYPE == Global.DEVICE_TYPE_WINDOWS) {
						String serializedPacket = "";
						while(!serializedPacket.endsWith(Global.END_OF_PACKET))
							serializedPacket += (char) inputStreamReader.read();
						
						serializedPacket = serializedPacket.replace(Global.END_OF_PACKET, "");
						packet = GamePacketSerialization.deserialize(serializedPacket);
					}
					
					if(packet != null)
						receivedQueue.offer(packet);
					
					Thread.sleep(500);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				isAlive = false;
			}
			
		}
		
		public void cancel() {
			if(Global.LOGS_ENABLED) System.out.println("ReceivingThread.cancel()");
			isAlive = false;
			try {
				if(inputStream != null) inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ConnectingThread extends Thread {
		private boolean isAlive = false;
		
		@Override
		public void run() {
			if(Global.LOGS_ENABLED) System.out.println("ConnectingThread.run()");
			isAlive = true;
			while(!isConnected && isAlive) {
				socket = connectSpecific();	// can block
				
				try {
					InputStream input = socket.getInputStream();
					OutputStream output = socket.getOutputStream();
					
					output.write(Global.DEVICE_TYPE_ANDROID);	// send your DEVICE_TYPE
					
					int opponentDeviceType = input.read();
					if(opponentDeviceType == Global.DEVICE_TYPE_WINDOWS) {
						if(Global.LOGS_ENABLED) System.out.println("Opponent's device type: WINDOWS");
						Global.OPPONENT_DEVICE_TYPE = opponentDeviceType;
					}
					else if(opponentDeviceType == Global.DEVICE_TYPE_ANDROID) {
						if(Global.LOGS_ENABLED) System.out.println("Opponent's device type: ANDROID");
						Global.OPPONENT_DEVICE_TYPE = opponentDeviceType;
					}// if wrong value is read then do nothing (default value (windows) will be used)
					
				} catch (IOException e1) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					socket = null;
					e1.printStackTrace();
				}
				
				isConnected = socket != null;
				
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(!sendingThread.isAlive() && isAlive) sendingThread.start();
			if(!receivingThread.isAlive() && isAlive) receivingThread.start();
		}
		
		public void cancel() {
			if(Global.LOGS_ENABLED) System.out.println("ConnectingThread.cancel()");
			
			cancelSpecific();
			isAlive = false;
		}
	}

}
