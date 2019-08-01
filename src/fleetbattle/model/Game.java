package fleetbattle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 *  A hajók random elhelyezése történik ebben a class-ban, illetve
 *  egyéb adatokat is innen nyerünk ki. később még ki fogok venni 
 *  felesleges adatokat, de még nem tudom mi kellhet még innen... 
 */
public class Game {

	private static Ship carrier;
	private static Ship destroyer;
	private static Ship submarine;
	private static Ship cruiser;
	private static Ship patrolBoat;
	
	private static boolean[][]table = new boolean[10][10]; // ez tárolja a hajókat a táblán.
	
	private static boolean[][]hits = new boolean[10][10]; // ez tárol(hat)ja a lövéseket.
/*
 * A játék eredetileg konzolra készült, a visual nevű mátrix a konzolon való megjelenítésre
 * készült. Nagy valószínűséggel nem lesz rá szükség, de tesztelés végett megtartanám, mert
 * itt azért egyszerűbb tesztelni mint a MainApp-ban.	
 */
	private static char[][]visual = new char[10][10];

	protected static Random rnd = new Random(); // ezt nem kell magyarázni :)
	
	protected static ArrayList<Ship>fleet = new ArrayList<Ship>(); // ez tárolja a már letett hajókat.
	
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
	protected static ArrayList<Integer[]> freeFields = new ArrayList<>();
	
	
	private static int hitsremaining = 15;
	
	private static int turns = 0;
	
	private static int y;
	
	private static int x;

	private static int yHit;
	
	private static int xHit;
	
	
	
	private static char sunk = '⬛';

	private static char hit = '❌';
	
	private static char missed = '❎';
	
	private static char water = '⬜';

	public List<Ship> getFleet() {
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
		carrier = Ship.CARRIER;
		carrier.setVertical(randomDirection());
	}

	private void destroyer() {
		destroyer = Ship.DESTROYER;
		destroyer.setVertical(randomDirection());
	}

	private void submarine() {
		submarine = Ship.SUBMARINE;
		submarine.setVertical(randomDirection());
	}

	private void cruiser() {
		cruiser = Ship.CRUISER;
		cruiser.setVertical(randomDirection());
	}

	private void patrolBoat() {
		patrolBoat = Ship.PATROLBOAT;
		patrolBoat.setVertical(randomDirection());
	}

	
	/* 
	 * Mezők ellenőrzése, hogy a hajó letehető-e az adott mezőre.
	 */

	private void checkFields(Ship ship) {
		int size = ship.getSize();
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
		if(!ship.isVertical()) {
			ship.setEndpoint(ship.getCoordinates()[0] + (ship.getSize()-1), (ship.getCoordinates()[1]));
		} else {
			ship.setEndpoint(ship.getCoordinates()[0], (ship.getCoordinates()[1] + (ship.getSize()-1)));
		}
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
		System.out.println("game carrier: " + carrier.coordinates[0] + "," + carrier.coordinates[1] 
				+ " " + carrier.coordinates[8] + "," + carrier.coordinates[9]);
		System.out.println(carrier.isVertical());
		
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
	}
	
	/*
	 * A hajók lerakása itt véget ért.
	 */

	public void reset() { // ez a MainApp-al kommunikál, amikor autoplace-re nyomunk.
		turns = 0;
		fleet.removeAll(fleet);
		hitsremaining = 15;
		for(int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				table[i][j] = false;
			}
		}
	}

	public boolean[][] getTable() { //ez szintén a MainAppal kommunikál, átadja a table-t
		return table;
	}
	
	public void setFleet(ArrayList<Ship> fleet) {
		Game.fleet = fleet;
	}

	
	
	
	
	
	
	/*
	 * innentől lefele:		 UNDER CONSTRUCTION !!!
	 * 
	 */
	private void hit() {
		System.out.println("Add meg a sor számát 0-9-ig! sor = ");
//		xHit = sc.nextInt();
		System.out.println("Add meg az oszlop számát 0-9-ig! sor = ");
//		yHit = sc.nextInt();
		if(hits[xHit][yHit]== true) {
			System.out.println("ide már lőttél!");
			hit();
		} else {
			if(table[xHit][yHit]==false) {
				System.out.println("Nem talált!");
//				visual[xHit][yHit]=missed;
				hits[xHit][yHit] = true;
				turns++;
			} else {
				System.out.println("Talált!");
				hits[xHit][yHit] = true;
//				visual[xHit][yHit]=hit;
				turns++;
				hitsremaining--;
			}
		}
	}
	
	private void checkShip(Ship ship) {
		int tmp[] = ship.getCoordinates();
		if(ship.isVertical() == true) {
			for(int i = ship.getCoordinates()[0]; i < ship.getCoordinates()[2]+1; i++) {
				if(table[i][ship.getCoordinates()[1]] == false) ship.hit--;
			}
		} else {
			for(int i = ship.getCoordinates()[1]; i < ship.getCoordinates()[3]+1;i++) {
				if(table[ship.getCoordinates()[0]][i] == false) ship.hit--;
			}
		}
		if (ship.getHit()==0) {
			ship.setSunk();
			if(ship.isVertical() == true) {
				for(int i = ship.getCoordinates()[0]; i < ship.getCoordinates()[2]+1; i++) {
					visual[i][ship.getCoordinates()[1]] = sunk;
				}
			} else {
				for(int i = ship.getCoordinates()[1]; i < ship.getCoordinates()[3]+1;i++) {
					visual[ship.getCoordinates()[0]][i] = sunk;
				}
			}
		}
	}
	
	private void checkAll() {
		for(Ship s:fleet) {
			checkShip(s);
		}
	}
	
	public Ship getShip(Ship ship) {
		return ship;
	}
	

	/*
	 * main függvényre szerintem nem lesz szükség, egyelőre megtartanám lehetséges
	 * tesztelések végett.
	 */
	
	public static void main(String[] args) {
		
		Game game = new Game();
		game.setupOfFields();
		game.placeAll();
		System.out.println(table[0][0]);
		System.out.println("freefields size = " + freeFields.size());
		System.out.println("fleet size = " + fleet.size());
		System.out.println(fieldsCoordinates.get(1)[0] + ", " + fieldsCoordinates.get(1)[1]);
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(table[i][j]==true) {
					System.out.print(hit);
				} else {
					System.out.print(water);
				}
			}
			System.out.println();
		}
		fleet.forEach(e -> {
			System.out.println(e.name() + ": " + e.getCoordinates()[0] + ", " +
					e.getCoordinates()[1] + ", " +
					e.getCoordinates()[e.getCoordinates().length-2] + ", " +
					e.getCoordinates()[e.getCoordinates().length-1]);
		});
		System.out.println(carrier.name());
		game.reset();
		System.out.println(table[0][0]);
		System.out.println("freefields size = " + freeFields.size());
		System.out.println("fleet size = " + fleet.size());
		System.out.println(fieldsCoordinates.get(1)[0] + ", " + fieldsCoordinates.get(1)[1]);
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(table[i][j]==true) {
					System.out.print(hit);
				} else {
					System.out.print(water);
				}
			}
			System.out.println();
		}
		fleet.forEach(e -> {
			System.out.println(e.getCoordinates()[0] + ", " +
					e.getCoordinates()[1] + ", " +
					e.getCoordinates()[2] + ", " +
					e.getCoordinates()[3]);
		});
	}




}
