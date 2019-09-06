package fleetbattle.view;


import communication.Connection;
import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class WelcomeLayoutController {
	GameData gd;
	Connection conn;
	
	@FXML
	public void initialize() {
		conn = Connection.getInstance();
		gd = GameData.getInstance();
		loggedInLabel.setText(gd.getPlayer1().getNickName().equals("Guest player")? 
				"Playing as guest":"Playing as " + gd.getPlayer1().getNickName());
	}
	
	@FXML
	CheckBox guestCheckBox;
	
	@FXML
	Label opponentReady;
	
	@FXML
	Label loggedInLabel;
	
	@FXML
	Button startButton;

	@FXML
	Button placeShips;
	
	@FXML
	Button loginButton;

	@FXML
	Button newPlayerButton;
	
	@FXML
	RadioButton singleplayer;

	@FXML
	RadioButton multiplayer;
	
	@FXML
	TextField nick;
	
	@FXML
	PasswordField password;
	
	
	private MainApp mainApp;
	
	public void initOpponentReadyLabel(String msg) {
		opponentReady.setText(msg);
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
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
		if(gd.getOwnFleet().size() > 4) {
			mainApp.showBattleLayout();
		}
	}
	
	public void handleLoginButton() {
		if(conn.isConnected()) {
		conn.send("login");
		conn.send(nick.getText());
		conn.send(password.getText());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(Connection.isLogin()) {
			loggedInLabel.setText("logged in...");
			String[] tmp = conn.getReceivedData().split(";");
			Player player = new Player(tmp[0], Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
			gd.setPlayer1(player);
		} else {
			loggedInLabel.setText("Wrong nick name or password!");
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid nick, or password!");
			alert.setHeaderText("Wrong nickname or password!");
			alert.setContentText("Please try again!");
			
			alert.showAndWait();
		}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No connection");
			alert.setHeaderText("There is no connection with the server!");
			alert.setContentText("Is your connection dry?");
			
			alert.showAndWait();
		}
		
	}
	
	public void handleNewPlayerButton() {
		if(conn.isConnected()) {
		conn.send("newplayer");
		conn.send(nick.getText());
		conn.send(password.getText());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!Connection.isNewPlayer()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Wrong nickname!");
			alert.setHeaderText("The given nickname is in use, or field was empty!");
			alert.setContentText("Please try again!");
			
			alert.showAndWait();
		} else {
			loggedInLabel.setText("Done! Please log in!");
		}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No connection");
			alert.setHeaderText("There is no connection with the server!");
			alert.setContentText("is your connection dry?");
			
			alert.showAndWait();
		}
	}

}