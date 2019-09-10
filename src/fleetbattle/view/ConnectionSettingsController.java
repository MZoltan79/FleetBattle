package fleetbattle.view;

import communication.Connection;
import fleetbattle.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ConnectionSettingsController {
	
	Stage settingsStage;
	MainApp mainApp;
	Connection conn;
	
	@FXML
	TextField host;
	
	@FXML
	TextField port;
	
	@FXML
	Button ok;
	
	@FXML
	Button cancel;
	
	@FXML
	public void initialize() {
		conn = Connection.getInstance();
	}

	public void handleOkButton() {
		if((host.getText() != null && !host.getText().equals("")) || (port.getText() != null && !port.getText().equals(""))) {
			if(host.getText() != null && !host.getText().equals("")) {
				conn.setHost(host.getText());
			}
			if(port.getText() != null && !port.getText().equals("")) {
				conn.setPort(Integer.parseInt(port.getText()));
			}
			conn.saveData();
			settingsStage.close();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Fields are empty");
			alert.setHeaderText("All the fields are empty!");
			alert.setContentText("You didn't type anything...");
			
			alert.showAndWait();
		}
	}
	
	public void handleCancelButton() {
		settingsStage.close();
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setSettingsStage(Stage settingsStage) {
		this.settingsStage = settingsStage;
	}
}
