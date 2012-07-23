package pl.mbassara.battleships.bluetooth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import pl.mbassara.battleships.Log;
import pl.mbassara.battleships.R;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

public abstract class BluetoothService {
	
	protected BluetoothSocket socket = null;
	protected String UUID;
	
	private boolean isConnected = false;
	private LinkedList<GamePacket> toSendQueue;
	private LinkedList<GamePacket> receivedQueue;
	private SendingThread sendingThread;
	private ReceivingThread receivingThread;
	private ConnectingThread connectingThread;
	
	public BluetoothService(Context parentContext) {
		Context context = parentContext.getApplicationContext();
        UUID = context.getString(R.string.UUID);
        toSendQueue = new LinkedList<GamePacket>();
        receivedQueue = new LinkedList<GamePacket>();
        
        sendingThread = new SendingThread();
        receivingThread = new ReceivingThread();
        connectingThread = new ConnectingThread();
	}
	
	abstract public boolean connectSpecific();
	
	public void connect() {
		if(Log.enabled) System.out.println("BluetoothService.connect()");
		if(!connectingThread.isAlive()) connectingThread.start();
	}
	
	public void stop() {
		if(sendingThread != null) sendingThread.cancel();
		if(receivingThread != null) receivingThread.cancel();
		if(connectingThread != null) connectingThread.cancel();
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

	class SendingThread extends Thread {
		private boolean isAlive = false;
		private ObjectOutputStream outputStream;
		
		@Override
		public void run() {
			if(Log.enabled) System.out.println("SendingThread.run()");
			isAlive = true;
			try {
				while(!isConnected && isAlive)
					Thread.sleep(250);
				
				outputStream = new ObjectOutputStream(
								socket.getOutputStream());
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
			if(Log.enabled) System.out.println("SendingThread.cancel()");
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
			if(Log.enabled) System.out.println("ReceivingThread.run()");
			isAlive = true;
			try {
				while(!isConnected && isAlive)
					Thread.sleep(250);
				
				inputStream = new ObjectInputStream(
								socket.getInputStream());
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
			if(Log.enabled) System.out.println("ReceivingThread.cancel()");
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
			if(Log.enabled) System.out.println("ConnectingThread.run()");
			isAlive = true;
			while(!isConnected && isAlive) {
				isConnected = connectSpecific();	// can block
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(!sendingThread.isAlive()) sendingThread.start();
			if(!receivingThread.isAlive()) receivingThread.start();
		}
		
		public void cancel() {
			if(Log.enabled) System.out.println("ConnectingThread.cancel()");
			isAlive = false;
			try {
				if(socket != null) socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
