package fleetbattle.view;

import java.util.Map;

import communication.Connection;
import data.PlayersData;
import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class WelcomeLayoutController {
	PlayersData pd;
	GameData gd;
	Connection conn;
	
	@FXML
	public void initialize() {
		conn = Connection.getInstance();
		gd = GameData.getInstance();
		pd = PlayersData.getInstance();
		initGuestNoteLabel();
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
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void initGuestNoteLabel() {
		guestNoteLabel.setText(MainApp.guestMode? "Note: your data won't be stored as guest!":"");
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
		conn.send("login");
		conn.send(nick.getText());
		conn.send(password.getText());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(conn.getReceivedData());
		if(Connection.isLogin()) {
			System.out.println("logged in...");
			String[] tmp = conn.getReceivedData().split(";");
			mainApp.setPlayer1(new Player(tmp[0], Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2])));
		} else {
			System.out.println("Wrong nick name or password! Try again");
		}
	}
	
	public void handleNewPlayerButton() {
		conn.send("newplayer");
		conn.send(nick.getText());
		conn.send(password.getText());
		System.out.println(conn.getReceivedData());
	}

}