package fleetbattle.model;

import java.util.ArrayList;

public class GameData {
	
	ArrayList<Ship> ownFleet;
	ArrayList<Ship> opponentsFleet;
	boolean[][] ownTable;
	boolean[][] opponentsTable;
	boolean connected;
	
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
//		oppFleet.removeAll(oppFleet);
//		oppFleet.addAll(list);
//		for(Ship s: oppFleet) {
//			System.out.println(s.name() + " koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
//		}
	}
	
	public void setOwnFleet(ArrayList<Ship> list) {
		this.ownFleet = list;
//		ownFleet.removeAll(ownFleet);
//		ownFleet.addAll(list);
//		for(Ship s: ownFleet) {
//			System.out.println(s.name() + " koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
//		}
		
	}
	
	public void addShipToOppFleet(Ship ship) {
		opponentsFleet.add(ship);
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isConnected() {
		return connected;
	}

	

}