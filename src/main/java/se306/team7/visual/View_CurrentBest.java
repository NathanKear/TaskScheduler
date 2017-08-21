package se306.team7.visual;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;

public class View_CurrentBest {

    public Text _text;
    private static View_CurrentBest _viewCurrentBest;

    private static final String CURRENT_BEST_STRING = "Current best cost found:\n";
    private static final String CURRENT_STATUS_IN_PROGRESS = "Status: still calculating...\n";
    private static final String CURRENT_STATUS_COMPLETED = "Status: Completed!\n";

    private View_CurrentBest() {
        _text = new Text(CURRENT_BEST_STRING);
        this.decorateView();
    }
    
   public static View_CurrentBest getInstance(){
	   if (_viewCurrentBest == null){
		   _viewCurrentBest = new View_CurrentBest();
	   }
       return _viewCurrentBest;
   }

   private void decorateView() {
       _text.setFont(new Font(30));
       _text.setTextAlignment(TextAlignment.CENTER);
   }

    public void update(int numOfLevels, int numOfCores, int currentBestCost, HashMap<Integer, Integer> histogram, HashMap<Integer, Integer> coreCurrentLevel, boolean isFinished) {
       if (isFinished) {
           _text.setText(CURRENT_BEST_STRING + currentBestCost + " time units\n" + CURRENT_STATUS_COMPLETED);
       } else {
           _text.setText(CURRENT_BEST_STRING + currentBestCost + " time units\n" + CURRENT_STATUS_IN_PROGRESS);
       }
    }
}
