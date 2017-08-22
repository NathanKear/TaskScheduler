package se306.team7.visual;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TaskSchedulerGUI extends Application {
	
	VisualModel _model;

	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Scheduler");

        Text status = new Text("Status: In progress...");
        status.setFont(new Font(30));
        status.setTextAlignment(TextAlignment.CENTER);

        View_Histogram hist = View_Histogram.getInstance();
        View_LineGraph lineGraph = View_LineGraph.getInstance();
        View_CurrentBest currentBest = View_CurrentBest.getInstance();
        /*
        Visual look option 1

        BorderPane root = new BorderPane();

        VBox leftVBox = new VBox(hist._barChart);
        VBox rightVBox = new VBox(currentBest._text, lineGraph._lineChart);
        root.setLeft(leftVBox);
        root.setRight(rightVBox);
         */

        /*
        Visual look option 2
         */
        HBox topHBox = new HBox(status, currentBest._text);
        HBox midHBox = new HBox(currentBest._text);
        HBox bottomHBox = new HBox(hist._barChart, lineGraph._lineChart);

        topHBox.setAlignment(Pos.CENTER);
        midHBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(topHBox, midHBox, bottomHBox);

        primaryStage.setResizable(false);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();
        
        _model = new VisualModel(); //sets up timer
        _model.startTimer();
        
    }
	
	public static void main(String[] args) {
        launch(args);
    }

}