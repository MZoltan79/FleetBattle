package fleetbattle.view;

import fleetbattle.MainApp;
import fleetbattle.model.Game;
import fleetbattle.model.Ship;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;

public class PlaceShipsLayoutController {
	
	public void initialize() {
		
	}
	
	@FXML
	Canvas placingCanvas;

	@FXML
	Button readyButton;


	@FXML
	Button autoPlaceButton;
	
	private static boolean[][] table;
	
	private MainApp mainApp;
	
	private Game game;

	public Canvas getPlacingCanvas() {
		return placingCanvas;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
//	public void newGame() {
//		game = new Game();
////		game.placeAll();
//	}
	
	public void handleReadyButton() {
		
	}
	
	public void handleAutoPlaceButton() {
//		game.placeAll();
//		table = game.getTable();
		mainApp.autoPlace();
//		game.reset();
	}
	 public static boolean[][] getTable() {
		 return table;
	 }
	
}
