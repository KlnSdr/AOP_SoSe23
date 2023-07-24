package Sinking;

import Sinking.UI.ViewLoader;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> ViewLoader.getInstance().loadView("LoosingScreen"));
    }
}
