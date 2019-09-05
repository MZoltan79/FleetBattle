package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Connection extends Thread {
	
	public static String msg = "";
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	public static String sendData = null;
	public static String receivedData = null;
	public static String loginData = null;
	public static boolean ready = false;

	public static boolean login = false;
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
//				System.out.println(receivedData);
				if(receivedData.equals("login successed")) login = true;
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

	public boolean isReady() {
		return ready;
	}

	public String getLoginData() {
		return loginData;
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

	public void send(String msg) {
		pw.println(msg);
	}
	
	public static boolean isLogin() {
		return login;
	}
	
}
