package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.UI.Window;

import javax.swing.*;

public class Example1 implements IView {
    @Override
    public void load(JFrame window) {
        System.out.println("Loading example 1...");

        window.setTitle(Window.baseTitle + " - Example 1");

        JLabel label = new JLabel("Example 1");
        label.setBounds(10, 10, 150, 20);
        window.add(label);

        JButton button = new JButton("load Example 2");
        button.setBounds(10, 40, 150, 20);
        button.addActionListener(e -> {
            ViewLoader.getInstance().loadView("Example2");
        });
        window.add(button);
    }

    @Override
    public void unload() {
        System.out.println("Unloading example 1...");
    }
}
