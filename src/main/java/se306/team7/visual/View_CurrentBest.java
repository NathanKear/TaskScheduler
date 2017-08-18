package se306.team7.visual;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class View_CurrentBest {

    public Text _text;

    public View_CurrentBest() {

        _text = new Text("Current best:\n 20 time units");
        _text.setFont(new Font(30));

    }
}
