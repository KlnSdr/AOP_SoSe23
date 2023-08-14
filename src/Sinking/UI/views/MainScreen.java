package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;
import Sinking.http.client.Consistency;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Sinking.UI.Window.baseTitle;


public class MainScreen implements IView {

    private String player1;
    private String player2;
    private String url;
    private String whosNext;
    private JButton testConnectionButton;
    private JFrame window;
    private JPanel upperContainer;
    private JLabel whosNextLabel;

    @Override
    public void load(JFrame window, Json data) {
        this.window = window;
        window.setTitle(baseTitle);

        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(container);

        //Statusleiste
        upperContainer = new JPanel();
        upperContainer.setLayout(new GridBagLayout());
        upperContainer.setBackground(Color.WHITE);
        upperContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcUpperContainer = new GridBagConstraints();
        gbcUpperContainer.gridx = 0;
        gbcUpperContainer.gridy = 0;
        gbcUpperContainer.weightx = 1.0;
        gbcUpperContainer.fill = GridBagConstraints.HORIZONTAL;
        container.add(upperContainer, gbcUpperContainer);

        JLabel playingAgainstLabel = new JLabel( "Gegner: " + getPlayer2());
        GridBagConstraints gbcPlayingAgainstLabel = new GridBagConstraints();
        gbcPlayingAgainstLabel.gridx = 0;
        gbcPlayingAgainstLabel.gridy = 0;
        gbcPlayingAgainstLabel.weightx = 1.0;
        gbcPlayingAgainstLabel.anchor = GridBagConstraints.WEST;
        gbcPlayingAgainstLabel.insets = new Insets(10, 10, 10, 10);
        upperContainer.add(playingAgainstLabel, gbcPlayingAgainstLabel);

        whosNextLabel = new JLabel(getWhosNext());
        GridBagConstraints gbcWhosNextLabel = new GridBagConstraints();
        gbcWhosNextLabel.gridx = 1;
        gbcWhosNextLabel.gridy = 0;
        gbcWhosNextLabel.weightx = 1.0;
        gbcWhosNextLabel.anchor = GridBagConstraints.CENTER;
        gbcWhosNextLabel.insets = new Insets(10, 10, 10, 10);
        upperContainer.add(whosNextLabel, gbcWhosNextLabel);

        testConnectionButton = new JButton("Verbindung prüfen");
        testConnectionButton.setPreferredSize(new Dimension(140, 20));
        GridBagConstraints gbcTestConnectionButton = new GridBagConstraints();
        gbcTestConnectionButton.gridx = 2;
        gbcTestConnectionButton.gridy = 0;
        gbcTestConnectionButton.weightx = 0.8;
        gbcTestConnectionButton.anchor = GridBagConstraints.EAST;
        gbcTestConnectionButton.insets = new Insets(10, 10, 10, 10);
        testConnectionButton.addActionListener(e -> {
            System.out.println("Test Connection");
            checkConnection();
        });
        upperContainer.add(testConnectionButton, gbcTestConnectionButton);

        JLabel urlTooltipLabel = new JLabel("•");
        urlTooltipLabel.setToolTipText(getServerUrl());
        GridBagConstraints gbcUrlTooltipLabel = new GridBagConstraints();
        gbcUrlTooltipLabel.gridx = 3;
        gbcUrlTooltipLabel.gridy = 0;
        gbcUrlTooltipLabel.anchor = GridBagConstraints.CENTER;
        gbcUrlTooltipLabel.insets = new Insets(10, 10, 10, 10);
        upperContainer.add(urlTooltipLabel, gbcUrlTooltipLabel);


        //Spielfelder
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcCenterContainer = new GridBagConstraints();
        gbcCenterContainer.gridx = 0;
        gbcCenterContainer.gridy = 1;
        gbcCenterContainer.weightx = 1.0;
        gbcCenterContainer.weighty = 1.0;
        gbcCenterContainer.fill = GridBagConstraints.BOTH;
        container.add(centerContainer, gbcCenterContainer);


        //Anzeige
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new GridBagLayout());
        bottomContainer.setBackground(Color.WHITE);
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcBottomContainer = new GridBagConstraints();
        gbcBottomContainer.gridx = 0;
        gbcBottomContainer.gridy = 2;
        gbcBottomContainer.weightx = 1.0;
        gbcBottomContainer.fill = GridBagConstraints.HORIZONTAL;
        container.add(bottomContainer, gbcBottomContainer);

        //Player1
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcButtonPanel = new GridBagConstraints();
        gbcButtonPanel.gridx = 0;
        gbcButtonPanel.gridy = 0;
        gbcButtonPanel.weightx = 1.0;
        buttonPanel.setBorder(BorderFactory.createEmptyBorder( 20, 20, 20, 20));
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

        //Player2
        JPanel buttonPanelPlayer2 = new JPanel();
        buttonPanelPlayer2.setLayout(new GridBagLayout());
        GridBagConstraints gbcButtonPanelPlayer2 = new GridBagConstraints();
        gbcButtonPanelPlayer2.gridx = 1;
        gbcButtonPanelPlayer2.gridy = 0;
        gbcButtonPanelPlayer2.weightx = 1.0;
        buttonPanelPlayer2.setBorder(BorderFactory.createEmptyBorder( 20, 20, 20, 20));
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
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

        JLabel player1Label = new JLabel("Eigene Flotte");
        GridBagConstraints gbcPlayer1Label = new GridBagConstraints();
        gbcPlayer1Label.gridx = 0;
        gbcPlayer1Label.gridy = 1;
        gbcPlayer1Label.weightx = 1.0;
        gbcPlayer1Label.anchor = GridBagConstraints.CENTER;
        gbcPlayer1Label.insets = new Insets(0, 0, 10, 10);
        bottomContainer.add(player1Label, gbcPlayer1Label);

        JLabel player2Label = new JLabel("Gegnerische Flotte");
        GridBagConstraints gbcPlayer2Label = new GridBagConstraints();
        gbcPlayer2Label.gridx = 1;
        gbcPlayer2Label.gridy = 1;
        gbcPlayer2Label.weightx = 1.0;
        gbcPlayer2Label.anchor = GridBagConstraints.CENTER;
        gbcPlayer2Label.insets = new Insets(0, 0, 10, 10);
        bottomContainer.add(player2Label, gbcPlayer2Label);

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

    private void setServerUrl(String url) {
        this.url = url;
    }
    private String getServerUrl() {
        return url;
    }
    private void setWhosNext(String name) {
        whosNext = name;
        whosNextLabel.setText(whosNext);
        window.repaint();
    }
    private String getWhosNext() {
        return whosNext;
    }

    private void checkConnection() {
        Consistency.getInstance().checkConnection(() -> {
            System.out.println("success");
            setResetButtonTestColor(Color.GREEN);
        }, () -> {
            System.out.println("failure");
            setResetButtonTestColor(Color.RED);
        });
    }

    private void setResetButtonTestColor(Color color) {
        testConnectionButton.setForeground(color);
        this.window.repaint();

        ActionListener timerAction = e -> {
            testConnectionButton.setForeground(Color.BLACK);
            window.repaint();
        };
        Timer timer = new Timer(2000, timerAction);
        timer.setRepeats(false);
        timer.start();
    }

    private void setPlayer2(String name) {
        player2 = name;
    }
    private String getPlayer2() {
        return player2;
    }
    private void setPlayer1(String name) {
        player1 = name;
    }
    private String getPlayer1() {
        return player1;
    }

    @Override
    public void unload() {System.out.println("Unloading MainScreen"); }

}
