package fleetbattle.view;


import fleetbattle.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RootLayoutController {
	MainApp mainApp;
	

	@FXML
	MenuItem close;
	
	@FXML
	MenuItem settings;
	
	
	
	
	public void initialize() {

	}
	
	
	public void  handleExit() {
		System.exit(0);
	}
	@FXML
	public void  handleSettings() {
//		mainApp.showConnectionSettingsLayout();
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}