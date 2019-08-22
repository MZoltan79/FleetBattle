package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Connection extends Thread {
	
	public static String msg = "";
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	
	
	@Override
	public void run() {
		try {
			while(true) {
				
			socket = new Socket(InetAddress.getLocalHost(), 11111);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			}
			
//			br.close();
//			pw.close();
//			socket.close();
		} catch (IOException e) {
			System.out.println("Server is no longer available");
		}
	}

	public void setMsg(String msg) {
		Connection.msg = msg;
	}
	
	public void sendData(String data) {
		pw.println(data);
	}
	
	public String receiveData() {
		String data = "";
		try {
			data = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	

	
	
}
