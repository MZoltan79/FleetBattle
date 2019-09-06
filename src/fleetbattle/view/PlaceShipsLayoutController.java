package fleetbattle.view;



import communication.Connection;
import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PlaceShipsLayoutController {
	
	public static boolean ready = false;
	
	GamePlay gp;
	GameData gd;
	Connection conn;
	private MainApp mainApp;
	
	public PlaceShipsLayoutController() {
		
	}
	@FXML
	public void initialize() {
		gd = GameData.getInstance();
		conn = Connection.getInstance();
		gp = GamePlay.getInstance();
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
	
	
	
	public Canvas getPlacingCanvas() {
		return placingCanvas;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
	}
	
	
	public void handleReadyButton() {
		if(gd.getOwnFleet().size() > 4) {
			if(!mainApp.isSinglePlayer()) {
				Connection.sendData = gp.buildOwnData();
				System.out.println("ready: " + Connection.sendData);
				conn.sendData();
			}
			ready = true;
			mainApp.showWelcomeLayout();
		}
		
	}

	public void handlePlaceButton() {
		if(mainApp.getTempShip() != null) {
			mainApp.getTempShip().isPlaced = true;
			if(mainApp.getTempShip().getCoordinates()[0] == 
					mainApp.getTempShip().getCoordinates()[mainApp.getTempShip().getCoordinates().length-2]) {
				mainApp.getTempShip().setVertical(true);
			} else {
				mainApp.getTempShip().setVertical(false);
			}
			if(gd.getOwnFleet().size() < 5) {
				gd.getOwnFleet().add(mainApp.getTempShip());
			} else {
				
			}
		System.out.println("recently placed: " + mainApp.getTempShip().getCoordinates()[0] + "," +
				mainApp.getTempShip().getCoordinates()[1] + " " +
				mainApp.getTempShip().getCoordinates()[mainApp.getTempShip().getCoordinates().length-2] + "," +
				mainApp.getTempShip().getCoordinates()[mainApp.getTempShip().getCoordinates().length-1]);
		System.out.println(mainApp.getTempShip().isVertical());
		System.out.println(gd.getOwnFleet().size());
		}
		showShipData();
		
	}
	
	public void handleClearButton() {
		mainApp.clearTable();
		showShipData();
		mainApp.showConnectionSettingsLayout();
	}

	// Ez kicsit még zavaros!!!
	
	public void handleRemoveButton() {
		int i = 0;
		for(i = 0 ;i < mainApp.getFleet().size(); i++) {
			if(mainApp.getFleet().get(i).getSize() == mainApp.getTempShip().getSize()) {
				break;
			}
		}
		if(gd.getOwnFleet().size() > 0) {
			gd.getOwnFleet().remove(i);
		} else {
			
		}
		int x = mainApp.getTempShip().getCoordinates()[0];
		int y = mainApp.getTempShip().getCoordinates()[1];
		for(int j = 0; j < mainApp.getTempShip().getSize(); j++) {
			if(mainApp.getTempShip().isVertical()) {
				mainApp.getTable()[x][y+j] = false;
			} else {
				mainApp.getTable()[x+j][y] = false;
			}
		}
		showShipData();
		mainApp.drawTable();
		
	}
	
	public void handleAutoPlaceButton() {
		mainApp.autoPlace();
		showShipData();
	}
	 
	public void showShipData() {
		switch(mainApp.getShipName()) {
		case "CARRIER": {
			typeLabel.setText("Carrier");
			sizeLabel.setText("5");
			statusLabel.setText(mainApp.checkShipIsPlaced()? "Placed":"Not placed");
			break;
		}
		case "DESTROYER": {
			typeLabel.setText("Destroyer");
			sizeLabel.setText("4");
			statusLabel.setText(mainApp.checkShipIsPlaced()? "Placed":"Not placed");
			break;
		}
		case "SUBMARINE": {
			typeLabel.setText("Submarine");
			sizeLabel.setText("3");
			statusLabel.setText(mainApp.checkShipIsPlaced()? "Placed":"Not placed");
			break;
		}
		case "CRUISER": {
			typeLabel.setText("Cruiser");
			sizeLabel.setText("2");
			statusLabel.setText(mainApp.checkShipIsPlaced()? "Placed":"Not placed");
			break;
		}
		case "PATROLBOAT": {
			typeLabel.setText("Patrolboat");
			sizeLabel.setText("1");
			statusLabel.setText(mainApp.checkShipIsPlaced()? "Placed":"Not placed");
			break;
		}
		default: {
			typeLabel.setText("");
			sizeLabel.setText("");
			statusLabel.setText("");
			break;
		}
		
		}
	}
	
}