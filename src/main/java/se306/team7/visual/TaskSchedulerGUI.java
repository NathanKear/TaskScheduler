package se306.team7.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
import se306.team7.Metrics;
import se306.team7.TaskScheduler;
import se306.team7.Digraph.Digraph;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.application.Platform;


public class TaskSchedulerGUI extends Application {
	
	VisualModel _model;
	 private List<ITaskSchedulerView> _views;
	 private static Digraph _digraph; // this is needed to execute the background task
	 private Text statusText;

	@SuppressWarnings("restriction")
	@Override
    public void start(Stage primaryStage) {
		
        primaryStage.setTitle("Task Scheduler");

        statusText = new Text(" In progress...");
        final Text status = new Text("Status: In progress...");
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
        _views = new ArrayList<ITaskSchedulerView>();
		 
		 _views.add(View_CurrentBest.getInstance());
		 _views.add(View_Histogram.getInstance());
		 _views.add(View_LineGraph.getInstance()); 
        _model = new VisualModel(); //sets up timer

        
        final Task task = new Task<Void>() {
      	  @Override
      	  public Void call() throws Exception {
      	    while (true) {
      	      Platform.runLater(new Runnable() {
      	        @Override
      	        public void run() {
      	        	for (ITaskSchedulerView view : _views){
  			        	view.update(Metrics.getCurrentBestCost(), Metrics.getHistogram(), Metrics.getCoreCurrentLevel());
  			        }
      	        }
      	      });
      	      Thread.sleep(100);
      	    }
      	  }
      	};
      	Thread th = new Thread(task);
      	th.setDaemon(true);
      	
      	 
        AlgorithmService service = new AlgorithmService();
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                System.out.println("done:" + t.getSource().getValue());
                status.setText("Status: Finished");
                task.cancel(true);
                //_model.stopTimer();
                
                //final update
                for (ITaskSchedulerView view : _views){
            		System.out.println("update cost is " + Metrics.getCurrentBestCost());
		        	view.update(Metrics.getCurrentBestCost(), Metrics.getHistogram(), Metrics.getCoreCurrentLevel());
		        }
            }
        });

        service.start();
      	th.start();
        //_model.startTimer();
    }

	public static void LauchGUI(String[] args, Digraph d) {
		_digraph = d;
        launch(args);
    }
	
	/**
	 * Inner class for Javafx Service to run the search algorithm in background thread
	 * @author cli727
	 *
	 */
	 private static class AlgorithmService extends Service<Void> {
		 	// change Void to return type of call
	        protected Task<Void> createTask() {
	        	
	            return new Task<Void>() {
	            	
	                protected Void call() {
	                	
	                	TaskScheduler.executeAlgorithm(_digraph);
	                	
						return null;
	                       
	                        
	                }
	            };
	        }
	    }

}