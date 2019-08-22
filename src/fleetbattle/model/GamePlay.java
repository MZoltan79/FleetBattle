package fleetbattle.model;

import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.Receiver;

import communication.Connection;
import fleetbattle.MainApp;
import fleetbattle.view.BattleLayoutController;
import fleetbattle.view.GameOverLayoutController;
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
	private MainApp mainApp;
	private AutoPlaceOpponent ao;
	GameData gd;
	
	private Random rnd = new Random();
	private BattleLayoutController controller;
	private GameOverLayoutController goController;
	Connection conn;
	
	private boolean connected;
	private boolean[][] ownHits;
	private boolean[][] opponentsHits;
	private boolean turn;
	private boolean ownTurnWasFirst;
	private int a = 0;
	private int b = 0;
	private Integer x = 10000;
	private Integer y = 10000;
	private Integer turns;
	private ArrayList<Ship> ownFleet;
	private ArrayList<Ship> opponentsFleet;
	private Canvas canvas;
	private GraphicsContext gc;
	
	
	
	private GamePlay() {
		this.setMinSize(398, 398);
		this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		turns = 1;
		turn = rnd.nextBoolean();
		ownTurnWasFirst = turn;
		mainApp = new MainApp();
		gd = GameData.getInstance();
		ao = AutoPlaceOpponent.getInstance();
		ao.setupOfFields();
		ao.placeAll();
		ownFleet = gd.getOwnFleet();
		opponentsHits = new boolean[10][10];
		ownHits = new boolean[10][10];
		canvas = new Canvas(398, 398);
		this.setCenter(canvas);
		gc = canvas.getGraphicsContext2D();
		opponentsFleet = gd.getOpponentsFleet();
		goController = new GameOverLayoutController();
	}
	

	public boolean isOwnTurnWasFirst() {
		return ownTurnWasFirst;
	}


	public void startSinglePlayer() {
			controller.drawOwnCanvas();
			drawOpponentsTable();
			System.out.println();
			if(!turn) {
				aITurn();
			} 
			ownTurn();
	}
	
	public void startMultiPlayer() {
		conn = new Connection();
		conn.setDaemon(true);
		conn.start();
		String ownData = buildOwnData();
		initializeOpponentsDataMP();
		conn.sendData(ownData);
		controller.drawOwnCanvas();
		drawOpponentsTable();
		System.out.println();
		ownTurn();
	}
	public void initializeOpponentsDataMP() {
		String opponentsData = conn.receiveData();
		String[] tmp = opponentsData.split(";");
		for(int i = 0; i < 5; i++) {
			gd.opponentsFleet.add(new Ship("carrier"));
			gd.opponentsFleet.get(i).setVertical(Boolean.parseBoolean(tmp[0]));									// ship isVertical()
			gd.opponentsFleet.get(i).setStartpoint(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));			// ship getCoordinates() (index: 0 és 1)
			gd.opponentsFleet.get(i).setCoordinates(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
		}
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				gd.opponentsTable[i][j] = Boolean.parseBoolean(tmp[3+i]);
			}
		}
	}
	
	public String buildOwnData() {
		StringBuilder data = new StringBuilder();
		for(int i = 0; i < 5; i++) {
			data.append(gd.getOwnFleet().get(i).isVertical() + ";" + gd.getOwnFleet().get(i).getCoordinates()[0] + 
					";" + gd.getOwnFleet().get(i).getCoordinates()[1]);
		}
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				data.append(";" + gd.getOwnTable()[i][j]);
			}
		}
		
		return data.toString();
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
		controller.setOppFleetIndicator(countFleetSize(gd.getOpponentsFleet()).toString());
		controller.setOwnFleetIndicator(countFleetSize(gd.getOwnFleet()).toString());
		if((countFleetSize(gd.opponentsFleet) == 0) || (countFleetSize(gd.getOwnFleet()) == 0)) {
			mainApp.showGameOverLayout();
		}
	
	}

	public void drawOwnHit() {
		Font font = new Font(30);
		gc.setFont(font);
		if(ownHits[x][y] == true) {
			controller.setHitIndicator("Already hit there...");
		} else if(ownHits[x][y] == false && gd.getOpponentsTable()[x][y] == false) {
			ownHits[x][y] = true;
			controller.setHitIndicator("Missed");
			gc.setFill(Color.DARKRED);
			gc.fillText("X", (x*30)+52, (y*30)+75);
			changeTurn();
			if(!ownTurnWasFirst) turns++;
			controller.showTurnStat();
			aITurn();
		} else if(ownHits[x][y] == false && gd.getOpponentsTable()[x][y] == true) {
			ownHits[x][y] = true;
			controller.setHitIndicator("Hit!");
			gc.setFill(Color.DARKRED);
			gc.fillOval((x*30)+49, (y*30)+49,28,28);
			ownHits[x][y] = true;
			controller.showTurnStat();
//			if(countFleetSize(gd.getOwnFleet()) < 1 || countFleetSize(gd.getOpponentsFleet()) < 1) {
//				mainApp.showGameOverLayout();
//			}
		}
	}
	
	public void checkOppFleet() {
		for(Ship s: gd.getOpponentsFleet()) {
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
		for(Ship s: gd.getOpponentsFleet()) {
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
				if(ownHits[i][j] == true && gd.getOpponentsTable()[i][j] == false) {
					gc.setFill(Color.DARKRED);
					gc.fillText("X", (i*30)+52, (j*30)+75);
				} else if(ownHits[i][j] == true && gd.getOpponentsTable()[i][j] == true) {
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


	public boolean[][] getOpponentsHits() {
		return opponentsHits;
	}

	public void aiHit() {
		a = rnd.nextInt(10);
		b = rnd.nextInt(10);
		if(opponentsHits[a][b] && turns < 87) {
			aiHit();
		}
	}
	
	public void aITurn() {
		aiHit();
		controller.checkOwnFleet();
		controller.updateMPGui();
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
				if(opponentsHits[i][j] == true && gd.getOwnTable()[i][j] == false) {
					gc.setFill(Color.DARKRED);
					gc.fillText("X", (i*15)+26, (j*15)+38);
				} else if(opponentsHits[i][j] == true && gd.getOwnTable()[i][j] == true) {
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

	public void increaseTurns() {
		turns++;
	}
	
	public Integer getTurns() {
		return turns;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isTurn() {
		return turn;
	}
	
	public void changeTurn() {
		turn = !turn;
	}
	
	public void setController(BattleLayoutController controller) {
		this.controller = controller;
	}
	
	
	
	

}