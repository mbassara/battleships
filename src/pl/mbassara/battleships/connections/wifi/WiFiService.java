package pl.mbassara.battleships.connections.wifi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import pl.mbassara.battleships.Constants;
import pl.mbassara.battleships.connections.GamePacket;
import pl.mbassara.battleships.connections.RemoteService;

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
        UUID = Constants.UUID;
        toSendQueue = new LinkedList<GamePacket>();
        receivedQueue = new LinkedList<GamePacket>();
        
        sendingThread = new SendingThread();
        receivingThread = new ReceivingThread();
        connectingThread = new ConnectingThread();
	}
	
	abstract public Socket connectSpecific();

	abstract public void cancelSpecific();
	
	public void connect() {
		if(Constants.LOGS_ENABLED) System.out.println("WiFiService.connect()");
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
		private ObjectOutputStream outputStream;
		
		@Override
		public void run() {
			if(Constants.LOGS_ENABLED) System.out.println("SendingThread.run()");
			isAlive = true;
			try {
				while(!isConnected && isAlive)
					Thread.sleep(250);
				
				if(isAlive) {
					outputStream = new ObjectOutputStream(
									socket.getOutputStream());
				}
				GamePacket packet;
			
				while(isAlive) {
					if(!toSendQueue.isEmpty()) {
						packet = toSendQueue.poll();
						outputStream.writeObject(packet);
						outputStream.flush();
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
			if(Constants.LOGS_ENABLED) System.out.println("SendingThread.cancel()");
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
		private ObjectInputStream inputStream;
		
		@Override
		public void run() {
			if(Constants.LOGS_ENABLED) System.out.println("ReceivingThread.run()");
			isAlive = true;
			try {
				while(!isConnected && isAlive)
					Thread.sleep(250);

				if(isAlive) {
					inputStream = new ObjectInputStream(
									socket.getInputStream());
				}
				GamePacket packet;
				
				while(isAlive) {
					packet = (GamePacket) inputStream.readObject();
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
			if(Constants.LOGS_ENABLED) System.out.println("ReceivingThread.cancel()");
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
			if(Constants.LOGS_ENABLED) System.out.println("ConnectingThread.run()");
			isAlive = true;
			while(!isConnected && isAlive) {
				socket = connectSpecific();	// can block
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
			if(Constants.LOGS_ENABLED) System.out.println("ConnectingThread.cancel()");
			
			cancelSpecific();
			isAlive = false;
		}
	}

}
