package Sinking.UI.views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class PlacingShips {
    static int state = 0;
    private JButton[] buttons;
    public JPanel createShips(JPanel buttonPanel){
        Window window = new Sinking.UI.Window();
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);
        window.setVisible(true);

        String[] shipList = {"Schiffe", "Schlachtschiff", "Kreuzer", "Zerstoerer", "U-Boot"};
        JComboBox<String> ships = new JComboBox<>(shipList);
        ships.setSize(100, 50);

        centerContainer.add(ships, 0);

        JPanel gameboard = new JPanel();
        gameboard.setLayout(new GridBagLayout());
        GridBagConstraints gbcButtonPanel = new GridBagConstraints();
        gbcButtonPanel.gridx = 0;
        gbcButtonPanel.gridy = 0;
        gameboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbcButton = new GridBagConstraints();
        buttons = new JButton[100];

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = new JButton();
                buttons[10 * row + col] = button;
                button.setPreferredSize(new Dimension(30, 30));

                gbcButton.insets = new Insets(0, 0, 0, 0);
                gbcButton.gridx = col;
                gbcButton.gridy = row;
                int finalRow = row;
                int finalCol = col;
                int n = (10 * finalRow + finalCol);
                    button.addActionListener(e1 -> {
                        System.out.println("Player clicked on " + finalRow + " " + finalCol);
                        if (state% 2 == 0) {
                            placingship(gameboard, n, ships.getSelectedItem());
                            state++;
                        } else {
                            for(int i = 0; i < buttons.length; i++){
                                if(buttons[i].getActionListeners().length == 2){
                                    ActionListener[] actions = buttons[i].getActionListeners();
                                    ActionListener a = actions[0];
                                    buttons[i].removeActionListener(a);
                                }
                            }
                            state++;
                        }
                    });

                gameboard.add(button, gbcButton);

            }
            gbcButtonPanel.insets = new Insets(0, 0, 0, 10);
        }
        for (int i = 0; i < gameboard.getComponents().length; i++) {
            gameboard.getComponent(i).setBackground(Color.BLUE);
        }
        centerContainer.add(gameboard);

        JButton bttn = new JButton("zurueck");
        bttn.addActionListener(e -> {
            window.dispose();
        });
        centerContainer.add(bttn);

        buttonPanel = gameboard;
        return buttonPanel;

    }


    public boolean placingship(JPanel p, int n, Object selectedItem) {
        if (selectedItem == null) {
            return false;
        }
        boolean status = false;
        if (selectedItem.equals("U-Boot")) {
            if (isGrey(p,n)) {
                p.getComponent(n).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 1) && rightplacement(n, n + 1, selectedItem) && isGrey(p,n+1)) {
                p.getComponent(n + 1).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 10) && isGrey(p,n-10)) {
                p.getComponent(n - 10).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 10) && isGrey(p,n+10)) {
                p.getComponent(n + 10).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 1) && rightplacement(n, n - 1, selectedItem) && isGrey(p,n-1)) {
                p.getComponent(n - 1).setBackground(Color.GREEN);
            }
            confirm(p, n, selectedItem);
            return true;
        }
        if (selectedItem.equals("Kreuzer")) {

            if (!(p.getComponent(n).getBackground() == Color.GRAY)) {
                p.getComponent(n).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 3) && rightplacement(n, n + 3, selectedItem) && isGrey(p,n+3) && isGrey(p,n+2) && isGrey(p,n+1)) {
                p.getComponent(n + 3).setBackground(Color.GREEN);
                p.getComponent(n + 2).setBackground(Color.GREEN);
                p.getComponent(n + 1).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 3) && rightplacement(n, n - 3, selectedItem) && isGrey(p,n-3) && isGrey(p,n-2) && isGrey(p,n-1)) {
                p.getComponent(n - 3).setBackground(Color.GREEN);
                p.getComponent(n - 2).setBackground(Color.GREEN);
                p.getComponent(n - 1).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 30) && isGrey(p,n-30) && isGrey(p,n-20) && isGrey(p,n-10)) {
                p.getComponent(n - 30).setBackground(Color.GREEN);
                p.getComponent(n - 20).setBackground(Color.GREEN);
                p.getComponent(n - 10).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 30) && isGrey(p,n+30) && isGrey(p,n+20) && isGrey(p,n+10)) {
                p.getComponent(n + 30).setBackground(Color.GREEN);
                p.getComponent(n + 20).setBackground(Color.GREEN);
                p.getComponent(n + 10).setBackground(Color.GREEN);
            }
            confirm(p, n, selectedItem);
            return true;
        }

        if (selectedItem.equals("Schlachtschiff")) {
            if (!(p.getComponent(n).getBackground() == Color.GRAY)) {
                p.getComponent(n).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 4) && rightplacement(n, n + 4, selectedItem) && isGrey(p,n+4) && isGrey(p,n+3) && isGrey(p,n+2) && isGrey(p,n+1)){
                p.getComponent(n + 4).setBackground(Color.GREEN);
                p.getComponent(n + 3).setBackground(Color.GREEN);
                p.getComponent(n + 2).setBackground(Color.GREEN);
                p.getComponent(n + 1).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 4) && rightplacement(n, n-4, selectedItem) && isGrey(p,n-4) && isGrey(p,n-3) && isGrey(p,n-2) && isGrey(p,n-1)) {
                p.getComponent(n - 4).setBackground(Color.GREEN);
                p.getComponent(n - 3).setBackground(Color.GREEN);
                p.getComponent(n - 2).setBackground(Color.GREEN);
                p.getComponent(n - 1).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 40) && isGrey(p,n-40) && isGrey(p,n-30) && isGrey(p,n-20) && isGrey(p,n-10)) {
                p.getComponent(n - 40).setBackground(Color.GREEN);
                p.getComponent(n - 30).setBackground(Color.GREEN);
                p.getComponent(n - 20).setBackground(Color.GREEN);
                p.getComponent(n - 10).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 40) && isGrey(p,n+40) && isGrey(p,n+30) && isGrey(p,n+20) && isGrey(p,n+10)) {
                p.getComponent(n + 40).setBackground(Color.GREEN);
                p.getComponent(n + 30).setBackground(Color.GREEN);
                p.getComponent(n + 20).setBackground(Color.GREEN);
                p.getComponent(n + 10).setBackground(Color.GREEN);
            }
            confirm(p, n, selectedItem);
            return true;
        }

        if (selectedItem.equals("Zerstoerer")) {
            if (!(p.getComponent(n).getBackground() == Color.GRAY)) {
                p.getComponent(n).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 2) && rightplacement(n, n + 2, selectedItem) && isGrey(p,n+2) && isGrey(p,n+1)) {
                p.getComponent(n + 2).setBackground(Color.GREEN);
                p.getComponent(n + 1).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 2) && rightplacement(n, n - 2, selectedItem) && isGrey(p,n-2) && isGrey(p,n-1)) {
                p.getComponent(n - 2).setBackground(Color.GREEN);
                p.getComponent(n - 1).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n - 20) && isGrey(p,n-20) && isGrey(p,n-10)) {
                p.getComponent(n - 20).setBackground(Color.GREEN);
                p.getComponent(n - 10).setBackground(Color.GREEN);
            }
            if (boundscheck(p, n + 20) && isGrey(p,n+20) && isGrey(p,n+10)) {
                p.getComponent(n + 20).setBackground(Color.GREEN);
                p.getComponent(n + 10).setBackground(Color.GREEN);
            }
            confirm(p, n, selectedItem);
            return true;
        }
        return false;

    }

    public boolean confirm(JPanel p, int n, Object selectedItem) {

        if (selectedItem.equals("U-Boot")) {
            deactivate(p);
            if (boundscheck(p, n + 1) && isGrey(p, n+1) && rightplacement(n, n + 1, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n + 1);
                p.getComponent(n + 1).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 1).setBackground(Color.GRAY);
                        p.getComponent(n + 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n - 1) && isGrey(p,n-1) && rightplacement(n, n -1, selectedItem)) {
                JButton b2 = (JButton) p.getComponent(n - 1);
                p.getComponent(n - 1).setEnabled(true);
                b2.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 1).setBackground(Color.GRAY);
                        p.getComponent(n - 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n + 10) && isGrey(p,n+10)) {
                JButton b3 = (JButton) p.getComponent(n + 10);
                if (boundscheck(p, n + 10)) {
                    p.getComponent(n + 10).setEnabled(true);
                }
                b3.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 10).setBackground(Color.GRAY);
                        p.getComponent(n + 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n - 10) && isGrey(p,n-10)) {
                JButton b4 = (JButton) p.getComponent(n - 10);
                p.getComponent(n - 10).setEnabled(true);
                b4.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 10).setBackground(Color.GRAY);
                        p.getComponent(n - 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }

            return true;
        }
        if (selectedItem.equals("Kreuzer")) {
            deactivate(p);
            if (boundscheck(p, n + 3) && isGrey(p,n+3) && rightplacement(n, n + 3, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n + 3);
                p.getComponent(n + 3).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 1).setBackground(Color.GRAY);
                        p.getComponent(n + 2).setBackground(Color.GRAY);
                        p.getComponent(n + 3).setBackground(Color.GRAY);

                        p.getComponent(n + 3).setEnabled(false);
                        p.getComponent(n + 2).setEnabled(false);
                        p.getComponent(n + 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n - 3) && isGrey(p,n-3) && rightplacement(n, n - 3, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n - 3);
                p.getComponent(n - 3).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 1).setBackground(Color.GRAY);
                        p.getComponent(n - 2).setBackground(Color.GRAY);
                        p.getComponent(n - 3).setBackground(Color.GRAY);

                        p.getComponent(n - 3).setEnabled(false);
                        p.getComponent(n - 2).setEnabled(false);
                        p.getComponent(n - 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }

            if (boundscheck(p, n + 30) && isGrey(p,n+ 30)) {
                JButton b1 = (JButton) p.getComponent(n + 30);
                p.getComponent(n + 30).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 10).setBackground(Color.GRAY);
                        p.getComponent(n + 20).setBackground(Color.GRAY);
                        p.getComponent(n + 30).setBackground(Color.GRAY);

                        p.getComponent(n + 30).setEnabled(false);
                        p.getComponent(n + 20).setEnabled(false);
                        p.getComponent(n + 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n - 30) && isGrey(p,n-30)) {
                JButton b1 = (JButton) p.getComponent(n - 30);
                p.getComponent(n - 30).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 10).setBackground(Color.GRAY);
                        p.getComponent(n - 20).setBackground(Color.GRAY);
                        p.getComponent(n - 30).setBackground(Color.GRAY);

                        p.getComponent(n - 30).setEnabled(false);
                        p.getComponent(n - 20).setEnabled(false);
                        p.getComponent(n - 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            return true;
        }
        if (selectedItem.equals("Schlachtschiff")){
            deactivate(p);
            if (boundscheck(p, n + 4) && isGrey(p,n+4) && rightplacement(n,n +4,selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n+4);
                p.getComponent(n+4).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 1).setBackground(Color.GRAY);
                        p.getComponent(n + 2).setBackground(Color.GRAY);
                        p.getComponent(n + 3).setBackground(Color.GRAY);
                        p.getComponent(n + 4).setBackground(Color.GRAY);

                        p.getComponent(n + 4).setEnabled(false);
                        p.getComponent(n + 3).setEnabled(false);
                        p.getComponent(n + 2).setEnabled(false);
                        p.getComponent(n + 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n-4) && isGrey(p,n-4) && rightplacement(n, n-4, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n-4);
                p.getComponent(n-4).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 1).setBackground(Color.GRAY);
                        p.getComponent(n - 2).setBackground(Color.GRAY);
                        p.getComponent(n - 3).setBackground(Color.GRAY);
                        p.getComponent(n - 4).setBackground(Color.GRAY);

                        p.getComponent(n - 4).setEnabled(false);
                        p.getComponent(n - 3).setEnabled(false);
                        p.getComponent(n - 2).setEnabled(false);
                        p.getComponent(n - 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }

            if (boundscheck(p, n +40) && isGrey(p,n+40)) {
                JButton b1 = (JButton) p.getComponent(n +40);
                p.getComponent(n +40).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 10).setBackground(Color.GRAY);
                        p.getComponent(n + 20).setBackground(Color.GRAY);
                        p.getComponent(n + 30).setBackground(Color.GRAY);
                        p.getComponent(n + 40).setBackground(Color.GRAY);

                        p.getComponent(n + 40).setEnabled(false);
                        p.getComponent(n + 30).setEnabled(false);
                        p.getComponent(n + 20).setEnabled(false);
                        p.getComponent(n + 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n-40) && isGrey(p,n-40)) {
                JButton b1 = (JButton) p.getComponent(n-40);
                p.getComponent(n-40).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 10).setBackground(Color.GRAY);
                        p.getComponent(n - 20).setBackground(Color.GRAY);
                        p.getComponent(n - 30).setBackground(Color.GRAY);
                        p.getComponent(n - 40).setBackground(Color.GRAY);

                        p.getComponent(n - 40).setEnabled(false);
                        p.getComponent(n - 30).setEnabled(false);
                        p.getComponent(n - 20).setEnabled(false);
                        p.getComponent(n - 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            return true;
        }
        if (selectedItem.equals("Zerstoerer")) {
            deactivate(p);
            if (boundscheck(p, n + 2) && isGrey(p, n + 2) && rightplacement(n, n + 2, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n + 2);
                p.getComponent(n + 2).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 1).setBackground(Color.GRAY);
                        p.getComponent(n + 2).setBackground(Color.GRAY);

                        p.getComponent(n + 2).setEnabled(false);
                        p.getComponent(n + 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n - 2) && isGrey(p, n - 2) && rightplacement(n, n - 2, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n - 2);
                p.getComponent(n - 2).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 1).setBackground(Color.GRAY);
                        p.getComponent(n - 2).setBackground(Color.GRAY);

                        p.getComponent(n - 2).setEnabled(false);
                        p.getComponent(n - 1).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }

            if (boundscheck(p, n + 20) && isGrey(p, n + 20)) {
                JButton b1 = (JButton) p.getComponent(n + 20);
                p.getComponent(n + 20).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n + 10).setBackground(Color.GRAY);
                        p.getComponent(n + 20).setBackground(Color.GRAY);

                        p.getComponent(n + 20).setEnabled(false);
                        p.getComponent(n + 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            if (boundscheck(p, n - 20) && isGrey(p, n - 20)) {
                JButton b1 = (JButton) p.getComponent(n - 20);
                p.getComponent(n - 20).setEnabled(true);
                b1.addActionListener(e -> {
                    for (int i = 0; i < p.getComponents().length; i++) {
                        if (p.getComponent(i).getBackground() == Color.GREEN) {
                            p.getComponent(i).setBackground(Color.BLUE);
                        }
                        p.getComponent(n).setBackground(Color.GRAY);
                        p.getComponent(n - 10).setBackground(Color.GRAY);
                        p.getComponent(n - 20).setBackground(Color.GRAY);

                        p.getComponent(n - 20).setEnabled(false);
                        p.getComponent(n - 10).setEnabled(false);
                        p.getComponent(n).setEnabled(false);
                        activate(p);
                    }
                });
            }
            return true;
        }
        return false;
    }
    public void deactivate(JPanel gameboard) {
        for (int i = 0; i < gameboard.getComponents().length; i++) {
            gameboard.getComponent(i).setEnabled(false);
        }
    }
    public void activate(JPanel gameboard) {
        for (int i = 0; i < gameboard.getComponents().length; i++) {
            if (gameboard.getComponent(i).getBackground() == Color.BLUE) {
                gameboard.getComponent(i).setEnabled(true);
            }
        }
    }
    public boolean boundscheck(JPanel gameboard, int n) {
        try {
            gameboard.getComponent(n);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    public boolean rightplacement(int n, int n1, Object selectedItem) {

        if (selectedItem.equals("U-Boot")) {
            if (n % 10 == 0 && ((n1 + 1) % 10 == 0)) {
                return false;
            }
            if ((n1 % 10 == 0 && ((n + 1) % 10 == 0))) {
                return false;
            }
        }
        if (selectedItem.equals("Kreuzer")){
            if (n % 10 == 0 && ((n1 + 3) % 10 == 0)) {
                return false;
            }
            if (((n1-2) % 10 == 0 && ((n + 1) % 10 == 0))) {
                return false;
            }

            if ((n-1) % 10 == 0 && ((n1 + 2) % 10 == 0)) {
                return false;
            }
            if (((n1-1) % 10 == 0 && ((n + 2) % 10 == 0))) {
                return false;
            }

            if ((n-2) % 10 == 0 && ((n1 + 1) % 10 == 0)) {
                return false;
            }
            if ((n1 % 10 == 0 && ((n + 3) % 10 == 0))) {
                return false;
            }
        }
        if (selectedItem.equals("Schlachtschiff")){
            if ((n+4) % 10 == 0 && (n1 % 10 == 0)) {
                return false;
            }
            if (((n1+4) % 10 == 0 && ((n) % 10 == 0))) {
                return false;
            }
            if (((n1-1) % 10 == 0 && ((n+3) % 10 == 0))) {
                return false;
            }
            if (((n1-3) % 10 == 0 && ((n + 1) % 10 == 0))) {
                return false;
            }
            if (((n1+1) % 10 == 0 && ((n-3) % 10 == 0))) {
                return false;
            }

            if ((n-1) % 10 == 0 && ((n1+3) % 10 == 0)) {
                return false;
            }
            if (((n1-2) % 10 == 0 && ((n + 2) % 10 == 0))) {
                return false;
            }
            if ((n-2) % 10 == 0 && ((n1+2) % 10 == 0)) {
                return false;
            }
        }
        if (selectedItem.equals("Zerstoerer")) {
            if ((n+2)% 10 == 0 && ((n1) % 10 == 0)) {
                return false;
            }
            if (((n1-1) % 10 == 0 && ((n+1) % 10 == 0))) {
                return false;
            }
            if (((n1+1) % 10 == 0 && ((n-1) % 10 == 0))) {
                return false;
            }
            if (((n1+2) % 10 == 0 && ((n) % 10 == 0))) {
                return false;
            }
        }
        return true;
    }
    public boolean isGrey(JPanel gameboard, int n){
      if (!(gameboard.getComponent(n).getBackground() == Color.GRAY)){
            return true;
        }
      return false;
    }
}


