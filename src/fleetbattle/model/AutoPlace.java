package fleetbattle.model;

import java.util.ArrayList;
import java.util.Random;


/*
 *  A hajók random elhelyezése történik ebben a class-ban. 
 */
public class AutoPlace {
	
	private static AutoPlace instance;
	
	public static AutoPlace getInstance() {
		if(instance == null) {
			instance = new AutoPlace();
		}
		return instance;
	}
	
	public static boolean ownFleetPlaced = false;

	
	private static Ship carrier;
	private static Ship destroyer;
	private static Ship submarine;
	private static Ship cruiser;
	private static Ship patrolBoat;
	
	private static boolean[][]table = new boolean[10][10]; // ez tárolja a hajókat a táblán.
	
	protected static Random rnd = new Random(); // ezt nem kell magyarázni :)
	
	private static ArrayList<Ship>fleet = new ArrayList<Ship>(); // ez tárolja a már letett hajókat.
	
	/*
	 *  A fieldsCoordinates és freeFields listák szintén az elrendezéshez kellenek.
	 *   a checkFields() metódus fogja használni mezők ellenőrzéséhez.
	 *   
	 *   
	 *   fieldsCoordinates: a mezők koordinátáját tárolja
	 */

	protected static ArrayList<Integer[]> fieldsCoordinates = new ArrayList<>();
	
	/*
	 * Hajók elhelyezésénél ebből választja ki random szerűen azt a mezőt,
	 * ahonnan "indulhat" a hajó.		
	 */
	protected ArrayList<Integer[]> freeFields = new ArrayList<>();

	public ArrayList<Ship> getFleet() {
		return fleet;
	}
	
	/*
	 * 
	 * Hajók random elhelyezése:
	 */
	public void setupOfFields() { // fieldsCoordinates lista inicializálása. 
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				fieldsCoordinates.add(new Integer[] {i,j});
			}
		}
	}
	/*
	 *	Hajók létrhozása, és az irányuk beállítésa véletlenszerúen 	
	 */
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

	
	/* 
	 * Mezők ellenőrzése, hogy a hajó letehető-e az adott mezőre.
	 */

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
	/*
	 * Az alábbi két metódus (az egyik függőleges, a másik vízszintes irányban) azt ellenőrzi le, 
	 * hogy a hajó az adott helyre, a kezdőpontjától számítva üres helyen lenne-e.
	 */
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
	/*
	 * A két következő metódus(szintén két irányban) azt vizsgálja meg, hogy adott mezőből indulva
	 * egyáltalán elfér-e a táblán (vagyis nem lóg-e ki).	
	 */
	private boolean checkStartPointByVerticalDirection(Integer[] startPoint, Ship ship) {
		if(startPoint[0] > 9-(ship.getSize()-1)) return false;
		return true;
	}
	
	private boolean checkStartPointByHorizontalDirection(Integer[] startPoint, Ship ship) {
		if(startPoint[1] > 9-(ship.getSize()-1)) return false;
		return true;
	}
	
	private boolean randomDirection() { // a hajó irányát randomizálja
		return rnd.nextBoolean();
	}
	/*
	 * Itt választja ki a hajó kezdő koordinátáját a freeFields listából
	 */
	private void randomize(Ship ship) {
		int temp = rnd.nextInt(freeFields.size());
		Integer[] startPoints = freeFields.get(temp);
		ship.setStartpoint(startPoints[0], startPoints[1]);
	}
	/*
	 * Ez állítja be a hajó összes koordinátáját, és rögzíti a table-ben.
	 */
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
	/*
	 * Ez helyezi el végül mind az 5 hajót a táblán.
	 * Minden hajó elhelyezése után szükséges a freeFields törlése!!!
	 */
	public void placeAll() {
		
		carrier();
		checkFields(carrier);
		randomize(carrier);
		placeShip(carrier);
		fleet.add(carrier);
		freeFields.removeAll(freeFields);
		
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
		gd.clearOwnFleet();
		gd.setOwnFleet(fleet);
		gd.setOwnTable(table);
	}
	
	/*
	 * A hajók lerakása itt véget ért.
	 */

	public void reset() { // ez a MainApp-al kommunikál, amikor autoplace-re nyomunk.
		fleet.removeAll(fleet);
		for(int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				table[i][j] = false;
			}
		}
	}

	public boolean[][] getTable() { //ez szintén a MainAppal kommunikál, átadja a table-t
		return table;
	}
	
	public boolean isOwnFleetPlaced() {
		return ownFleetPlaced;
	}

	public void setOwnFleetPlaced(boolean ownFleetPlaced) {
		AutoPlace.ownFleetPlaced = ownFleetPlaced;
	}
	
	public Ship getShip(Ship ship) {
		return ship;
	}

	private AutoPlace() {
		this.freeFields = new ArrayList<>();
	}




}