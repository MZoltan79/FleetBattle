package fleetbattle.model;

import java.util.ArrayList;

import communication.Connection;

public class GameData {
	
	ArrayList<Ship> ownFleet;
	ArrayList<Ship> opponentsFleet;
	boolean[][] ownTable;
	boolean[][] opponentsTable;
	boolean connected;
	private Player player1;
	private Player player2;
	private Connection conn;
	
	private static GameData instance;
	
	public static GameData getInstance() {
		if(instance == null) {
			instance = new GameData();
		}
		return instance; 
	}
	
	private GameData() {
		ownFleet = new ArrayList<Ship>();
		opponentsFleet = new ArrayList<Ship>();
		ownTable = new boolean[10][10];
		opponentsTable = new boolean[10][10];
		connected = false;
		
	}

	public ArrayList<Ship> getOwnFleet() {
		return ownFleet;
	}

	public ArrayList<Ship> getOpponentsFleet() {
		return opponentsFleet;
	}
	
	

	
	public boolean[][] getOwnTable() {
		return ownTable;
	}

	public void setOwnTable(boolean[][] table) {
		ownTable = table;
	}

	public boolean[][] getOpponentsTable() {
		return opponentsTable;
	}

	public void setOpponentsTable(boolean[][] table) {
		opponentsTable = table;
	}

	public void setOppFleet(ArrayList<Ship> list) {
		this.opponentsFleet = list;
	}
	
	public void setOwnFleet(ArrayList<Ship> list) {
		this.ownFleet = list;
		
	}
	
	public void addShipToOppFleet(Ship ship) {
		opponentsFleet.add(ship);
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public void clearOwnFleet() {
		ownFleet = null;
		ownFleet = new ArrayList<Ship>();
	}
	
	public boolean isConnected() {
		return connected;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	

}