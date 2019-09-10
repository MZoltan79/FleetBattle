package fleetbattle;
	
import java.io.IOException;
import java.util.ArrayList;

import communication.Connection;
import fleetbattle.model.AutoPlace;
import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import fleetbattle.model.Player;
import fleetbattle.model.Ship;
import fleetbattle.view.BattleLayoutController;
import fleetbattle.view.ConnectionSettingsController;
import fleetbattle.view.GameOverLayoutController;
import fleetbattle.view.PlaceShipsLayoutController;
import fleetbattle.view.RootLayoutController;
import fleetbattle.view.WelcomeLayoutController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private static BorderPane placingPane;
	private ToggleGroup group = new ToggleGroup();
	private Canvas canvas = new Canvas(398,398);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	
	private AutoPlace ap;				
	private GamePlay gp;
	private GameData gd;
	private Connection conn;

	public static Player player1;
	public static Player player2;
	
	private boolean[][] table;

	private boolean singlePlayer = true;
	private boolean firstLaunchOfWelcomeLayout;
	private boolean firstLaunch = true;
	private static int x = 0;
	private static int y = 0;
	private String shipName = "CARRIER";

	private Ship tempShip = null;
	private Ship carrier = new Ship("carrier");
	private Ship destroyer = new Ship("destroyer");
	private Ship submarine = new Ship("submarine");
	private Ship cruiser = new Ship("cruiser");
	private Ship patrolBoat = new Ship("patrolboat");
	private ArrayList<Ship> fleet = new ArrayList<>();
	private ArrayList<Ship> ownFleet = new ArrayList<>();
	
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		this.primaryStage.setTitle("Fleet Battle");
		Image icon = new Image(MainApp.class.getResourceAsStream("view/Battleship-icon3.png"));
		this.primaryStage.getIcons().add(icon);
		initRootLayout();
		showWelcomeLayout();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

	
	// initRootLayout() - initializes the root layout, and some game data.
	
	public void initRootLayout() {
		try {
			gd = GameData.getInstance();
			ap = AutoPlace.getInstance();
			ap.setupOfFields();
			if(gd.getPlayer1() == null) gd.setPlayer1(new Player());
			if(gd.getPlayer2() == null) gd.setPlayer2(new Player());
			firstLaunchOfWelcomeLayout = true;
			if(firstLaunchOfWelcomeLayout) {
				conn = Connection.getInstance();
				conn.setDaemon(true);
				conn.start();
			}
			ownFleet.add(carrier);
			ownFleet.add(destroyer);
			ownFleet.add(submarine);
			ownFleet.add(cruiser);
			ownFleet.add(patrolBoat);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = loader.load();
			RootLayoutController controller = new RootLayoutController();
			controller.setMainApp(this);
			MenuItem settings = (MenuItem) loader.getNamespace().get("settings");
			settings.setOnAction(e -> showConnectionSettingsLayout());
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// showWelcomeLayout() - this layout returns several times during gameplay. Handles single-, multiplayer mode, indicates the opponents status
	
	public void showWelcomeLayout() {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WelcomeLayout.fxml"));
			AnchorPane welcomeLayout = loader.load();
			rootLayout.setCenter(welcomeLayout);
			WelcomeLayoutController controller = loader.getController();
			controller.setMainApp(this);
			Button startButton = (Button) loader.getNamespace().get("startButton");
			RadioButton singleplayer = (RadioButton) loader.getNamespace().get("singleplayer");
			RadioButton multiplayer = (RadioButton) loader.getNamespace().get("multiplayer");
			ToggleGroup modeGroup = new ToggleGroup();
			Label opponentReady = (Label) loader.getNamespace().get("opponentReady");
			singleplayer.setToggleGroup(modeGroup);
			multiplayer.setToggleGroup(modeGroup);
			if(singlePlayer) {
				singleplayer.setSelected(true);
				startButton.setDisable(false);
			} else {
				multiplayer.setSelected(true);
				startButton.setDisable(true);
			}
			if(!singlePlayer && PlaceShipsLayoutController.ready ) {
				opponentReady.setText("Waiting for opponent...");
				Thread th = new Thread(new Runnable() {
				String data = "";
					
				@Override
				public void run() {
					while(true) {
						data = Connection.receivedData;
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
						e.printStackTrace();
						}
						if(data != null && data.length() > 100) {
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									opponentReady.setText("Opponent is ready to fight!");
									startButton.setDisable(false);
								}
							});
							break;
							}
						}
					}
				});
				th.setDaemon(true);
				th.start();
			}
			
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		firstLaunchOfWelcomeLayout = false;
	}
	
	
	// Ez a hajók elrendezése képernyő
	
	public void showPlaceShipsLayout() {
		placeShips();
	}
	
	public void autoPlace() {
//		gd.clearOwnFleet();
		ap.reset();
		ap.placeAll();
		placeShips();
		drawTable();
	}

	// drawTable() this method draws the table in PlaceShipsLayout.
	
	public void drawTable() {
		fleet = gd.getOwnFleet();
		table = gd.getOwnTable();
		Font font = new Font(30);
		gc.setFont(font);
		Character[] columns = {'A','B','C','D','E','F','G','H'};
		gc.setFill(Color.CADETBLUE);
		for(int i = 0; i < columns.length; i++) {
			gc.fillText(columns[i].toString(), i*30 + 53, 39);
		}
		gc.fillText("I", 297, 39);
		gc.fillText("J", 327, 39);
		for(Integer i = 1; i < 10; i++) {
			gc.fillText(i.toString(), 18, (i-1)*30 + 75);
		}
		gc.fillText("10", 12, 345);
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(gd.getOwnTable()[i][j] == true) {
					gc.setFill(Color.DARKRED);
					gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
				} else {
					gc.setFill(Color.CADETBLUE);
					gc.fillRect((i*30 + 49), (j*30 + 49), 28, 28);
				}
			}
		}
	}
	
	// placeShips() - Loads PlaceShipsLayout.fxml. While placing ships manually, handles several MouseEvents, to set ships' datas. 
	
	public void placeShips() {
		try {
			AnchorPane placeShipsLayout;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PlaceShipsLayout_new.fxml"));
			placeShipsLayout = loader.load();
			placingPane = (BorderPane) loader.getNamespace().get("placingPane");
			placingPane.setCenter(canvas);
			table = gd.getOwnTable();
			RadioButton rbCarrier = (RadioButton) loader.getNamespace().get("rbCarrier");
			RadioButton rbDestroyer = (RadioButton) loader.getNamespace().get("rbDestroyer");
			RadioButton rbSubmarine = (RadioButton) loader.getNamespace().get("rbSubmarine");
			RadioButton rbCruiser = (RadioButton) loader.getNamespace().get("rbCruiser");
			RadioButton rbPatrolBoat = (RadioButton) loader.getNamespace().get("rbPatrolBoat");
			rbCarrier.setToggleGroup(group);
			rbDestroyer.setToggleGroup(group);
			rbSubmarine.setToggleGroup(group);
			rbCruiser.setToggleGroup(group);
			rbPatrolBoat.setToggleGroup(group);
			rbCarrier.setSelected(true);
			getShipName();
			rootLayout.setCenter(placeShipsLayout);
			drawTable();
			canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::mouseDraggedOrReleased);
			canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, this::mouseDraggedOrReleased);
			canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, this::mousePressed);
			canvas.addEventFilter(MouseEvent.MOUSE_MOVED, this::mouseMoved);
			canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, this::mousePrimaryPressed);
			canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, this::mousePrimaryReleased);
						
			
			PlaceShipsLayoutController controller = loader.getController();
			controller.setMainApp(this);
			controller.showShipData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// showBattleLayout() - Loads BattleLayout.fxml and some minor data. Here happens the fight :)
	
	public void showBattleLayout() {
		try {
			AnchorPane battleLayout;
			AnchorPane rightPane;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/BattleLayout.fxml"));
			battleLayout = loader.load();
			rightPane = (AnchorPane) loader.getNamespace().get("rightPane");
			rootLayout.setCenter(battleLayout);
			gp = GamePlay.getInstance();
			gp.setMainApp(this);
			rightPane.getChildren().setAll(gp);
			BattleLayoutController bController = loader.getController();
			bController.setMainApp(this);
			bController.showTurnStat();
			if(singlePlayer) {
				gp.startSinglePlayer();
			} else {
				gp.startMultiPlayer();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// showGameOverLayout() - loads GameOverLayout.fxml. Shows result of a battle.
	
	public void showGameOverLayout() {
		try {
			AnchorPane gameOverLayout;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/GameOverLayout.fxml"));
			gameOverLayout = loader.load();
			
			Stage resultStage = new Stage();
			resultStage.setTitle("Battle results");
			resultStage.initModality(Modality.WINDOW_MODAL);
			resultStage.initOwner(primaryStage);
			Scene scene = new Scene(gameOverLayout);
			resultStage.setScene(scene);
			GameOverLayoutController controller = loader.getController();
			controller.setResultStage(resultStage);
			controller.setMainApp(this);
			resultStage.show();
			controller.showResults();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// showConnectionSettingsLayout() - loads ConnectionSettings.fxml. Here u can edit connection settings to the server.
	
	public void showConnectionSettingsLayout() {
		try {
			AnchorPane connSettings;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ConnectionSettings.fxml"));
			connSettings = loader.load();
			
			Stage settingsStage = new Stage();
			settingsStage.setTitle("Connection settings");
			settingsStage.initModality(Modality.WINDOW_MODAL);
			settingsStage.initOwner(primaryStage);
			Scene scene = new Scene(connSettings);
			settingsStage.setScene(scene);
			ConnectionSettingsController controller = loader.getController();
			controller.setSettingsStage(settingsStage);
			controller.setMainApp(this);
			settingsStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// This is for display ships name while placing ships.
	
	public String getShipName() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/PlaceShipsLayout_new.fxml"));
		shipName = ((RadioButton)group.getSelectedToggle()).getText().toLowerCase();
		setTempShip(shipName);
		return shipName;
	}
	
	
	/*
	 * Sets the values of 'x' and 'y'. These are necessary to set ship's coordinates while placing ships, and sets hits during battle; 
	 */
	
	public void mouseMoved(MouseEvent ev) {
		x = (int)((ev.getX()-51)/30);
		y = (int)(ev.getY()-51)/30;
		
	}
	
	// This deploys the selected ships on the table.
	
	public void mouseDraggedOrReleased(MouseEvent ev) {
		x = (int)((ev.getX()-51)/30);
		y = (int)(ev.getY()-51)/30;
		if(ev.getButton() == MouseButton.PRIMARY) {
			gd.getOwnTable()[x][y] = true;
		} else if (ev.getButton() == MouseButton.SECONDARY) {
			gd.getOwnTable()[x][y] = false;
		}
		drawTable();
	}
	
	// redraws table
	public void mousePressed(MouseEvent ev) {
		if(x >= 0 && y >= 0) {
			drawTable();
		}
	}
	
	// Sets the selected ship's start and end points to its coordinates.
	public void mousePrimaryPressed(MouseEvent ev) {
		if(ev.getButton() == MouseButton.PRIMARY) tempShip.setStartpoint(x, y);
	}
	
	public void mousePrimaryReleased(MouseEvent ev) {
		if(ev.getButton() == MouseButton.PRIMARY) tempShip.setEndpoint(x, y);
	}
	
	
	public Ship getTempShip() {
		return tempShip;
	}

	public void setTempShip(String shipName) {
		for(Ship s: ownFleet) {
			if(s.getName().equals(shipName)) {
				tempShip = s;
			}
		}
		
	}

	public void setTempShip(Ship tempShip) {
		this.tempShip = tempShip;
	}

	public boolean[][] getTable() {
		return table;
	}
	
	public void setTable(boolean[][] table) {
		this.table = table;
	}
	
	public void clearTable() {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				gd.getOwnTable()[i][j] = false;
			}
		}
			gd.getOwnFleet().removeAll(gd.getOwnFleet());
		drawTable();
	}
	
	public void placeShip(Ship tempShip) {
		
	}
	public boolean checkShipIsPlaced() {
		for(Ship s: gd.getOwnFleet()) {
			if(s.equals(tempShip)) {
				return true;
			}
		}
		return false;
	}


	public ArrayList<Ship> getFleet() {
		return fleet;
	}
	
	public boolean isSinglePlayer() {
		return singlePlayer;
	}

	public void setSinglePlayer(boolean singlePlayer) {
		this.singlePlayer = singlePlayer;
	}

	public boolean isGuestMode() {
		return firstLaunchOfWelcomeLayout;
	}

	public void setGuestMode() {
		firstLaunchOfWelcomeLayout = !firstLaunchOfWelcomeLayout;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public boolean isFirstLaunch() {
		return firstLaunch;
	}

	public void setFirstLaunch(boolean firstLaunch) {
		this.firstLaunch = firstLaunch;
	}
	
	

}