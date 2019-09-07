package fleetbattle.model;

import java.util.ArrayList;
import java.util.Random;
import communication.Connection;
import fleetbattle.MainApp;
import fleetbattle.view.BattleLayoutController;
import fleetbattle.view.GameOverLayoutController;
import javafx.application.Platform;
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
	private GameData gd;
	private Connection conn;
	private BattleLayoutController controller;
	private GameOverLayoutController goController;
	
	private Canvas canvas;
	private GraphicsContext gc;
	private Random rnd = new Random();
	private ArrayList<Ship> ownFleet;
	private ArrayList<Ship> opponentsFleet;
	private boolean[][] ownHits;
	private boolean[][] opponentsHits;
	private boolean turn;
	private boolean ownTurnWasFirst;
	private int a;
	private int b;
	
	@SuppressWarnings("unused")
	private int prevA;
	@SuppressWarnings("unused")
	private int prevB;
	
	private Integer x = 10000;
	private Integer y = 10000;
	private Integer turns;
	
	private GamePlay() {
		this.setMinSize(398, 398);
		this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		a = -1;
		b = -1;
		prevA = a;
		prevB = b;
		turns = 1;
		turn = rnd.nextBoolean();
		ownTurnWasFirst = turn;
		mainApp = new MainApp();
		gd = GameData.getInstance();
		conn = Connection.getInstance();
		if(mainApp.isSinglePlayer()) {
			ao = AutoPlaceOpponent.getInstance();
			ao.setupOfFields();
			ao.placeAll();
		}
		ownFleet = gd.getOwnFleet();
		opponentsHits = new boolean[10][10];
		ownHits = new boolean[10][10];
		canvas = new Canvas(398, 398);
		this.setCenter(canvas);
		gc = canvas.getGraphicsContext2D();
		opponentsFleet = gd.getOpponentsFleet();
		goController = new GameOverLayoutController();
	}


	public void startSinglePlayer() {
			gd.setPlayer1(new Player("you",0,0));
			gd.setPlayer2(new Player("AI opponent",0,0));
			controller.drawOwnCanvas();
			drawOpponentsTable();
			if(!turn) {
				opponentsTurn();
			} 
			ownTurn();
	}
	
	public void startMultiPlayer() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				initializeOpponentsDataMP();
				controller.drawOwnCanvas();
				drawOpponentsTable();
				opponentsHitMP();
				ownTurn();
				
			}
		});
	}
	
	/*
	 * 	initializeOpponentsDataMP() sets the opponent's data (fleet, map, etc...).
	 * 	gets data from opponent's buildOwnData method.
	 */
	public void initializeOpponentsDataMP() {
		
		gd.getOpponentsFleet().removeAll(gd.getOpponentsFleet());
		String data = Connection.receivedData;
		String[] tmp = data.split(";");
		if(conn.isClientOne()) {
			
		if(tmp[0].equals("true") && turn == true) {
			turn = false;
			ownTurnWasFirst = false;
		} else if(tmp[0].equals("false") && turn == false) {
			turn = true;
			ownTurnWasFirst = true;
		}
		}
		controller.showTurnStat();
		Player temp = new Player(tmp[1],Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]));
		gd.setPlayer2(temp);
		Ship carrier = new Ship("carrier");
		Ship destroyer = new Ship("destroyer");
		Ship submarine = new Ship("submarine");
		Ship cruiser = new Ship("cruiser");
		Ship patrolboat = new Ship("patrolboat");
		Ship[] tempFleet = new Ship[5];
		tempFleet[0] = carrier;
		tempFleet[1] = destroyer;
		tempFleet[2] = submarine;
		tempFleet[3] = cruiser;
		tempFleet[4] = patrolboat;
		for(int i = 0; i < 5; i++) {
			gd.getOpponentsFleet().add(tempFleet[i]);
			gd.getOpponentsFleet().get(i).setVertical(Boolean.parseBoolean(tmp[4 + (i*3)]));
			gd.getOpponentsFleet().get(i).setStartpoint(Integer.parseInt(tmp[5+ (i*3)]), Integer.parseInt(tmp[6+ (i*3)]));
			gd.getOpponentsFleet().get(i).setCoordinates(Integer.parseInt(tmp[5+ (i*3)]), Integer.parseInt(tmp[6+ (i*3)]));
		}
		
		int x = 0;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				gd.opponentsTable[i][j] = Boolean.parseBoolean(tmp[19+x]);
				x++;
			}
		}
	}
	
	/*
	 * buildOwnData() builds a String, and sends it to the opponent.
	 */
	
	public String buildOwnData() {
		Ship swap;
		int max;
		for(int i = gd.getOwnFleet().size()-1; i > 0; i--) {
			max = i;
			for(int j = i-1; j >= 0; j--) {
				if(gd.getOwnFleet().get(j).getSize() < gd.getOwnFleet().get(max).getSize()) {
					max = j;
				}
			}
			if(max != i) {
				swap = gd.getOwnFleet().get(i);
				gd.getOwnFleet().set(i, gd.getOwnFleet().get(max));
				gd.getOwnFleet().set(max, swap);
						
			}
		}
		StringBuilder data = new StringBuilder();
		data.append(turn + ";" + gd.getPlayer1().toString() + ";");
		for(int i = 0; i < 5; i++) {
			data.append(gd.getOwnFleet().get(i).isVertical() + ";" + gd.getOwnFleet().get(i).getCoordinates()[0] + 
					";" + gd.getOwnFleet().get(i).getCoordinates()[1] + ";");
		}
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				data.append(gd.getOwnTable()[i][j] + ";");
			}
		}
		
		return data.toString();
	}
	

	public void ownTurn() {
		if(mainApp.isFirstLaunch()) {
			canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, this::mouseMovedOwnHit);
			mainApp.setFirstLaunch(false);
		}
	}
	
	public void mouseMovedOwnHit(MouseEvent ev) {
		x = (int)((ev.getX()-51)/30);
		y = (int)(ev.getY()-51)/30;
		
		drawOwnHit();
		checkOppFleet();
		drawOpponentsSunkedShips();
		controller.setOppFleetIndicator(countFleetSize(gd.getOpponentsFleet()).toString());
		controller.setOwnFleetIndicator(countFleetSize(gd.getOwnFleet()).toString());
	
	}
	
	/*
	 * drawOwnHit() draws own hits.
	 */

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
			if(!ownTurnWasFirst && mainApp.isSinglePlayer()) {
				turns++;
			} else if(!ownTurnWasFirst && !mainApp.isSinglePlayer()) {
				turns++;
			}
			controller.showTurnStat();
			if(!mainApp.isSinglePlayer()) {
				conn.send(x + ";" + y);
			}
			if(mainApp.isSinglePlayer()) {
				opponentsTurn();
			}
		} else if(ownHits[x][y] == false && gd.getOpponentsTable()[x][y] == true) {
			ownHits[x][y] = true;
			controller.setHitIndicator("Hit!");
			gc.setFill(Color.DARKRED);
			gc.fillOval((x*30)+49, (y*30)+49,28,28);
			ownHits[x][y] = true;
			if(!mainApp.isSinglePlayer()) {
				conn.send(x + ";" + y);
			}
		}
	}
	
	
	/*
	 *	checkOppFleet() checks if the opponents ships are sunked. 
	 */
	
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
	
	/*
	 * drawOwnSunkedShips() draws the sunked ships, after checking them with checkOppFleet() method.
	 */
	
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
		if(countFleetSize(gd.getOpponentsFleet()) == 0) {
			if(countFleetSize(gd.getOwnFleet()) == 0) {
				gd.getPlayer2().increaseGamesWon();
				gd.getPlayer1().increaseGamesPlayed();
			}
			if(countFleetSize(gd.getOpponentsFleet()) == 0) {
				gd.getPlayer1().increaseGamesWon();
				gd.getPlayer2().increaseGamesPlayed();
			}
			mainApp.showGameOverLayout();
			resetHits();
			if(!mainApp.isSinglePlayer()) {
				conn.send("gameover");
				conn.send(gd.getPlayer1() + ";" + gd.getPlayer2());
			}
		}
		
	}

	/*
	 * 	drawOpponentsTable() draws the current map of the enemy in every turn.
	 */
	
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
	
	/*
	 * opponentsHit() generates the coordinates of AI hits.
	 * opponentsTurn draws the hits of AI.
	 */

	public void opponentsHit() {
		if(mainApp.isSinglePlayer()) {
			a = rnd.nextInt(10);
			b = rnd.nextInt(10);
			if(opponentsHits[a][b] && turns < 87) {
				opponentsHit();
			}
		}
	}
	
	public void opponentsTurn() {
		if(mainApp.isSinglePlayer()) {
			opponentsHit();
			controller.checkOwnFleet();
			controller.updateMPGui();
//			if(countFleetSize(gd.getOwnFleet()) == 0 || countFleetSize(gd.getOpponentsFleet()) == 0) {
//				mainApp.showGameOverLayout();
//				resetHits();
//			}
			
		}
	}
	
//	public void drawOwnTable() {
//		Font font = new Font(30);
//		gc.setFont(font);
//		Character[] columns = {'A','B','C','D','E','F','G','H'};
//		gc.setFill(Color.CADETBLUE);
//		for(int i = 0; i < columns.length; i++) {
//			gc.fillText(columns[i].toString(), i*15 + 20, 19);
//		}
//		gc.fillText("I", 150, 19);
//		gc.fillText("J", 170, 19);
//		for(Integer i = 1; i < 10; i++) {
//			gc.fillText(i.toString(), 18, (i-1)*30 + 75);
//		}
//		gc.fillText("10", 6, 175);
//		for(int i = 0; i < 10; i++) {
//			for(int j = 0; j < 10; j++) {
//				gc.setFill(Color.CADETBLUE);
//				gc.fillRect((i*15 + 25), (j*15 + 25), 14, 14);
//				if(opponentsHits[i][j] == true && gd.getOwnTable()[i][j] == false) {
//					gc.setFill(Color.DARKRED);
//					gc.fillText("X", (i*15)+26, (j*15)+38);
//				} else if(opponentsHits[i][j] == true && gd.getOwnTable()[i][j] == true) {
//					gc.setFill(Color.DARKRED);
//					gc.fillOval((a*15)+25, (b*15)+25,14,14);
//				}
//			}
//		}
//	}
	
/*
 * 	Draws the hits of multiplayer opponent.
 */
	
	
	/*
	 * opponentsHitMP() checks the two int 'a' & 'b' if they changed, and draws multiplayer opponent's hits.
	 */
	
	public void opponentsHitMP() {
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				controller.checkOwnFleet();
				String[] tmp = new String[2];
				while(true) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String data = conn.getReceivedData();
					if(data.length() < 6) {
						tmp = data.split(";");
						a = Integer.parseInt(tmp[0]);
						b = Integer.parseInt(tmp[1]);
						if(a > -1 && b > -1) {
						controller.checkOwnFleet();
						controller.drawOpponentsHit();
						controller.drawOwnSunkedShips();
						if(countFleetSize(gd.getOwnFleet()) == 0) {
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									if(countFleetSize(gd.getOwnFleet()) == 0) {
										gd.getPlayer2().increaseGamesWon();
										gd.getPlayer1().increaseGamesPlayed();
									}
									if(countFleetSize(gd.getOpponentsFleet()) == 0) {
										gd.getPlayer1().increaseGamesWon();
										gd.getPlayer2().increaseGamesPlayed();
									}
									mainApp.showGameOverLayout();
									resetHits();
									// TODO Auto-generated method stub
									
								}
							});
							break;
						}
						
						}
					}
				}
			}
		});
		th.setDaemon(true);
		th.start();
	}
/*
 * 	drawTable() draws the own map for the first time.
 */
	
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

	
	public Integer countFleetSize(ArrayList<Ship> list) {
		Integer result = 5;
		for(Ship s:list) {
			if (s.isSunk()) {
				result--;
			}
		}
		return result;
	}

	public boolean isOwnTurnWasFirst() {
		return ownTurnWasFirst;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
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
	
	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public boolean[][] getOpponentsHits() {
		return opponentsHits;
	}

	public ArrayList<Ship> getOwnFleet(){
		return ownFleet;
	}
	
	public void resetHits() {
		turns = 1;
		turn = rnd.nextBoolean();
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				opponentsHits[i][j] = false;
				ownHits[i][j] = false;
			}
		}
	}
}