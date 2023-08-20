package Sinking.UI.views;

import Sinking.Game.Data.ClientStore;
import Sinking.UI.IView;
import Sinking.common.Tupel;
import Sinking.http.client.Client;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;
import Sinking.http.client.Consistency;
import Sinking.http.client.Request;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static Sinking.UI.Window.baseTitle;


public class MainScreen implements IView {

    private String player1;
    private String player2;
    private String url;
    private String whosNext;
    private JButton testConnectionButton;
    private JPanel upperContainer;
    private JLabel whosNextLabel;
    private final Tupel<Integer, Integer>[] ships = ClientStore.getInstance().getShips();
    private JButton [] buttonMatrixMine = new JButton[100];
    private JButton [] buttonMatrixEnemie = new JButton[100];
    private Timer boardUpdate;

    @Override
    public void load(JFrame window, Json data) {
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

        getEnemieName();
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
            for (int col = 0; col < 10; col++){
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
                colorButton(button, row, col);
                buttonMatrixMine[row * 10 + col] = button;
                gbcButton.insets = new Insets(0, 0, 0, 0);
                gbcButton.gridx = col;
                gbcButton.gridy = row;
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
                button.setBackground(Color.BLUE);
                buttonMatrixEnemie[10*row+col] = button;
                GridBagConstraints gbcButtonPlayer2 = new GridBagConstraints();
                gbcButtonPlayer2.gridx = col;
                gbcButtonPlayer2.gridy = row;
                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> {
                    JButton source = ((JButton)e.getSource());
                    System.out.println("Player clicked on " + finalRow + " " + finalCol);
                    source.setText("⌛");
                    shootAt(finalCol, finalRow, source);
                    // winnerAvailable();
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

        boardUpdate = new Timer(1000, e -> {
            getGameboardUpdate();
        });
        boardUpdate.setRepeats(true);
        boardUpdate.start();

        disableButtons(buttonMatrixEnemie);
    }
/*
    private void winnerAvailable() {

        if(Gamestate.hasWinner()) {
            if (nickname.equals(Gamestate.spieler1)) {
                ViewLoader.getInstance().loadView("WinningScreen", viewArgs);
            } else {
                ViewLoader.getInstance().loadView("LosingScreen", viewArgs);
            }
        }
    }*/

    private void colorButton(JButton button, int col, int row) {
        boolean foundShip = false;

        for (Tupel<Integer, Integer> ship : ships) {
            int x = ship._1();
            int y = ship._2();
            if (x == col && y == row) {
                foundShip = true;
                break;
            }
        }

        if (foundShip) {
            button.setBackground(Color.GRAY);
        } else {
            button.setBackground(Color.BLUE);
        }
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
        testConnectionButton.setBackground(color);

        ActionListener timerAction = e -> {
            testConnectionButton.setBackground(null);
        };
        Timer timer = new Timer(2000, timerAction);
        timer.setRepeats(false);
        timer.start();
    }

    private void shootAt(int x, int y, JButton src) {
        disableButtons(buttonMatrixEnemie);
        setWhosNext(getPlayer2());

        ClientStore store = ClientStore.getInstance();
        Client client = store.getClient();
        String gameId = store.getGameId();
        String token = store.getPlayerToken();

        Request req = client.newRequest("/fireAt");
        req.setQuery("id", gameId);
        req.setBody("playerToken", token);
        req.setBody("x", Integer.toString(x));
        req.setBody("y", Integer.toString(y));

        client.post(req, response -> {
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            src.setText("");
            if (response.getBody().hasKey("hitShip") && response.getBody().get("hitShip").orElse("false").equals("true")) {
                src.setBackground(Color.GRAY);
            } else {
                src.setBackground(Color.RED);
            }
        });
    }

    private void getGameboardUpdate() {
        ClientStore store = ClientStore.getInstance();
        Client client = store.getClient();
        String gameId = store.getGameId();
        String token = store.getPlayerToken();

        Request req = client.newRequest("/getGamestate");
        req.setQuery("id", gameId);
        req.setBody("playerToken", token);

        client.post(req, response -> {
            if (response.getStatusCode() == 200) {
                analyzeResponse(response.getBody());
            }
        });
    }

    private void analyzeResponse(Json body) {
        if (body.hasKey("hasWinner") && body.get("hasWinner").orElse("false").equals("true")) {
            boardUpdate.stop();
            if (body.hasKey("winner") && body.get("winner").orElse("").equals(ClientStore.getInstance().getNickname())) {
                ViewLoader.getInstance().loadView("WinningScreen");
            } else {
                ViewLoader.getInstance().loadView("LosingScreen");
            }
            return;
        }

        if (body.hasKey("isNext") && body.get("isNext").orElse("false").equals("true")) {
            enableWaterButtons();
            setWhosNext(ClientStore.getInstance().getNickname());
        } else {
            setWhosNext(getPlayer2());
        }

        if (body.hasKey("own") && body.get("own").isPresent()) {
            updateOwnGameboardMatrix(body.get("own").get());
        }
    }

    private void disableButtons(JButton[] matrix){
        for (int i = 0; i < matrix.length; i++){
            matrix[i].setEnabled(false);
        }
    }

    private void enableWaterButtons() {
        for (JButton jButton : buttonMatrixEnemie) {
            if (jButton.getBackground() == Color.BLUE) {
                jButton.setEnabled(true);
            }
        }
    }

    private void updateOwnGameboardMatrix(String serverResponse) {
        for(int i = 0; i < serverResponse.length(); i++) {
            char c = serverResponse.charAt(i);
            if (c == 'U' && buttonMatrixMine[i].getBackground() != Color.GRAY) {
                buttonMatrixMine[i].setBackground(Color.BLUE);
            } else if (c == 'H') {
                buttonMatrixMine[i].setBackground(Color.RED);
            } else if (c == 'M') {
                buttonMatrixMine[i].setBackground(Color.BLACK);
            }
        }
    }

    private void setPlayer2(String name) {
        player2 = name;
    }
    private String getPlayer2(){
        return player2;
    }
    public void getEnemieName(){
        ClientStore store = ClientStore.getInstance();
        Client client = store.getClient();
        String gameId = store.getGameId();
        String token = store.getPlayerToken();

        Request req = client.newRequest("/getName");
        req.setQuery("id", gameId);
        req.setBody("playerToken", token);

        client.post(req, response -> {
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            String name;
            if(response.getBody().hasKey("Opponentname")){
                setPlayer2(response.getBody().get("Opponentname").orElse(""));
            }
        });
    }
    private void setPlayer1(String name) {
        player1 = name;
    }
    private String getPlayer1() {
        return player1;
    }

    /*public int placingship(JPanel p, int r, int c){
        int n = r*10 +c;
       /* p.getComponent(n).setBackground(Color.GREEN);
        p.getComponent(n+1).setBackground(Color.GREEN);
        p.getComponent(n+2).setBackground(Color.GREEN);
        System.out.println(n);
        return n;*/

    //}
    @Override
    public void unload() {System.out.println("Unloading MainScreen"); }

}
