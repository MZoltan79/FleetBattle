package fleetbattle.model;

import java.util.ArrayList;
import java.util.Random;


public class Game {
	
private static boolean[][]table = new boolean[10][10];
	
	private static boolean[][]hits = new boolean[10][10];
	
	private static char[][]visual = new char[10][10];

	
	
	protected static Random rnd = new Random();
	
	protected static ArrayList<Ship>fleet = new ArrayList<Ship>();
	
	
	// A checkFields() metódus fogja használni mezők ellenőrzéséhez. A generikus még nem biztos!!!
	protected static ArrayList<Integer[]> fields = new ArrayList<>();
	
	
	private static int hitsremaining = 15;
	
	private static int turns = 0;
	
	private static int y;
	
	private static int x;

	private static int yHit;
	
	private static int xHit;
	
	private static Ship carrier;/* = Ship.CARRIER;*/
	private static Ship destroyer;
	private static Ship submarine;
	private static Ship cruiser;
	private static Ship patrolBoat;
	
	
	private static char sunk = '⬛';

	private static char hit = '❌';
	
	private static char missed = '❎';
	
	private static char water = '⬜';
	
	private void setupOfFields() {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				fields.add(new Integer[] {i,j});
			}
			
		}
	}
	
	public void reset() {
		turns = 0;
		fleet.removeAll(fleet);
		hitsremaining = 15;
		for(int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				table[i][j] = false;
			}
		}
	}
	/*
	 * Mezők ellenőrzése, hogy a hajó letehető-e az adott mezőre.
	 */
	private void checkFields(Ship ship) {
		int size = ship.getSize();
		for(int i = 0; i < fields.size(); i++) {
			for(int j = 0; j < size; j++) {
				
			}
		}
	}
	
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
	
	private boolean RandomDirection() {
		return rnd.nextBoolean();
	}
	
	private void Randomize(Ship ship) {
		
		
		
		
//		while(true) {
//			
//		
//		if(ship.isVertical()) {
//			x = rnd.nextInt(10-(ship.getSize()-1));
//			y = rnd.nextInt(10);
//		} else {
//			x = rnd.nextInt(10);
//			y = rnd.nextInt(10-(ship.getSize()-1));
//		}
//		if(table[x][y] == false) break;
//		}
//		
//		if(ship.isVertical()) {
//		x = rnd.nextInt(10-(ship.getSize()-1));
//		y = rnd.nextInt(10);
//		} else {
//			x = rnd.nextInt(10);
//			y = rnd.nextInt(10-(ship.getSize()-1));
//		}
//		if(table[x][y] == true) Randomize(ship);
	}
	
	public void placeAll() {
		while(fleet.size() != 5) {
			
			
		Carrier();
		PlaceShip(carrier);
		
		PatrolBoat();
		PlaceShip(patrolBoat);
		
		Cruiser();
		PlaceShip(cruiser);
		
		Submarine();
		PlaceShip(submarine);
		

		Destroyer();
		PlaceShip(destroyer);
		}
	
	}
	
	private void PlaceShip(Ship ship) {
		
		
		
		/*	
		boolean freeplace = true;
		if(ship.isVertical() == false) {
			for(int i = ship.getCoordinates()[1]; i < ship.getCoordinates()[3]+1;i++) {
				if(table[ship.getCoordinates()[0]][i] == true) {
					freeplace = false;
					break;
				}
			}
		} else {
			for(int i = ship.getCoordinates()[0]; i < ship.getCoordinates()[2]+1; i++) {
				if(table[i][ship.getCoordinates()[1]] == true) {
					freeplace = false;
					break;
				}
			}
		}
		if (freeplace == true) {
			if(ship.isVertical() == false) {
				for(int i = ship.getCoordinates()[1]; i < ship.getCoordinates()[3]+1;i++) {
					table[ship.getCoordinates()[0]][i] = true;
				}
				fleet.add(ship);
			} else {
				for(int i = ship.getCoordinates()[0]; i < ship.getCoordinates()[2]+1; i++) {
					table[i][ship.getCoordinates()[1]] = true;
				}
				fleet.add(ship);
			}
		} else {
			int shipsize = ship.getSize();
			switch(shipsize) {
			case 1: PatrolBoat(); break;
			case 2: Cruiser(); break;
			case 3: Submarine(); break;
			case 4: Destroyer(); break;
			case 5: Carrier(); break;
			}
		}
		 */
	}
	private void Carrier() {
		carrier = Ship.CARRIER;
		carrier.setVertical(RandomDirection());
		Randomize(carrier);
		carrier.setStartpoint(x, y);
		if(carrier.isVertical() == true) {
			carrier.setEndpoint(x + (carrier.getSize()-1), (y));
		} else {
			carrier.setEndpoint(x, (y + (carrier.getSize()-1)));
		}
	}

	private void Destroyer() {
		destroyer = Ship.DESTROYER;
		destroyer.setVertical(RandomDirection());
		Randomize(destroyer);
		destroyer.setStartpoint(x, y);
		if(destroyer.isVertical() == true) {
			destroyer.setEndpoint(x + (destroyer.getSize()-1), (y));
		} else {
			destroyer.setEndpoint(x, (y + (destroyer.getSize()-1)));
		}
	}

	private void Submarine() {
		submarine = Ship.SUBMARINE;
		submarine.setVertical(RandomDirection());
		Randomize(submarine);
		submarine.setStartpoint(x, y);
		if(submarine.isVertical() == true) {
			submarine.setEndpoint(x + (submarine.getSize()-1), (y));
		} else {
			submarine.setEndpoint(x, (y + (submarine.getSize()-1)));
		}
	}

	private void Cruiser() {
		cruiser = Ship.CRUISER;
		cruiser.setVertical(RandomDirection());
		Randomize(cruiser);
		cruiser.setStartpoint(x, y);
		if(cruiser.isVertical() == true) {
			cruiser.setEndpoint(x + (cruiser.getSize()-1), (y));
		} else {
			cruiser.setEndpoint(x, (y + (cruiser.getSize()-1)));
		}
	}

	private void PatrolBoat() {
		patrolBoat = Ship.PATROLBOAT;
		Randomize(patrolBoat);
		patrolBoat.setStartpoint(x, y);
		patrolBoat.setEndpoint(x, y);
		
	}
	
	
	
	

	public static void main(String[] args) {
		
		Game game = new Game();
		game.placeAll();
		
		
//		main.placeAll();
//	
//		System.out.println(fleet.size());
//		for(int i = 0; i < 10; i++) {
//			for(int j = 0; j < 10; j++) {
//				if(table[i][j]==true) {
//					System.out.print(hit);
//				} else {
//					System.out.print(water);
//				}
//			}
//			System.out.println();
//		}
	}



	public boolean[][] getTable() {
		return table;
	}

}
