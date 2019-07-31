package fleetbattle.view;


import fleetbattle.MainApp;
import fleetbattle.model.Game;
import fleetbattle.model.Ship;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PlaceShipsLayoutController {
	
	public PlaceShipsLayoutController() {
		
	}
	@FXML
	public void initialize() {
	}
	
	
	
	@FXML
	Label typeLabel;
	
	@FXML
	Label sizeLabel;
	
	@FXML
	Label statusLabel;
	
	@FXML
	Canvas placingCanvas;

	@FXML
	Button readyButton;

	@FXML
	Button autoPlaceButton;

	@FXML
	Button placeButton;
	
	@FXML
	Button removeButton;
	
	@FXML
	Button clearButton;
	
	private static boolean[][] table;
	
	private MainApp mainApp;
	
	private Game game;

	public Canvas getPlacingCanvas() {
		return placingCanvas;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
	}
	
	
	public void handleReadyButton() {
		
	}

	public void handlePlaceButton() {
//		mainApp.placeShips();
		
	}
	
	public void handleClearButton() {
		
	}

	public void handleRemoveButton() {
		
	}
	
	public void handleAutoPlaceButton() {
		mainApp.autoPlace();
	}
	 public static boolean[][] getTable() {
		 return table;
	 }
	 
	 
//	 public void showShipData() {
//		mainApp.getFleet().forEach(e -> {
//			if(e.name().equalsIgnoreCase(mainApp.getShipName())) {
//				typeLabel.setText(e.name().toLowerCase());
//				sizeLabel.setText(e.getSize().toString());
//				statusLabel.setText(e.isPlaced? "placed" : "not placed" );
//			}
//		});
//	 }
	
}
