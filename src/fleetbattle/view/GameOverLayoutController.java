package fleetbattle.view;


import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GameOverLayoutController {

	GameData gd;
	GamePlay gp;
	Stage resultStage;
	MainApp mainApp;
	Image defeat;
	Image victory;
	
	@FXML
	public void initialize() {
		defeat = new Image("file:/home/moricz/workspaces/own-workspace/FleetBattle/src/fleetbattle/view/defeat.jpg",600,300,true,false);
		victory = new Image("file:/home/moricz/workspaces/own-workspace/FleetBattle/src/fleetbattle/view/victory.jpg",600,300,true,false);
	}
	
	@FXML
	Button continueButton;
	
	@FXML
	Label display;
	
	@FXML
	Label player1Small;
	
	@FXML
	Label player2Small;
	
	@FXML
	Label player1GamesPlayed;
	
	@FXML
	Label player1GamesWon;
	
	@FXML
	Label player2GamesPlayed;
	
	@FXML
	Label player2GamesWon;
	
	@FXML
	ImageView resultPicture;
	
	public void showResults() {
		gp = GamePlay.getInstance();
		gd = GameData.getInstance();
		if(gp.countFleetSize(gd.getOwnFleet()) == 0) {
			display.setText("DEFEAT");
			resultPicture.setImage(defeat);
		} else {
			display.setText("VICTORY");
			resultPicture.setImage(victory);
		}
		player1Small.setText(MainApp.player1.getNickName());
		player1GamesPlayed.setText(MainApp.player1.getGamesPlayed().toString());
		player1GamesWon.setText(MainApp.player1.getGamesWon().toString());
		
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setResultStage(Stage resultStage) {
		this.resultStage = resultStage;
	}
	
	
	
	
}
