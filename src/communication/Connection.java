package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import fleetbattle.model.GamePlay;
import javafx.application.Platform;

public class Connection extends Thread {
	
	public static String msg = "";
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	public static String sendData = null;
	public static String receivedData = null;
	private boolean connected;

	private static Connection instance;
	
	private Connection() {
		
	}
	
	public static Connection getInstance() {
		if(instance == null) {
			instance = new Connection();
		}
		return instance;
	}
	
	
	
	
	@Override
	public void run() {
//		try {
//			while(true) {
//				
//			socket = new Socket(InetAddress.getLocalHost(), 11111);
//			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			pw = new PrintWriter(socket.getOutputStream(), true);
//			break;
//			}
//			System.out.println("Connection established: " + socket.isConnected());
//			while(true) {
//					receivedData = br.readLine();
//					System.out.println("Connection.receivedData: " + receivedData);
//			}
//		} catch (IOException e) {
//			System.out.println("Server is no longer available");
//		}
		receiveData();
	}
	
	public void receiveData() {
		try {
			
			while (true) {
				
				try {
					socket = new Socket(InetAddress.getLocalHost(), 11111);
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					pw = new PrintWriter(socket.getOutputStream(), true);
					break;
				} catch (IOException e) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
					}
				}
				
			}
			
			
			connected = true;
			
			while(true) {
				receivedData = br.readLine();
				System.out.println("Connection.receivedData: " + receivedData);
			}
			
		} catch (UnknownHostException e) {
			connected = false;
		} catch (IOException e) {
			connected = false;
		}
	}
	


	public String getReceivedData() {
		return receivedData;
	}


	public void setMsg(String msg) {
		Connection.msg = msg;
	}
	
	public void sendData() {
		pw.println(sendData);
	}
	
	public boolean isConnected() {
		return connected;
	}

	
	
}
