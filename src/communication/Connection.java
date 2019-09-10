package communication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
	public static boolean newPlayer = false;
	private Integer port;
	private String host;

	public static boolean login = false;
	private boolean connected;
	private boolean clientOne; 

	private static Connection instance;
	
	private Connection() {
		loadData();
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
					if(host.equals("localhost")) {
						socket = new Socket(InetAddress.getLocalHost(), port);
					} else {
						socket = new Socket(host, port);
					}
//					System.out.println("socket connected: " + socket.isConnected());
//					System.out.println("port: " + socket.getLocalPort());
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
			
			while(connected) {
				receivedData = br.readLine();
				if(receivedData.equals("login successed")) login = true;
				if(receivedData.equals("new player successed")) newPlayer = true;
				if(receivedData.equals("client1")) clientOne = true;
				if(receivedData.equals("client2")) clientOne = false;
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
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void send(String msg) {
		pw.println(msg);
	}
	
	public static boolean isLogin() {
		return login;
	}
	
	public static boolean isNewPlayer() {
		return newPlayer;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
		System.out.println(this.port);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isClientOne() {
		return clientOne;
	}
	
	public void loadData() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("connection.txt"));
			while(br.ready()) {
				String line = br.readLine();
				String[] tmp = line.split(";");
				host = tmp[0];
				port = Integer.parseInt(tmp[1]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveData() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter("connection.txt"));
			pw.println(host + ";" + port.toString());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
