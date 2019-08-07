package fleetbattle.view;

import fleetbattle.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class WelcomeLayoutController {
	
	public void initialize() {
		
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
		mainApp.showBattleLayout();
	}

}
