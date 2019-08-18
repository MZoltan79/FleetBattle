package fleetbattle.model;

import java.util.ArrayList;

public class GameData {
	
	ArrayList<Ship> ownFleet;
	ArrayList<Ship> oppFleet;
	
	private static GameData instance;
	
	public static GameData getInstance() {
		if(instance == null) {
			instance = new GameData();
		}
		return instance; 
	}
	
	private GameData() {
		ownFleet = new ArrayList<Ship>();
		oppFleet = new ArrayList<Ship>();
	}

	public ArrayList<Ship> getOwnFleet() {
		return ownFleet;
	}

	public ArrayList<Ship> getOppFleet() {
		return oppFleet;
	}

	
	public void setOppFleet(ArrayList<Ship> list) {
		this.oppFleet = list;
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
		oppFleet.add(ship);
	}

	

}
