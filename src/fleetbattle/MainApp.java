package fleetbattle;
	
import java.io.IOException;

import fleetbattle.model.Game;
import fleetbattle.view.PlaceShipsLayoutController;
import fleetbattle.view.WelcomeLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private static Game game;				
	private static boolean[][] table;
	
	
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
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PlaceShipsLayout.fxml"));
			AnchorPane placeShipsLayout = loader.load();
			rootLayout.setCenter(placeShipsLayout);
			Canvas placingCanvas = (Canvas) loader.getNamespace().get("placingCanvas");
			GraphicsContext gc = placingCanvas.getGraphicsContext2D();
			gc.setFill(Color.CADETBLUE);
			gc.fillRect(10, 10, 280, 280);
			
			PlaceShipsLayoutController controller = loader.getController();
			
			controller.setMainApp(this);
			game = new Game();
			game.setupOfFields();
			game.placeAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void autoPlace() {
		AnchorPane placeShipsLayout;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PlaceShipsLayout.fxml"));
			placeShipsLayout = loader.load();
			rootLayout.setCenter(placeShipsLayout);
			Canvas placingCanvas = (Canvas) loader.getNamespace().get("placingCanvas");
			GraphicsContext gc = placingCanvas.getGraphicsContext2D();
			table = game.getTable();
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					if(table[i][j] == true) {
						gc.setFill(Color.DARKRED);
						gc.fillRect(i*30, j*30, 28, 28);
					} else {
						gc.setFill(Color.CADETBLUE);
						gc.fillRect(i*30, j*30, 28, 28);
					}
				}
			}
			PlaceShipsLayoutController controller = loader.getController();
			
			game.reset();
			game.placeAll();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
