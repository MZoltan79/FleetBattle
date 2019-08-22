package fleetbattle.model;

import java.util.ArrayList;
import java.util.Random;

public class AutoPlaceOpponent {
	
	private static AutoPlaceOpponent instance;
	
	public static AutoPlaceOpponent getInstance() {
		if(instance == null) {
			instance = new AutoPlaceOpponent();
		}
		return instance;
	}

	private AutoPlaceOpponent() {
		this.freeFields = new ArrayList<>();
	}

	private static Ship carrier;
	private static Ship destroyer;
	private static Ship submarine;
	private static Ship cruiser;
	private static Ship patrolBoat;
	
	private static boolean[][]table = new boolean[10][10];
	
	protected static Random rnd = new Random(); 

	private ArrayList<Ship>fleet = new ArrayList<Ship>(); 
	
	protected static ArrayList<Integer[]> fieldsCoordinates = new ArrayList<>();

	protected ArrayList<Integer[]> freeFields = new ArrayList<>();
	
	public ArrayList<Ship> getFleet() {
		return fleet;
	}

	public void setupOfFields() { 
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				fieldsCoordinates.add(new Integer[] {i,j});
			}
		}
	}
	

	
	private void carrier() {
		carrier = new Ship("carrier");
		carrier.setVertical(randomDirection());
	}

	private void destroyer() {
		destroyer = new Ship("destroyer");
		destroyer.setVertical(randomDirection());
	}

	private void submarine() {
		submarine = new Ship("submarine");
		submarine.setVertical(randomDirection());
	}

	private void cruiser() {
		cruiser = new Ship("cruiser");
		cruiser.setVertical(randomDirection());
	}

	private void patrolBoat() {
		patrolBoat = new Ship("patrolboat");
		patrolBoat.setVertical(randomDirection());
	}


	private void checkFields(Ship ship) {
		for(int i = 0; i < fieldsCoordinates.size(); i++) {
			
		Integer[] startPoints = fieldsCoordinates.get(i);
		if(!ship.isVertical()) {
			if(checkStartPointByVerticalDirection(startPoints, ship) &&
					(checkFieldsByVerticalDirection(startPoints, ship))) {
				freeFields.add(startPoints);
			}
		} else {
			if(checkStartPointByHorizontalDirection(startPoints, ship) &&
					(checkFieldsByHorizontalDirection(startPoints, ship))) {
				freeFields.add(startPoints);
				}
			
			}
		}
	}
	
	private boolean checkFieldsByVerticalDirection(Integer[] startPoint, Ship ship) {
		int size = ship.getSize();
		for(int i = 0; i < size; i++) {
			if(table[startPoint[0]+i][startPoint[1]] == true) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkFieldsByHorizontalDirection(Integer[] startPoint, Ship ship) {
		int size = ship.getSize();
		for(int i = 0; i < size; i++) {
			if(table[startPoint[0]][startPoint[1]+i] == true) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkStartPointByVerticalDirection(Integer[] startPoint, Ship ship) {
		if(startPoint[0] > 9-(ship.getSize()-1)) return false;
		return true;
	}
	
	private boolean checkStartPointByHorizontalDirection(Integer[] startPoint, Ship ship) {
		if(startPoint[1] > 9-(ship.getSize()-1)) return false;
		return true;
	}
	
	private boolean randomDirection() { 		
		return rnd.nextBoolean();
	}

	private void randomize(Ship ship) {
		int temp = rnd.nextInt(freeFields.size());
		Integer[] startPoints = freeFields.get(temp);
		ship.setStartpoint(startPoints[0], startPoints[1]);
	}

	private void placeShip(Ship ship) {
		ship.setCoordinates(ship.getCoordinates()[0], ship.getCoordinates()[1]);
		for(int i = 0; i < ship.getSize(); i++) {
			if(ship.isVertical()) {
				table[ship.getCoordinates()[0]][ship.getCoordinates()[1]+i] = true;
			} else {
				table[ship.getCoordinates()[0]+i][ship.getCoordinates()[1]] = true;
			}
		}
		ship.isPlaced = true;
	}

	public void placeAll() {
		
		carrier();
		checkFields(carrier);
		randomize(carrier);
		placeShip(carrier);
		fleet.add(carrier);
		freeFields.removeAll(freeFields);
//		System.out.println("game carrier: " + carrier.coordinates[0] + "," + carrier.coordinates[1] 
//				+ " " + carrier.coordinates[8] + "," + carrier.coordinates[9]);
//		System.out.println(carrier.isVertical());
		
		destroyer();
		checkFields(destroyer);
		randomize(destroyer);
		placeShip(destroyer);
		fleet.add(destroyer);
		freeFields.removeAll(freeFields);
		
		submarine();
		checkFields(submarine);
		randomize(submarine);
		placeShip(submarine);
		fleet.add(submarine);
		freeFields.removeAll(freeFields);
		
		cruiser();
		checkFields(cruiser);
		randomize(cruiser);
		placeShip(cruiser);
		fleet.add(cruiser);
		freeFields.removeAll(freeFields);
		
		patrolBoat();
		checkFields(patrolBoat);
		randomize(patrolBoat);
		placeShip(patrolBoat);
		fleet.add(patrolBoat);
		freeFields.removeAll(freeFields);
		
		GameData gd = GameData.getInstance();
		gd.setOppFleet(fleet);
		gd.setOpponentsTable(table);
	}
	

	public boolean[][] getTable() { 
		return table;
	}
	
	public void setFleet(ArrayList<Ship> fleet) {
		this.fleet = fleet;
	}

}