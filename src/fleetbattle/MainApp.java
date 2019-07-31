package fleetbattle;
	
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import application.Main;
import fleetbattle.model.Game;
import fleetbattle.model.Ship;
import fleetbattle.view.PlaceShipsLayoutController;
import fleetbattle.view.WelcomeLayoutController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class MainApp extends Application {

	private Canvas canvas = new Canvas(398,398);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	private Stage primaryStage;
	private BorderPane rootLayout;
	private static BorderPane placingPane;
	private static Game game;				
	private static boolean[][] table;
	private boolean singlePlayer = true;;
	static int x = 0;
	static int y = 0;
	String shipName = "";
	Ship carrier = Ship.CARRIER;
	Ship destroyer = Ship.DESTROYER;
	Ship submarine = Ship.SUBMARINE;
	Ship cruiser = Ship.CRUISER;
	Ship patrolBoat = Ship.PATROLBOAT;
	ArrayList<Ship> fleet = new ArrayList<>();
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		this.primaryStage.setTitle("Fleet Battle");
		initRootLayout();
		showWelcomeLayout();
	}
	
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
	
	public void showWelcomeLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WelcomeLayout.fxml"));
			AnchorPane welcomeLayout = loader.load();
			rootLayout.setCenter(welcomeLayout);
			WelcomeLayoutController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void showPlaceShipsLayout() {
		game = new Game();
		game.setupOfFields();
		placeShips();
	}
	
	public void autoPlace() {
		game.reset();
		game.placeAll();
		placeShips();
		drawTable();
	}

	public void drawTable() {
			table = game.getTable();
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
	
	public void placeShip(String shipName) {
		
	}
	
	public void placeShips() {
		try {
			AnchorPane placeShipsLayout;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PlaceShipsLayout_new.fxml"));
			placeShipsLayout = loader.load();
			placingPane = (BorderPane) loader.getNamespace().get("placingPane");
			rootLayout.setCenter(placeShipsLayout);
			placingPane.setCenter(canvas);
			table = game.getTable();
			ToggleGroup group = new ToggleGroup();
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
//			rbCarrier.setSelected(true);
//			shipName = ((RadioButton)group.getSelectedToggle()).getText().toUpperCase();
			drawTable();
			canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::mouseDraggedOrReleased);
			canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, this::mouseDraggedOrReleased);
			canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, this::mouseReleased);
						
			
			PlaceShipsLayoutController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Ship> getFleet() {
		return fleet;
	}
	
	public void initFleet() {
		fleet.add(carrier);
		fleet.add(destroyer);
		fleet.add(submarine);
		fleet.add(cruiser);
		fleet.add(patrolBoat);
	}
	
	public String getShipName() {
		return shipName;
	}
	
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
	public void mouseReleased(MouseEvent ev) {
		if(x >= 0 && y >= 0) {
			drawTable();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
