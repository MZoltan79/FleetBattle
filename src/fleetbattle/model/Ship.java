package fleetbattle.model;

public enum Ship {
	
	CARRIER(5,5),
	DESTROYER(4,4),
	SUBMARINE(3,3),
	CRUISER(2,2),
	PATROLBOAT(1,2);
	
	
	protected final int size;
	protected boolean vertical;
	protected int[]coordinates = new int[4];
	protected boolean sunk = false;
	int hit;
	
	
	private Ship(int size, int hit) {
		this.size = size;
		this.hit = hit;
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

	
	public int getSize() {
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
		coordinates[2] = x;
		coordinates[3] = y;
	}

	
	
}
