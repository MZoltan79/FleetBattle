package fleetbattle.model;

public class Player {
	
	private String nickName;
	private boolean guest;
	private int gamesPlayed;
	private int gamesWon;
	private int hits;
	

	public Player() {
		this.nickName = "Guest player";
		this.hits = 0;
		this.guest = true;
		this.gamesPlayed = 0;
		this.gamesWon = 0;
	}

	@Override
	public String toString() {
		return nickName + ";" + gamesPlayed + ";" + gamesWon ;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
	

}
