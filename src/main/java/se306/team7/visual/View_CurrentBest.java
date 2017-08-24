package se306.team7.visual;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.ConcurrentHashMap;

public class View_CurrentBest implements ITaskSchedulerView {

    public Text _text;
    private static View_CurrentBest _viewCurrentBest;

    private static final String CURRENT_BEST_STRING = "Current best cost found: ";

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

    /**
     * Simply shows the best full schedule cost found so far.
     * @param currentBestCost
     * @param histogram
     * @param coreCurrentLevel
     */
    public void update(int currentBestCost, ConcurrentHashMap<Integer, Integer> histogram, ConcurrentHashMap<Integer, Integer> coreCurrentLevel) {
        _text.setText(CURRENT_BEST_STRING + currentBestCost + " time units");
    }
}
