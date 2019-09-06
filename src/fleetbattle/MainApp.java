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
	
	AutoPlace ap;				
	GamePlay gp;
	GameData gd;
	
	Ship tempShip = null;
	Ship carrier = new Ship("carrier");
	Ship destroyer = new Ship("destroyer");
	Ship submarine = new Ship("submarine");
	Ship cruiser = new Ship("cruiser");
	Ship patrolBoat = new Ship("patrolboat2");
	ArrayList<Ship> fleet = new ArrayList<>();
	ArrayList<Ship> ownFleet = new ArrayList<>();
	

	public static Player player1;
	public static Player player2;
	Connection conn;
	private boolean[][] table;
	private boolean singlePlayer = true;
	private boolean firstLaunchOfWelcomeLayout;
	static int x = 0;
	static int y = 0;
	String shipName = "CARRIER";

	
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		this.primaryStage.setTitle("Fleet Battle");
		this.primaryStage.getIcons().add(new Image("file:src/fleetbattle/view/Battleship-icon3.png"));
		initRootLayout();
		showWelcomeLayout();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

	
	// Ez az alap stage. a root...
	
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = loader.load();
			RootLayoutController controller = new RootLayoutController();
			controller.setMainApp(this);
			gd = GameData.getInstance();
			if(gd.getPlayer1() == null) {
				gd.setPlayer1(new Player());
			}
			if(gd.getPlayer2() == null) {
				gd.setPlayer2(new Player());
			}
			MenuItem settings = (MenuItem) loader.getNamespace().get("settings");
			settings.setOnAction(e -> showConnectionSettingsLayout());
			firstLaunchOfWelcomeLayout = true;
			if(firstLaunchOfWelcomeLayout) {
				conn = Connection.getInstance();
				conn.setDaemon(true);
				conn.start();
			}
			ap = AutoPlace.getInstance();
			ap.setupOfFields();
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Ez a kezdőképernyö
	
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
		ap.reset();
		ap.placeAll();
		placeShips();
		drawTable();
	}

	//PlaceShipsLayout ablakban ez rajzolja a táblát
	
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
	
	// Itt manuális lerakásnál MouseEvent-ek segítségével lehet letenni a hajókat
	
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
	
	// Ez a battle (maga a játék) képernyő
	
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
	
	
	public String getShipName() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/PlaceShipsLayout_new.fxml"));
		shipName = ((RadioButton)group.getSelectedToggle()).getText().toUpperCase();
		switch (shipName) {
			case "CARRIER": tempShip = carrier; break;
			case "DESTROYER": tempShip = destroyer; break;
			case "SUBMARINE": tempShip = submarine; break;
			case "CRUISER": tempShip = cruiser; break;
			case "PATROLBOAT": tempShip = patrolBoat; break;
			default: break;
			}
		return shipName;
	}
	
	/*
	 *  Tábla méretéhez igazítva állítja be "x" és "y" értékét, mely a hajók koordinátájának
	 *  a rögzítéséhez kell.
	 */
	
	public void mouseMoved(MouseEvent ev) {
		x = (int)((ev.getX()-51)/30);
		y = (int)(ev.getY()-51)/30;
		
	}
	
	// úgyszintén az elhelyezéshez kell, de ez már a rögzíti is a hajókat a table-ön.
	
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
	
	// Ez frissíti a tábla kirajzolását.
	public void mousePressed(MouseEvent ev) {
		if(x >= 0 && y >= 0) {
			drawTable();
//			System.out.println(x + " " + y);
		}
	}
	
	// Ez az adot hajó kezdö és vég pontjait rögzíti a Ship coordinates-be.
	public void mousePrimaryPressed(MouseEvent ev) {
		if(ev.getButton() == MouseButton.PRIMARY) tempShip.setStartpoint(x, y);
	}
	public void mousePrimaryReleased(MouseEvent ev) {
		if(ev.getButton() == MouseButton.PRIMARY) tempShip.setEndpoint(x, y);
	}
	
	public Ship getTempShip() {
		return tempShip;
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
		if(gd.getOwnFleet().size() != 0) {
			gd.getOwnFleet().removeAll(gd.getOwnFleet());
		}
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

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	

}