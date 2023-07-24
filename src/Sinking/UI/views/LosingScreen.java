package Sinking.UI.views;

import Sinking.UI.views.Abstract.EndScreen;

import java.awt.*;

public class LosingScreen extends EndScreen {
    public LosingScreen() {
        super();
        this.message = ":( Du hast verloren!";
        this.backgroundColor = Color.RED;
        this.textColor = Color.WHITE;
    }
}
