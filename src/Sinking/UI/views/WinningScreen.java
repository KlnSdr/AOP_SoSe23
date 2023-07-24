package Sinking.UI.views;

import Sinking.UI.views.Abstract.EndScreen;

import java.awt.*;

public class WinningScreen extends EndScreen {
    public WinningScreen() {
        super();
        this.message = ":D Du hast gewonnen!";
        this.backgroundColor = Color.GREEN;
        this.textColor = Color.BLACK;
    }
}
