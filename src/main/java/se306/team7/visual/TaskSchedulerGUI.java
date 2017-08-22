package se306.team7.visual;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se306.team7.Metrics;
import se306.team7.TaskScheduler;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.application.Platform;


public class TaskSchedulerGUI extends Application {
	
	VisualModel _model;
	static String[] _args;
	 private List<ITaskSchedulerView> _views;

	@SuppressWarnings("restriction")
	@Override
    public void start(Stage primaryStage) {
		
        primaryStage.setTitle("Task Scheduler");

        BorderPane root = new BorderPane();

        View_Histogram hist = View_Histogram.getInstance();
        View_LineGraph lineGraph = View_LineGraph.getInstance();
        View_CurrentBest currentBest = View_CurrentBest.getInstance();

        VBox leftVBox = new VBox(hist._barChart);
        VBox rightVBox = new VBox(currentBest._text, lineGraph._lineChart);
        root.setLeft(leftVBox);
        root.setRight(rightVBox);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();
        _views = new ArrayList<ITaskSchedulerView>();
		 
		 _views.add(View_CurrentBest.getInstance());
		 _views.add(View_Histogram.getInstance());
		 _views.add(View_LineGraph.getInstance()); 
        _model = new VisualModel(); //sets up timer
        _model.startTimer();
        
       
       
        
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
      	      Thread.sleep(1);
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
                
                //task.cancel(true);
                _model.stopTimer();
                
                //final update
                for (ITaskSchedulerView view : _views){
            		System.out.println("update cost is " + Metrics.getCurrentBestCost());
		        	view.update(Metrics.getCurrentBestCost(), Metrics.getHistogram(), Metrics.getCoreCurrentLevel());
		        }
            }
        });

        service.start();
      	//th.start();
    }
	
	/**
	 * In the final project metrics should be initiated in the algorithm
	 */
	private void setUpMetrics() {
		
		
	}

	public static void main(String[] args) {
		_args = args;
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

                		/**int i = 0;
	                	while (true){
	                		System.out.print("set cost to : " + i);
		                	Metrics.setCurrentBestCost(i);
		                	
		                	if (i == 1000000000){
		                		break;
		                	}
		                	
		                	i ++;
	                	}**/
	                	
	                    TaskScheduler.main(_args);
	                	
						return null;
	                       
	                        
	                }
	            };
	        }
	    }

}