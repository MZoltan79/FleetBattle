package fleetbattle.view;

import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.Ship;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class WelcomeLayoutController {
	GameData gd;
	
	public void initialize() {
		gd = GameData.getInstance();
	}
	
	@FXML
	CheckBox guestCheckBox;
	
	@FXML
	Label guestNoteLabel;
	
	@FXML
	Button startButton;

	@FXML
	Button placeShips;
	
	@FXML
	Button loginButton;
	
	@FXML
	RadioButton singleplayer;

	@FXML
	RadioButton multiplayer;
	
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void initGuestNoteLabel() {
		guestNoteLabel.setText(guestCheckBox.isSelected()? "Note: your data won't be stored as guest!":"");
	}
	
	public void handlePlaceShipsButton() {
		mainApp.showPlaceShipsLayout();
	}
	
	public void handleMultiPlayerRadioButton() {
		mainApp.setSinglePlayer(false);
	}

	public void handleSinglePlayerRadioButton() {
		mainApp.setSinglePlayer(true);
	}
	
	public void handleStartButton() {
//		for(Ship s: gd.getOwnFleet()) {
//			System.out.println(s.name() + " koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
//		}
		mainApp.showBattleLayout();
	}

}
