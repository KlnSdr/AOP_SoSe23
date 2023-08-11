package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;


import javax.swing.*;
import java.awt.*;

import static Sinking.UI.Window.baseTitle;


public class MainScreen implements IView {
    @Override
    public void load(JFrame window) {
        window.setTitle(baseTitle);
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        JLabel player1Label = new JLabel("Eigene Flotte");
        GridBagConstraints gbcPlayer1Label = new GridBagConstraints();
        gbcPlayer1Label.gridx = 0;
        gbcPlayer1Label.gridy = 1;
//        gbcPlayer1Label.gridwidth = 10;
        gbcPlayer1Label.anchor = GridBagConstraints.CENTER;
        gbcPlayer1Label.insets = new Insets(0, 0, 10, 10);
        centerContainer.add(player1Label, gbcPlayer1Label);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcButtonPanel = new GridBagConstraints();
        gbcButtonPanel.gridx = 0;
        gbcButtonPanel.gridy = 0;
        gbcButtonPanel.insets = new Insets(10, 10, 10, 10);
        GridBagConstraints gbcButton = new GridBagConstraints();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));

                gbcButton.insets = new Insets(0, 0, 0, 0);
                gbcButton.gridx = col;
                gbcButton.gridy = row;
                final int finalRow = row;
                final int finalCol = col;
                button.addActionListener(e -> {
                    System.out.println("Player clicked on " + finalRow + " " + finalCol);
                    //fire(finalRow, finalCol);
                });
                buttonPanel.add(button, gbcButton);
            }
            gbcButtonPanel.insets = new Insets(0, 0, 0, 10);
        }
        centerContainer.add(buttonPanel);

        JLabel player2Label = new JLabel("Gegnerische Flotte");
        GridBagConstraints gbcPlayer2Label = new GridBagConstraints();
        gbcPlayer2Label.gridx = 1;
        gbcPlayer2Label.gridy = 1;
        gbcPlayer2Label.anchor = GridBagConstraints.CENTER;
        gbcPlayer2Label.insets = new Insets(0, 0, 10, 10);
        centerContainer.add(player2Label, gbcPlayer2Label);

        JPanel buttonPanelPlayer2 = new JPanel();

        buttonPanelPlayer2.setLayout(new GridBagLayout());
        GridBagConstraints gbcButtonPanelPlayer2 = new GridBagConstraints();
        gbcButtonPanelPlayer2.gridx = 1;
        gbcButtonPanelPlayer2.gridy = 0;
        gbcButtonPanelPlayer2.insets = new Insets(10, 10, 10, 10);
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
//                gbcbutton.insets = new Insets(0, 0, 0, 0);
                GridBagConstraints gbcButtonPlayer2 = new GridBagConstraints();
                gbcButtonPlayer2.gridx = col;
                gbcButtonPlayer2.gridy = row;
                final int finalRow = row;
                final int finalCol = col;
                button.addActionListener(e -> {
                    System.out.println("Player clicked on " + finalRow + " " + finalCol);
                    //fire(finalRow, finalCol);
                });
                buttonPanelPlayer2.add(button, gbcButtonPlayer2);
            }
        }
        centerContainer.add(buttonPanelPlayer2);

//        JLabel chatLabel = new JLabel("Chat");
//        GridBagConstraints gbcChatLabel = new GridBagConstraints();
//        gbcChatLabel.gridx = 2;
//        gbcChatLabel.gridy = 0;
//        gbcChatLabel.anchor = GridBagConstraints.CENTER;
//        gbcChatLabel.insets = new Insets(0, 0, 10, 10);
//        centerContainer.add(chatLabel, gbcChatLabel);
//
//        JTextField chatField = new JTextField();
//        GridBagConstraints gbcChatField = new GridBagConstraints();
//        chatField.setPreferredSize(new Dimension(120, chatField.getPreferredSize().height)); // Breite anpassen
//        chatField.setColumns(20); // Höhe anpassen
//        gbcChatField.gridx = 2;
//        gbcChatField.gridy = 1;
//        gbcChatField.anchor = GridBagConstraints.CENTER;
//        gbcChatField.insets = new Insets(0, 0, 10, 10);
//        centerContainer.add(chatField, gbcChatField);


    }

    @Override
    public void unload() {System.out.println("Unloading MainScreen"); }

}
