package fleetbattle.view;



import communication.Connection;
import fleetbattle.MainApp;
import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import fleetbattle.model.Ship;
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
//		gd.getOwnFleet().add(new Ship("carrier"));
//		gd.getOwnFleet().add(new Ship("destroyer"));
//		gd.getOwnFleet().add(new Ship("submarine"));
//		gd.getOwnFleet().add(new Ship("cruiser"));
//		gd.getOwnFleet().add(new Ship("patrolboat"));
		System.out.println(gd.getOwnFleet().size());
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
			mainApp.getTempShip().setCoordinates(mainApp.getTempShip().getCoordinates()[0], mainApp.getTempShip().getCoordinates()[1]);
			if(gd.getOwnFleet().size() < 5) {
				gd.getOwnFleet().add(mainApp.getTempShip());
				mainApp.getTempShip().printShip();
			} else {
				
			}
		System.out.println("recently placed: " + mainApp.getTempShip().getCoordinates()[0] + "," +
				mainApp.getTempShip().getCoordinates()[1] + " " +
				mainApp.getTempShip().getCoordinates()[mainApp.getTempShip().getCoordinates().length-2] + "," +
				mainApp.getTempShip().getCoordinates()[mainApp.getTempShip().getCoordinates().length-1]);
		}
		showShipData();
		
	}
	
	public void handleClearButton() {
		mainApp.clearTable();
		showShipData();
	}

	// Ez kicsit még zavaros!!!
	
	public void handleRemoveButton() {
		if(gd.getOwnFleet().size() > 0) {
		for(int i = 0 ;i < gd.getOwnFleet().size(); i++) {
			if(gd.getOwnFleet().get(i).getSize() == mainApp.getTempShip().getSize()) {
				gd.getOwnFleet().remove(i);
				break;
			}
		}
		int x = mainApp.getTempShip().getCoordinates()[0];
		int y = mainApp.getTempShip().getCoordinates()[1];
		for(int j = 0; j < mainApp.getTempShip().getSize(); j++) {
			if(mainApp.getTempShip().isVertical()) {
				gd.getOwnTable()[x][y+j] = false;
			} else {
				gd.getOwnTable()[x+j][y] = false;
			}
		}
		showShipData();
		mainApp.drawTable();
		}
		
	}
	
	public void handleAutoPlaceButton() {
		gd.clearOwnFleet();
		mainApp.autoPlace();
		showShipData();
		System.out.println(gd.getOwnFleet().size());
	}
	 
	public void showShipData() {
		switch(mainApp.getShipName()) {
		case "carrier": {
			typeLabel.setText("Carrier");
			sizeLabel.setText("5");
			checkShipIsPlaced();
			break;
		}
		case "destroyer": {
			typeLabel.setText("Destroyer");
			sizeLabel.setText("4");
			checkShipIsPlaced();
			break;
		}
		case "submarine": {
			typeLabel.setText("Submarine");
			sizeLabel.setText("3");
			checkShipIsPlaced();
			break;
		}
		case "cruiser": {
			typeLabel.setText("Cruiser");
			sizeLabel.setText("2");
			checkShipIsPlaced();
			break;
		}
		case "patrolboat": {
			typeLabel.setText("Patrolboat");
			sizeLabel.setText("1");
			checkShipIsPlaced();
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
	
	public void checkShipIsPlaced() {
		for(Ship s: gd.getOwnFleet()) {
			if(s.getName().equals(mainApp.getShipName()) && s.isPlaced) {
				statusLabel.setText("Placed");
				break;
			} else {
				statusLabel.setText("Not placed");
			}
		}	
//		});
		
	}
	
}