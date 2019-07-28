package fleetbattle.view;

import fleetbattle.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

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

}
