package Sinking;

import Sinking.UI.ViewLoader;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("How are you, world?");
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
        SwingUtilities.invokeLater(() -> ViewLoader.getInstance().loadView("LoosingScreen"));
    }
}
