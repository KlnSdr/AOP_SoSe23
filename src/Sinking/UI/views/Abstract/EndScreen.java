package Sinking.UI.views.Abstract;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.UI.Window;

import javax.swing.*;
import java.awt.*;

public abstract class EndScreen implements IView {
    protected String message = "";
    protected Color backgroundColor;
    protected Color textColor;
    @Override
    public void load(JFrame window) {
        System.out.println("Loading EndScreen");
        window.setTitle(Window.baseTitle);

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(backgroundColor);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        JPanel container = new JPanel();
        container.setBackground(backgroundColor);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(message);
        label.setForeground(textColor);
        label.setBounds(0, 0, 300, 20);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(label);

        container.add(Box.createVerticalStrut(10)); // add spacing between label and button

        JButton backHome = new JButton("Zurück zum Hauptmenü");
        backHome.setBounds(0, 100, 300, 20);
        backHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        backHome.addActionListener(e -> ViewLoader.getInstance().loadView("MainMenu"));
        container.add(backHome);

        centerContainer.add(container);
    }

    @Override
    public void unload() {
        System.out.println("Unloading EndScreen");
    }
}
