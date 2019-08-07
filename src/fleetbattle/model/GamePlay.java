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
	
	private static Random rnd = new Random();
	private static BattleLayoutController controller;
	
	MainApp mainApp;
	AutoPlace ap;
	AiOpponent ao;
	private boolean singlePlayer;
	private boolean[][] ownTable;
	private boolean[][] ownHits;
	private boolean[][] opponentsTable;
	private boolean[][] opponentsHits;
	private boolean turn;
	int i = 0;
	public boolean isTurn() {
		return turn;
	}
	Integer prevX;
	Integer prevY;
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
		ap = new AutoPlace();
		ao = new AiOpponent();
		singlePlayer = mainApp.isSinglePlayer();
		ownTable = mainApp.getTable();
		opponentsTable = ao.getTable();
		opponentsHits = new boolean[10][10];
		ownHits = new boolean[10][10];
		turn = rnd.nextBoolean();
		ownFleet = mainApp.getFleet();
		canvas = new Canvas(398, 398);
		gc = canvas.getGraphicsContext2D();
		this.setCenter(canvas);
		ao.setupOfFields();
		ao.placeAll();
		opponentsFleet = ao.getFleet();
	}
	

	public void startSinglePlayer() {
//		ao.printFleet();
		ownTurn();
	}
	
	public void ownTurn() {
		drawTable();
		canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, this::mouseMovedOwnHit);
	}
	
	public void mouseMovedOwnHit(MouseEvent ev) {
		x = (int)((ev.getX()-51)/30);
		y = (int)(ev.getY()-51)/30;
		System.out.println(x + ", " + y);
		drawOwnHit();
		checkOppFleet();
		drawOpponentsSunkedShips();
		
	}
	
	public void drawOwnHit() {
		Font font = new Font(30);
		gc.setFont(font);
		if(ownHits[x][y] == true) {
			controller.showHitIndicator("Already hit there...");
		} else if(ownHits[x][y] == false && opponentsTable[x][y] == false) {
			controller.showHitIndicator("Missed");
			gc.setFill(Color.DARKRED);
			gc.fillText("X", (x*30)+52, (y*30)+75);
		} else if(ownHits[x][y] == false && opponentsTable[x][y] == true) {
			controller.showHitIndicator("Hit!");
			gc.setFill(Color.DARKRED);
			gc.fillOval((x*30)+49, (y*30)+49,28,28);
		}
		
		ownHits[x][y] = true;
	}
	
	public void checkOwnFleet() {
		for(Ship s: ownFleet) {
			boolean sunk = true;
			if(s.isVertical()) {
				for(int i = 0; i < s.getSize(); i++) {
					if(ownTable[s.getCoordinates()[0]][s.getCoordinates()[1+i]] == false) {
						sunk = false;
					}
				}
			} else {
				for(int i = 0; i < s.getSize(); i++) {
					if(ownTable[s.getCoordinates()[0+i]][s.getCoordinates()[1]] == false) {
						sunk = false;
					}
				}
			}
			if(sunk) s.setSunk();
		}
	}

	public void checkOppFleet() {
		for(Ship s: opponentsFleet) {
			boolean sunk = true;
			if(s.isVertical() == true) {
				for(int i = 0; i < s.getSize(); i++) {
					if(ownHits[s.getCoordinates()[0+(i*2)]][s.getCoordinates()[1+(i*2)]] == false) {
						sunk = false;
					}
				}
			} else {
				for(int i = 0; i < s.getSize(); i++) {
					if(ownHits[s.getCoordinates()[0+(i*2)]][s.getCoordinates()[1+(i*2)]] == false) {
						sunk = false;
					}
				}
			}
			if(sunk) s.setSunk();
		}
	}
	
	public void drawOpponentsSunkedShips() {
		gc.setFill(Color.DARKRED);
		for(Ship s: opponentsFleet) {
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
				if(opponentsTable[i][j] == true) {
					gc.setFill(Color.DARKRED);
					gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
				} else {
					gc.setFill(Color.CADETBLUE);
					gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
				}
			}
		}
	}
	
	public void drawOwnTable() {
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
				if(ownTable[i][j] == true) {
					gc.setFill(Color.DARKRED);
					gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
				} else {
					gc.setFill(Color.CADETBLUE);
					gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
				}
			}
		}
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void aiHit() {
		x = rnd.nextInt(10);
		y = rnd.nextInt(10);
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
	
	@SuppressWarnings("static-access")
	public void setController(BattleLayoutController controller) {
		this.controller = controller;
	}
	
	
	

}
