package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
//	Canvas mainCanvas;
	static GraphicsContext gc;
	
	public static void draw() {
		gc.setFill(Color.BLUE);
		gc.fillRect(5, 5, 100, 100);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		this.primaryStage.setTitle("Fleet Battle");
		initRootLayout();
	}
	
	public void initRootLayout() {
		try {
			FXMLLoader fxmlmain = new FXMLLoader(getClass().getResource("RootLayout.fxml"));
			VBox root = fxmlmain.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			

			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
