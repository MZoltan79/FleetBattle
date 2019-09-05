package fleetbattle.view;


import data.PlayersData;
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
	PlayersData pd;
	
	@FXML
	public void initialize() {
		defeat = new Image("file:src/fleetbattle/view/defeat.jpg",600,300,true,false);
		victory = new Image("file:src/fleetbattle/view/victory.jpg",600,300,true,false);
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
		pd = PlayersData.getInstance();
		gp = GamePlay.getInstance();
		gd = GameData.getInstance();
		if(gp.countFleetSize(gd.getOwnFleet()) == 0) {
			display.setText("DEFEAT");
			resultPicture.setImage(defeat);
//			MainApp.player1.increaseGamesPlayed();
//			MainApp.player2.increaseGamesPlayed();
//			MainApp.player2.increaseGamesWon();
		} else {
			display.setText("VICTORY");
			resultPicture.setImage(victory);
//			MainApp.player2.increaseGamesPlayed();
//			MainApp.player1.increaseGamesPlayed();
//			MainApp.player1.increaseGamesWon();
			
		}
		player1Small.setText(pd.getPlayer1().getNickName());
		player1GamesPlayed.setText(pd.getPlayer1().getGamesPlayed().toString());
		player1GamesWon.setText(pd.getPlayer1().getGamesWon().toString());
		player2Small.setText(pd.getPlayer2().getNickName());
		player2GamesPlayed.setText(pd.getPlayer2().getGamesPlayed().toString());
		player2GamesWon.setText(pd.getPlayer2().getGamesWon().toString());
		
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setResultStage(Stage resultStage) {
		this.resultStage = resultStage;
	}
	
	@FXML
	public void continueButton() {
		mainApp.showWelcomeLayout();
		resultStage.close();
	}
	
	
}
