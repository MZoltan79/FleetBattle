package fleetbattle.model;

import java.util.ArrayList;
import java.util.Random;

import fleetbattle.MainApp;
import fleetbattle.view.BattleLayoutController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GamePlay extends BorderPane{
	
	private static GamePlay instance;
	
	public static GamePlay getInstance() {
		if(instance == null) {
			instance = new GamePlay();
		}
		return instance; 
	}
	
	private static boolean hit = false;
	private static Random rnd = new Random();
	private static BattleLayoutController controller;
	
	MainApp mainApp;
	AutoPlace ap;
	AutoPlaceOpponent ao;
	GameData gd;
	
	private boolean singlePlayer;
	private boolean[][] ownTable;
	private boolean[][] ownHits;
	private boolean[][] opponentsTable;
	private boolean[][] opponentsHits;
	private boolean turn;
	int i = 0;

	Integer prevX;
	Integer prevY;
	private int a = 0;
	private int b = 0;
	private Integer x = 10000;
	private Integer y = 10000;
	private Integer turns = 1;
	ArrayList<Ship> ownFleet;
	ArrayList<Ship> opponentsFleet;
	Canvas canvas;
	GraphicsContext gc;
	
	
	
	private GamePlay() {
		this.setMinSize(398, 398);
		this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

		mainApp = new MainApp();
		gd = GameData.getInstance();
		ap = AutoPlace.getInstance();
		ao = AutoPlaceOpponent.getInstance();

		System.out.println("AiOpponent placeAll() előtt");
		for(Ship s: gd.getOwnFleet()) {
			System.out.println(s.name() + " koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
		}
		
		ao.setupOfFields();
		ao.placeAll();
		
		System.out.println("AiOpponent placeAll() után");
		for(Ship s: gd.getOwnFleet()) {
			System.out.println(s.name() + " koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
		}
		
		ownFleet = gd.getOwnFleet();
		singlePlayer = mainApp.isSinglePlayer();
		ownTable = mainApp.getTable();
		opponentsHits = new boolean[10][10];
		ownHits = new boolean[10][10];
		turn = rnd.nextBoolean();
		canvas = new Canvas(398, 398);
		this.setCenter(canvas);
		gc = canvas.getGraphicsContext2D();
		opponentsTable = ao.getTable();
		opponentsFleet = gd.getOppFleet();
	}
	

	public void startSinglePlayer() {
			controller.drawOwnCanvas();
			drawOpponentsTable();
			System.out.println();
//			for(Ship s: gd.getOwnFleet()) {
//				System.out.println(s.name() + " saját koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
//			}
//			for(Ship s: gd.getOppFleet()) {
//				System.out.println(s.name() + " ellen koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
//			}
			
			ownTurn();
	}
	
	public void ownTurn() {
			canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, this::mouseMovedOwnHit);
	}
	
	
	public void mouseMovedOwnHit(MouseEvent ev) {
		x = (int)((ev.getX()-51)/30);
		y = (int)(ev.getY()-51)/30;
		
		drawOwnHit();
		checkOppFleet();
		drawOpponentsSunkedShips();
		controller.setOppFleetIndicator(countFleetSize(gd.getOppFleet()).toString());
		if(!hit) {
			aITurn();
		}
	}

	public void aITurn() {
		aiHit();
		setOpponentHit();
		controller.drawOpponentsHit();
		controller.checkOwnFleet();
		controller.drawOwnSunkedShips();
		if(!hit) {
			ownTurn();
			}
	}
	
	public void setOpponentHit() {
		if(opponentsHits[a][b] == true) {
			hit = true;
			aITurn();
		} else if(opponentsHits[a][b] == false && ownTable[a][b] == false) {
			opponentsHits[a][b] = true;
			hit = true;
		} else if(opponentsHits[a][b] == false && ownTable[a][b] == true) {
			opponentsHits[a][b] = true;
			hit = false;
		}
	}
	
	
	public void aiHit() {
		a = rnd.nextInt(10);
		b = rnd.nextInt(10);
	}
	
	public void drawOwnHit() {
		Font font = new Font(30);
		gc.setFont(font);
		if(ownHits[x][y] == true) {
			controller.setHitIndicator("Already hit there...");
		} else if(ownHits[x][y] == false && opponentsTable[x][y] == false) {
			ownHits[x][y] = true;
			hit = false;
			controller.setHitIndicator("Missed");
			gc.setFill(Color.DARKRED);
			gc.fillText("X", (x*30)+52, (y*30)+75);
		} else if(ownHits[x][y] == false && opponentsTable[x][y] == true) {
			ownHits[x][y] = true;
			hit = true;
			controller.setHitIndicator("Hit!");
			gc.setFill(Color.DARKRED);
			gc.fillOval((x*30)+49, (y*30)+49,28,28);
		}
		
		ownHits[x][y] = true;
	}

	public void setHit(boolean hit) {
		GamePlay.hit = hit;
	}


	public void checkOppFleet() {
		for(Ship s: gd.getOppFleet()) {
			boolean sunk = true;
				for(int i = 0; i < s.getSize(); i++) {
					if(ownHits[s.getCoordinates()[0+(i*2)]][s.getCoordinates()[1+(i*2)]] == false) {
						sunk = false;
					}
				}
			if(sunk) s.setSunk();
		}
	}
	
	public void drawOpponentsSunkedShips() {
		gc.setFill(Color.DARKRED);
		for(Ship s: gd.getOppFleet()) {
			if((s.isVertical() == true) && s.isSunk()) {
				for(int i = 0; i < s.getSize(); i++) {
					gc.fillRect((s.getCoordinates()[0]*30 + 49), (s.getCoordinates()[1+i*2]*30 + 49), 28, 28);
				}
			} else if((s.isVertical() == false) && s.isSunk()){
				for(int i = 0; i < s.getSize(); i++) {
					gc.fillRect((s.getCoordinates()[0+i*2]*30 + 49), (s.getCoordinates()[1]*30 + 49), 28, 28);
				}
			}
			
		}
		
	}

	
	public void drawTable() {
		Font font = new Font(30);
		gc.setFont(font);
		Character[] columns = {'A','B','C','D','E','F','G','H'};
		gc.setFill(Color.CADETBLUE);
		for(int i = 0; i < columns.length; i++) {
			gc.fillText(columns[i].toString(), i*30 + 53, 39);
		}
		gc.fillText("I", 297, 39);
		gc.fillText("J", 327, 39);
		for(Integer i = 1; i < 10; i++) {
			gc.fillText(i.toString(), 18, (i-1)*30 + 75);
		}
		
		gc.fillText("10", 12, 345);
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				gc.setFill(Color.CADETBLUE);
				gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
			}
		}
	}
	
	public void drawOpponentsTable() {
		Font font = new Font(30);
		gc.setFont(font);
		Character[] columns = {'A','B','C','D','E','F','G','H'};
		gc.setFill(Color.CADETBLUE);
		for(int i = 0; i < columns.length; i++) {
			gc.fillText(columns[i].toString(), i*30 + 53, 39);
		}
		gc.fillText("I", 297, 39);
		gc.fillText("J", 327, 39);
		for(Integer i = 1; i < 10; i++) {
			gc.fillText(i.toString(), 18, (i-1)*30 + 75);
		}
		gc.fillText("10", 12, 345);
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				gc.setFill(Color.CADETBLUE);
				gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
				if(ownHits[i][j] == true && opponentsTable[i][j] == false) {
					gc.setFill(Color.DARKRED);
					gc.fillText("X", (i*30)+52, (j*30)+75);
				} else if(ownHits[i][j] == true && opponentsTable[i][j] == true) {
					gc.setFill(Color.DARKRED);
					gc.fillOval((x*30)+49, (y*30)+49,28,28);
				}
			}
		}
	}
	
	

	public int getA() {
		return a;
	}


	public int getB() {
		return b;
	}


	public boolean[][] getOwnTable() {
		return ownTable;
	}


	public boolean[][] getOpponentsHits() {
		return opponentsHits;
	}


	public void drawOwnTable() {
		Font font = new Font(30);
		gc.setFont(font);
		Character[] columns = {'A','B','C','D','E','F','G','H'};
		gc.setFill(Color.CADETBLUE);
		for(int i = 0; i < columns.length; i++) {
			gc.fillText(columns[i].toString(), i*15 + 20, 19);
		}
		gc.fillText("I", 150, 19);
		gc.fillText("J", 170, 19);
		for(Integer i = 1; i < 10; i++) {
			gc.fillText(i.toString(), 18, (i-1)*30 + 75);
		}
		gc.fillText("10", 6, 175);
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				gc.setFill(Color.CADETBLUE);
				gc.fillRect((i*15 + 25), (j*15 + 25), 14, 14);
				if(opponentsHits[i][j] == true && ownTable[i][j] == false) {
					gc.setFill(Color.DARKRED);
					gc.fillText("X", (i*15)+26, (j*15)+38);
				} else if(opponentsHits[i][j] == true && ownTable[i][j] == true) {
					gc.setFill(Color.DARKRED);
					gc.fillOval((a*15)+25, (b*15)+25,14,14);
				}
			}
		}
	}
	
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	
	public Integer countFleetSize(ArrayList<Ship> list) {
		Integer result = 5;
		for(Ship s:list) {
			if (s.isSunk()) {
				result--;
			}
		}
		return result;
	}


	public Integer getTurns() {
		return turns;
	}

	public int getX() {
		return x;
	}

	public ArrayList<Ship> getOwnFleet() {
		return ownFleet;
	}

	public ArrayList<Ship> getOpponentsFleet() {
		return opponentsFleet;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isTurn() {
		return turn;
	}
	
	@SuppressWarnings("static-access")
	public void setController(BattleLayoutController controller) {
		this.controller = controller;
	}
	
	public void addShipToOppFleet(Ship ship) {
		opponentsFleet.add(ship);
	}
	
	
	

}