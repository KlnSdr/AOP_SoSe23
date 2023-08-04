package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;


import javax.swing.*;
import java.awt.*;

import static Sinking.UI.Window.baseTitle;

public class MainMenu implements IView {
    @Override
    public void load(JFrame window) {
        window.setTitle(baseTitle);
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        JButton newLocalGameButton = new JButton("New Local Game");
        GridBagConstraints gbcNewLocalGame = new GridBagConstraints();
        gbcNewLocalGame.gridx = 0;
        gbcNewLocalGame.gridy = 0;
        gbcNewLocalGame.anchor = GridBagConstraints.WEST;
        gbcNewLocalGame.insets = new Insets(0, 0, 10, 10);
        newLocalGameButton.addActionListener(e -> {
            System.out.println("Loading New Local Game");
            ViewLoader.getInstance().loadView("DevScreen");
        });
        centerContainer.add(newLocalGameButton, gbcNewLocalGame);

        JButton newOnlineGameButton = new JButton("New Online Game");
        GridBagConstraints gbcNewOnlineGame = new GridBagConstraints();
        gbcNewOnlineGame.gridx = 0;
        gbcNewOnlineGame.gridy = 1;
        gbcNewOnlineGame.anchor = GridBagConstraints.WEST;
        gbcNewOnlineGame.insets = new Insets(0, 0, 10, 10);
        newOnlineGameButton.addActionListener(e -> {
            System.out.println("Loading New Online Game");
            ViewLoader.getInstance().loadView("DevScreen");
        });
        centerContainer.add(newOnlineGameButton, gbcNewOnlineGame);

        JButton joinGameButton = new JButton("Join Online Game");
        GridBagConstraints gbcJoinGame = new GridBagConstraints();
        gbcJoinGame.gridx = 1;
        gbcJoinGame.gridy = 0;
        gbcJoinGame.anchor = GridBagConstraints.EAST;
        gbcJoinGame.insets = new Insets(0, 0, 10, 0);
        joinGameButton.addActionListener(e -> {
            System.out.println("Loading Join Game");
            ViewLoader.getInstance().loadView("DevScreen");
        });
        centerContainer.add(joinGameButton, gbcJoinGame);

    }

    @Override
    public void unload() {System.out.println("Unloading MainMenu"); }

}
