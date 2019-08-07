package fleetbattle.view;

import fleetbattle.MainApp;
import fleetbattle.model.GamePlay;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BattleLayoutController {
	
	MainApp mainApp;
	GamePlay gp; 
	Integer oppFleetSize;
	Integer ownFleetSize;
	
	public void initialize() {
		gp = GamePlay.getInstance();
		gp.setController(this);
		
		oppFleetSize = gp.getOpponentsFleet().size();
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
	
	public void showTurnStat() {
		turnIndicator.setText(gp.isTurn()? "Your turn":"Enemy's turn");
		turn.setText(gp.getTurns().toString());
		oppFleet.setText(oppFleetSize.toString());
		ownFleetSize = mainApp.getFleet().size();
		yourFleet.setText(ownFleetSize.toString());
	}
	
	public void showHitIndicator(String srt) {
		hitIndicator.setText(srt);
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	

}
