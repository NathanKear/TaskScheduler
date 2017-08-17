package se306.team7.visual;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TaskSchedulerGUI extends Application {

	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Scheduler");

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setScene(scene);

        primaryStage.show();
    }
	
	public static void main(String[] args) {
        launch(args);
    }

}