package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;

import javax.swing.*;
import java.awt.*;

public class DevScreen implements IView {
    @Override
    public void load(JFrame window, Json data) {
        window.setTitle("Dev Screen");
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        window.add(container);

        JButton winningScreenButton = new JButton("Winning Screen");
        winningScreenButton.setBounds(0, 0, 150, 20);
        winningScreenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        winningScreenButton.addActionListener(e -> {
            System.out.println("Loading WinningScreen");
            ViewLoader.getInstance().loadView("WinningScreen");
        });
        container.add(winningScreenButton);

        JButton losingScreenButton = new JButton("Losing Screen");
        losingScreenButton.setBounds(0, 100, 150, 20);
        losingScreenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        losingScreenButton.addActionListener(e -> {
            System.out.println("Loading LosingScreen");
            ViewLoader.getInstance().loadView("LosingScreen");
        });
        container.add(losingScreenButton);
    }

    @Override
    public void unload() {
        System.out.println("Unloading DevScreen");
    }
}
