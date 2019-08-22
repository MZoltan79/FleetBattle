package fleetbattle.view;


import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverLayoutController {

	GameData gd;
	GamePlay gp;
	Stage resultStage;
	
	@FXML
	Button back;
	
	@FXML
	Label player1;
	
	@FXML
	Label player2;
	
	@FXML
	Label display;
	
	@FXML
	Label player1Stat;
	
	@FXML
	Label player2Stat;
	
	
	
	public void showResults() {
		gp = GamePlay.getInstance();
		gd = GameData.getInstance();
		if(gp.countFleetSize(gd.getOwnFleet()) == 0) {
			display.setText("DEFEAT");
		} else {
			display.setText("VICTORY");
		}
		
	}
	
	public void setResultStage(Stage resultStage) {
		this.resultStage = resultStage;
	}
	
	
}
