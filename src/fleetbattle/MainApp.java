package fleetbattle;
	
import java.io.IOException;
import java.util.ArrayList;

import fleetbattle.model.AutoPlaceOpponent;
import fleetbattle.model.AutoPlace;
import fleetbattle.model.GameData;
import fleetbattle.model.GamePlay;
import fleetbattle.model.Player;
import fleetbattle.model.Ship;
import fleetbattle.view.BattleLayoutController;
import fleetbattle.view.PlaceShipsLayoutController;
import fleetbattle.view.WelcomeLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private static BorderPane placingPane;
	private ToggleGroup group = new ToggleGroup();
	private Canvas canvas = new Canvas(398,398);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	
	private static AutoPlace ap;				
	GamePlay gp;
	GameData gd;
	Ship tempShip = null;
	Ship carrier = Ship.CARRIER;
	Ship destroyer = Ship.DESTROYER;
	Ship submarine = Ship.SUBMARINE;
	Ship cruiser = Ship.CRUISER;
	Ship patrolBoat = Ship.PATROLBOAT;

	ArrayList<Ship> fleet = new ArrayList<>();
	ArrayList<Ship> ownFleet = new ArrayList<>();

	Player player1;
	Player player2;

	private static boolean[][] table;
	private static boolean[][] ownTable;
	private boolean singlePlayer = true;;
	static int x = 0;
	static int y = 0;
	String shipName = "CARRIER";
	
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		this.primaryStage.setTitle("Fleet Battle");
		this.primaryStage.getIcons().add(new Image("file:/home/moricz/workspaces/own-workspace/FleetBattle/src/fleetbattle/view/Battleship-icon3.png"));
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
			RadioButton singleplayer = (RadioButton) loader.getNamespace().get("singleplayer");
			RadioButton multiplayer = (RadioButton) loader.getNamespace().get("multiplayer");
			ToggleGroup modeGroup = new ToggleGroup();
			singleplayer.setToggleGroup(modeGroup);
			multiplayer.setToggleGroup(modeGroup);
			if(singlePlayer) {
				singleplayer.setSelected(true);
			} else {
				multiplayer.setSelected(true);
			}
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// Ez a hajók elrendezése képernyő
	
	public void showPlaceShipsLayout() {
		ap = AutoPlace.getInstance();
		ap.setupOfFields();
		gd = GameData.getInstance();
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
		table = ap.getTable();
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
				if(table[i][j] == true) {
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
			table = ap.getTable();
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
			System.out.println("showBattleLayout - mainAppban:");
			for(Ship s: gd.getOwnFleet()) {
				System.out.println(s.name() + " koordináták: " + s.getCoordinates()[0] + "," +s.getCoordinates()[1]);
			}
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
			gp.startSinglePlayer(); // !!!!
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
			table[x][y] = true;
		} else if (ev.getButton() == MouseButton.SECONDARY) {
			table[x][y] = false;
		}
		drawTable();
	}
	
	// Ez frissíti a tábla kirajzolását.
	public void mousePressed(MouseEvent ev) {
		if(x >= 0 && y >= 0) {
			drawTable();
			System.out.println(x + " " + y);
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
	
	
	
	public boolean[][] getOwnTable() {
		return ownTable;
	}

	public void setOwnTable(boolean[][] ownTable) {
		MainApp.ownTable = ownTable;
	}



	public boolean[][] getTable() {
		return table;
	}
	
	public void setTable(boolean[][] table) {
		MainApp.table = table;
	}
	
	public void clearTable() {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				table[i][j] = false;
			}
		}
		if(fleet.size() != 0) {
			fleet.removeAll(fleet);
		}
		drawTable();
	}
	
	public void placeShip(Ship tempShip) {
		
	}
	public boolean checkShipIsPlaced() {
		for(Ship s: fleet) {
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

}