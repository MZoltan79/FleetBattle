package fleetbattle.view;

import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import fleetbattle.model.Ship;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BattleLayoutController {
	
	MainApp mainApp;
	GamePlay gp; 
	Integer oppFleetSize;
	Integer ownFleetSize;
	GraphicsContext gc;
	GameData gd;
	
	public void initialize() {
		gd = GameData.getInstance();
		gp = GamePlay.getInstance();
		gp.setController(this);
		gc = ownCanvas.getGraphicsContext2D();
		oppFleetSize = gd.getOpponentsFleet().size();
		ownFleetSize = gd.getOwnFleet().size();
	}
	
	@FXML
	Label hitIndicator;
	
	@FXML
	Label turnIndicator;
	
	@FXML
	Label turn;
	
	@FXML
	Label oppFleet;
	
	@FXML
	Label yourFleet;
	
	@FXML
	Canvas ownCanvas;
	
	public void showTurnStat() {
		turnIndicator.setText(gp.isTurn()? "Your turn":"Enemy's turn");
		turn.setText(gp.getTurns().toString());
		oppFleetSize = gd.getOpponentsFleet().size();
		ownFleetSize = gd.getOwnFleet().size();
		oppFleet.setText(oppFleetSize.toString());
		yourFleet.setText(ownFleetSize.toString());
	}
	
	public void setOppFleetIndicator(String srt) {
		oppFleet.setText(srt);
	}

	public void setOwnFleetIndicator(String srt) {
		yourFleet.setText(srt);
	}
	
	public void setHitIndicator(String srt) {
		hitIndicator.setText(srt);
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void drawOwnCanvas() {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				gc.setFill(Color.CADETBLUE);
				gc.fillRect((i*15 + 25), (j*15 + 25), 14, 14);
				if(gd.getOwnTable()[i][j] == true) {
					gc.setFill(Color.DARKGREEN);
					gc.fillRect((i*15 + 25), (j*15 + 25), 14, 14);
				}
			}
		}
	}
	
	public void drawOpponentsHit() {
		Font font = new Font(15);
		gc.setFont(font);
		
		System.out.println("saját tábla: " + gd.getOwnTable()[gp.getA()][gp.getB()]);
		
		if(gp.getOpponentsHits()[gp.getA()][gp.getB()]) {
			gp.ownTurn();
		} else if(gp.getOpponentsHits()[gp.getA()][gp.getB()] == false && gd.getOwnTable()[gp.getA()][gp.getB()] == false) {
			gp.getOpponentsHits()[gp.getA()][gp.getB()] = true;
			gc.setFill(Color.DARKRED);
			gc.fillText("X", (gp.getA()*15)+26, (gp.getB()*15)+38);
			gp.ownTurn();
		} else if(gp.getOpponentsHits()[gp.getA()][gp.getB()] == false && gd.getOwnTable()[gp.getA()][gp.getB()] == true) {
			gp.getOpponentsHits()[gp.getA()][gp.getB()] = true;
			gc.setFill(Color.DARKRED);
			gc.fillOval((gp.getA()*15)+25, (gp.getB()*15)+25,14,14);
			gp.aITurn();;
		}
		
	}
	
	public void drawOwnSunkedShips() {
		gc.setFill(Color.DARKRED);
		for(Ship s: gd.getOwnFleet()) {
			if((s.isVertical() == true) && s.isSunk()) {
				for(int i = 0; i < s.getSize(); i++) {
					gc.fillRect((s.getCoordinates()[0]*15 + 25), (s.getCoordinates()[1+i*2]*15 + 25), 14, 14);
				}
			} else if((s.isVertical() == false) && s.isSunk()){
				for(int i = 0; i < s.getSize(); i++) {
					gc.fillRect((s.getCoordinates()[0+i*2]*15 + 25), (s.getCoordinates()[1]*15 + 25), 14, 14);
				}
			}
			
		}
		
	}
	
	public void checkOwnFleet() {
		for(Ship s: gd.getOwnFleet()) {
			boolean sunk = true;
			if(s.isVertical()) {
				for(int i = 0; i < s.getSize(); i++) {
					if(gp.getOpponentsHits()[s.getCoordinates()[0+(i*2)]][s.getCoordinates()[1+(i*2)]] == false) {
						sunk = false;
					}
				}
			} else {
				for(int i = 0; i < s.getSize(); i++) {
					if(gp.getOpponentsHits()[s.getCoordinates()[0+(i*2)]][s.getCoordinates()[1+(i*2)]] == false) {
						sunk = false;
					}
				}
			}
			if(sunk) {
				s.setSunk();
				
			}
		}
	}

}