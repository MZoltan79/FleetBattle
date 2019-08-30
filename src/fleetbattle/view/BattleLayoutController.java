package fleetbattle.view;

import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import fleetbattle.model.Ship;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

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
	Label turnCounter;
	
	@FXML
	Label oppFleet;
	
	@FXML
	Label yourFleet;
	
	@FXML
	Canvas ownCanvas;
	
	public void showTurnCounter() {
		turnCounter.setText(gp.getTurns().toString());
	}
	
	public void showTurnStat() {
		turnIndicator.setText(gp.isTurn()? "Your turn":"Enemy's turn");
		turnCounter.setText(gp.getTurns().toString());
		oppFleetSize = gp.countFleetSize(gd.getOpponentsFleet());
		ownFleetSize = gp.countFleetSize(gd.getOwnFleet());
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
//		showTurnStat();
		Font font = new Font(15);
		gc.setFont(font);
		if(gp.getA() > -1 && gp.getB() > -1) {
			
		 if(gp.getOpponentsHits()[gp.getA()][gp.getB()] == false && gd.getOwnTable()[gp.getA()][gp.getB()] == false) {
			 gp.getOpponentsHits()[gp.getA()][gp.getB()] = true;
			 gc.setFill(Color.DARKRED);
			 gc.fillText("X", (gp.getA()*15)+26, (gp.getB()*15)+38);
			 gp.changeTurn();
			 if(gp.isOwnTurnWasFirst()) gp.increaseTurns();
//				showTurnStat();
		 	} else if(gp.getOpponentsHits()[gp.getA()][gp.getB()] == false && gd.getOwnTable()[gp.getA()][gp.getB()] == true) {
		 		gp.getOpponentsHits()[gp.getA()][gp.getB()] = true;
		 		gc.setFill(Color.DARKRED);
		 		gc.fillOval((gp.getA()*15)+25, (gp.getB()*15)+25,14,14);
//		 		showTurnStat();
//		 		gp.opponentsTurn();
		 	}
		}
		
	}
	
	public void updateMPGui() {
		final KeyFrame kf1 = new KeyFrame(Duration.seconds(Math.random()), e -> drawOpponentsHit());
		final KeyFrame kf2 = new KeyFrame(Duration.seconds(1.1), e -> drawOwnSunkedShips());
		final Timeline tl = new Timeline(kf1, kf2);
		Platform.runLater(tl::play);
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
//		if(gp.countFleetSize(gd.getOpponentsFleet()) < 1 || gp.countFleetSize(gd.getOwnFleet()) < 1) {
//			mainApp.showGameOverLayout();
//			}
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