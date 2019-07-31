package fleetbattle.model;

public enum Ship {
	
	CARRIER(5,5),
	DESTROYER(4,4),
	SUBMARINE(3,3),
	CRUISER(2,2),
	PATROLBOAT(1,1);
	
	protected final int size;
	protected boolean vertical;
	protected int[]coordinates;
	protected boolean sunk = false;
	int hit;
	public boolean isPlaced = false;
	
	
	private Ship(int size, int hit) {
		this.size = size;
		this.hit = hit;
		if(size<2) {
			this.coordinates = new int[4];
		} else {
			this.coordinates = new int[size*2]; 
		}
		
	}

	
	
	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public boolean isSunk() {
		return sunk;
	}

	public void setSunk() {
		this.sunk = !sunk;
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
	public void printCoordinates() {
		
	}
	
	public void setStartpoint(int x, int y) {
		coordinates[0] = x;
		coordinates[1] = y;
	}

	public void setEndpoint(int x, int y) {
		coordinates[coordinates.length-2] = x;
		coordinates[coordinates.length-1] = y;
	}

	
	
}
