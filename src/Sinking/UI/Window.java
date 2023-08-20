package Sinking.UI;

import javax.swing.*;

public class Window extends JFrame {
    public static final String baseTitle = "Schiffe versenken";

    public Window() {
        this.setSize(1000, 600);
        this.setLocation(100, 100);
        this.setTitle(baseTitle);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
}
