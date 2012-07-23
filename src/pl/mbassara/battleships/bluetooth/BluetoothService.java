package pl.mbassara.battleships.bluetooth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import pl.mbassara.battleships.R;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

public abstract class BluetoothService {
	
	protected BluetoothSocket socket = null;
	protected String UUID;
	
	private boolean isConnected = false;
	private LinkedList<GamePacket> toSendQueue;
	private LinkedList<GamePacket> receivedQueue;
	private Thread sendingThread;
	private Thread receivingThread;
	
	public BluetoothService(Context parentContext) {
		Context context = parentContext.getApplicationContext();
        UUID = context.getString(R.string.UUID);
        toSendQueue = new LinkedList<GamePacket>();
        receivedQueue = new LinkedList<GamePacket>();
        
        sendingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(!isConnected())
						Thread.sleep(250);
					
					ObjectOutputStream outputStream = new ObjectOutputStream(
															socket.getOutputStream());
					GamePacket packet;
				
					while(true) {
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
				}
			}
		});
        
        receivingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(!isConnected())
						Thread.sleep(250);
					
					ObjectInputStream inputStream = new ObjectInputStream(
															socket.getInputStream());
					GamePacket packet;
					
					while(true) {
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
				}
				
			}
		});
	}
	
	abstract public boolean connectSpecific();
	
	public void connect() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!isConnected)
					isConnected = connectSpecific();	// can block
				if(!sendingThread.isAlive()) sendingThread.start();
				if(!receivingThread.isAlive()) receivingThread.start();
			}
		});
		thread.start();
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
}
