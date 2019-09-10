package fleetbattle.model;

public class Ship {
	
	protected String name;
	protected int size;
	protected boolean vertical;
	protected int[]coordinates;
	protected boolean sunk;
	public boolean isPlaced;
	
	
	public Ship(String name) {
		this.name = name;
		sunk = false;
		isPlaced = false;
		switch(name) {
		case "carrier": size = 5; break;
		case "destroyer" : size = 4; break;
		case "submarine" : size = 3; break;
		case "cruiser" : size = 2; break;
		case "patrolboat" : size = 1; break;
		}
		if(size<2) {
			coordinates = new int[4];
		} else {
			coordinates = new int[size*2]; 
		}
		
	}

	


	public boolean isSunk() {
		return sunk;
	}

	public void setSunk() {
		sunk = true;
	}

	public void setSunk(boolean sunk) {
		this.sunk = sunk;
	}

	
	public Integer getSize() {
		return size;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	public boolean isVertical() {
		return vertical;
	}




	public int[] getCoordinates() {
		return coordinates;
	}
	
	public void setStartpoint(int x, int y) {
		coordinates[0] = x;
		coordinates[1] = y;
	}

	public void setEndpoint(int x, int y) {
		coordinates[coordinates.length-2] = x;
		coordinates[coordinates.length-1] = y;
	}
	public void setCoordinates(int x, int y) {
		if(vertical) {
			for(int i = 1; i < size; i++) {
				coordinates[i*2] = x;
				coordinates[(i*2)+1] = y+i;
			}
				
		} else {
			for(int i = 1; i < size; i++) {
				coordinates[i*2] = x+i;
				coordinates[(i*2)+1] = y;
			}
		}
		if(size == 1) {
			setEndpoint(coordinates[0], coordinates[1]);
		}
	}
	
	public void printCoordinates() {
		System.out.println(name);
		for(int i = 0; i < this.coordinates.length; i = i+2) {
			System.out.print(coordinates[i] +","+ coordinates[i+1] + " - ");
		}
		System.out.println();
	}
	
	public String getName() {
		return name;
	}

	public void printShip() {
		System.out.println(name);
		System.out.println("size: " + size);
		System.out.println("vertical: " + vertical);
		System.out.println("placed: " + isPlaced);
		System.out.print("coordinates: ");
		for(int i = 0; i < coordinates.length; i++) {
			System.out.print(coordinates[i] + ";");
		}
		System.out.println("\n");
	}
	
	public void checkEndPoints() {
		int swap;
		if(coordinates[0] > coordinates[coordinates.length-2]) {
			swap = coordinates[0];
			coordinates[0] = coordinates[coordinates.length-2];
			coordinates[coordinates.length-2] = swap;
		}
		if(coordinates[1] > coordinates[coordinates.length-1]) {
			swap = coordinates[1];
			coordinates[1] = coordinates[coordinates.length-1];
			coordinates[coordinates.length-1] = swap;
		}
	}


	public String toString() {
		return name + ", verical: " + vertical + ", sunk: " + sunk + ", coordinates: " + coordinates[0] + ":" + coordinates[1]; 
	}

	
	
}
