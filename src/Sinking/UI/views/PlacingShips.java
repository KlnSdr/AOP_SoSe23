package Sinking.UI.views;

import Sinking.Game.Data.ClientStore;
import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;
import Sinking.common.Tupel;
import Sinking.http.client.Client;
import Sinking.http.client.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlacingShips implements IView {
    static int state = 0;
    private JButton[] buttons;
    private JPanel gameBoardPanel;
    private final ArrayList<String> shipList = new ArrayList<>(List.of(new String[]{"Schiffe", "Schlachtschiff", "Kreuzer", "Zerstoerer", "U-Boot"}));
    private final int[] shipAmounts = new int[]{0, 0, 0, 0};
    private final int[] shipMaxAmounts = new int[]{1, 2, 3, 4};
    private final JLabel[] shipLabels = new JLabel[4];

    public void load(JFrame window, Json data) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(container);

        //Übrige Schiffe anzeigen
        JPanel leftContainer = new JPanel();
        leftContainer.setLayout(new GridBagLayout());
        leftContainer.setBackground(Color.WHITE);
        leftContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcLeftContainer = new GridBagConstraints();
        gbcLeftContainer.gridx = 0;
        gbcLeftContainer.gridy = 0;
        gbcLeftContainer.insets = new Insets(10, 10, 10, 10);
        gbcLeftContainer.fill = GridBagConstraints.HORIZONTAL;
        container.add(leftContainer, gbcLeftContainer);

        for (int i = 1; i < 5; i++) {
            JLabel ship = new JLabel(shipList.get(i) + ": 0/" + shipMaxAmounts[i - 1]);
            GridBagConstraints gbcShipLabel = new GridBagConstraints();
            gbcShipLabel.gridx = 0;
            gbcShipLabel.gridy = i - 1;
            gbcShipLabel.fill = GridBagConstraints.CENTER;
            leftContainer.add(ship, gbcShipLabel);
            shipLabels[i - 1] = ship;
        }

        //Schiffe platzieren
        JPanel rightContainer = new JPanel();
        rightContainer.setLayout(new GridBagLayout());
        rightContainer.setBackground(Color.WHITE);
        rightContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcUpperContainer = new GridBagConstraints();
        gbcUpperContainer.gridx = 1;
        gbcUpperContainer.gridy = 0;
        gbcUpperContainer.weightx = 1.0;
        gbcUpperContainer.fill = GridBagConstraints.HORIZONTAL;
        container.add(rightContainer, gbcUpperContainer);

        JComboBox<String> shipComboBox = new JComboBox<>(shipList.toArray(new String[0]));
        shipComboBox.setPreferredSize(new Dimension(100, 20));
        GridBagConstraints gbcShipsComboBox = new GridBagConstraints();
        gbcShipsComboBox.gridx = 0;
        gbcShipsComboBox.gridy = 0;
        gbcShipsComboBox.anchor = GridBagConstraints.CENTER;
        gbcShipsComboBox.insets = new Insets(10, 10, 10, 10);
        rightContainer.add(shipComboBox, gbcShipsComboBox);

        gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcGameBoardPanel = new GridBagConstraints();
        gbcGameBoardPanel.gridx = 1;
        gbcGameBoardPanel.gridy = 0;
        gameBoardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
                    System.out.println(n);
                    System.out.println("Player clicked on " + finalRow + " " + finalCol);
                    if (state % 2 == 0) {
                        placingship(gameBoardPanel, n, shipComboBox.getSelectedItem());
                        checkPlacedShips(shipComboBox.getSelectedItem());
                        state++;
                    } else {
                        for (int i = 0; i < buttons.length; i++) {
                            if (buttons[i].getActionListeners().length == 2) {
                                ActionListener[] actions = buttons[i].getActionListeners();
                                ActionListener a = actions[0];
                                buttons[i].removeActionListener(a);
                            }
                        }
                        adjustJbox(shipComboBox);
                        updateAvailableShips();
                        state++;
                    }
                });

                gameBoardPanel.add(button, gbcButton);

            }
            gbcGameBoardPanel.insets = new Insets(0, 0, 0, 10);
        }
        for (int i = 0; i < gameBoardPanel.getComponents().length; i++) {
            gameBoardPanel.getComponent(i).setBackground(Color.BLUE);
        }
        rightContainer.add(gameBoardPanel);


        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcBackButton = new GridBagConstraints();
        gbcBackButton.gridx = 2;
        gbcBackButton.gridy = 0;
        gbcBackButton.insets = new Insets(10, 10, 10, 10);
        backButton.addActionListener(e -> {
            ViewLoader.getInstance().loadView("MainMenu");
        });
        rightContainer.add(backButton, gbcBackButton);

    }

    private void checkPlacedShips(Object selectedItem) {
        if (selectedItem.equals("U-Boot")) {
            shipAmounts[3]++;
        }
        if (selectedItem.equals("Kreuzer")) {
            shipAmounts[1]++;
        }
        if (selectedItem.equals("Schlachtschiff")) {
            shipAmounts[0]++;
        }
        if (selectedItem.equals("Zerstoerer")) {
            shipAmounts[2]++;
        }
    }

    private void adjustJbox(JComboBox ships) {
        Object selectedItem = ships.getSelectedItem();
        if (shipAmounts[3] == 4) {
            ships.removeItem(selectedItem);
            shipAmounts[3]++;
        }
        if (shipAmounts[2] == 3) {
            ships.removeItem(selectedItem);
            shipAmounts[2]++;
        }
        if (shipAmounts[1] == 2) {
            ships.removeItem(selectedItem);
            shipAmounts[1]++;
        }
        if (shipAmounts[0] == 1) {
            ships.removeItem(selectedItem);
            shipAmounts[0]++;
        }
    }

    private void updateAvailableShips() {
        boolean allShipsPlaced = true;
        for (int i = 0; i < shipLabels.length; i++) {
            JLabel lbl = shipLabels[i];
            lbl.setText(shipList.get(i + 1) + ": " + Integer.min(shipAmounts[i], shipMaxAmounts[i]) + "/" + shipMaxAmounts[i]);
            if (shipAmounts[i] < shipMaxAmounts[i]) {
                allShipsPlaced = false;
            }
        }

        if (allShipsPlaced) {
            ViewLoader.getInstance().loadView("MainScreen");
        }
    }

    public boolean placingship(JPanel p, int n, Object selectedItem) {
        if (selectedItem == null) {
            return false;
        }
        if (selectedItem.equals("Schiffe")) {
            state++;
        }
        boolean status = false;
        if ((preventSoftlock(selectedItem, n, p))) {
            if (selectedItem.equals("U-Boot")) {
                if (isNotGrey(n)) {
                    p.getComponent(n).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n + 1) && rightplacement(n, n + 1, selectedItem) && isNotGrey(n + 1)) {
                    p.getComponent(n + 1).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 10) && isNotGrey(n - 10)) {
                    p.getComponent(n - 10).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n + 10) && isNotGrey(n + 10)) {
                    p.getComponent(n + 10).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 1) && rightplacement(n, n - 1, selectedItem) && isNotGrey(n - 1)) {
                    p.getComponent(n - 1).setBackground(Color.GREEN);
                }
                confirm(p, n, selectedItem);
                return true;
            }
            if (selectedItem.equals("Kreuzer")) {

                if (!(p.getComponent(n).getBackground() == Color.GRAY)) {
                    p.getComponent(n).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n + 3) && rightplacement(n, n + 3, selectedItem) && isNotGrey(n + 3) && isNotGrey(n + 2) && isNotGrey(n + 1)) {
                    p.getComponent(n + 3).setBackground(Color.GREEN);
                    p.getComponent(n + 2).setBackground(Color.GREEN);
                    p.getComponent(n + 1).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 3) && rightplacement(n, n - 3, selectedItem) && isNotGrey(n - 3) && isNotGrey(n - 2) && isNotGrey(n - 1)) {
                    p.getComponent(n - 3).setBackground(Color.GREEN);
                    p.getComponent(n - 2).setBackground(Color.GREEN);
                    p.getComponent(n - 1).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 30) && isNotGrey(n - 30) && isNotGrey(n - 20) && isNotGrey(n - 10)) {
                    p.getComponent(n - 30).setBackground(Color.GREEN);
                    p.getComponent(n - 20).setBackground(Color.GREEN);
                    p.getComponent(n - 10).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n + 30) && isNotGrey(n + 30) && isNotGrey(n + 20) && isNotGrey(n + 10)) {
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
                if (boundscheck(p, n + 4) && rightplacement(n, n + 4, selectedItem) && isNotGrey(n + 4) && isNotGrey(n + 3) && isNotGrey(n + 2) && isNotGrey(n + 1)) {
                    p.getComponent(n + 4).setBackground(Color.GREEN);
                    p.getComponent(n + 3).setBackground(Color.GREEN);
                    p.getComponent(n + 2).setBackground(Color.GREEN);
                    p.getComponent(n + 1).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 4) && rightplacement(n, n - 4, selectedItem) && isNotGrey(n - 4) && isNotGrey(n - 3) && isNotGrey(n - 2) && isNotGrey(n - 1)) {
                    p.getComponent(n - 4).setBackground(Color.GREEN);
                    p.getComponent(n - 3).setBackground(Color.GREEN);
                    p.getComponent(n - 2).setBackground(Color.GREEN);
                    p.getComponent(n - 1).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 40) && isNotGrey(n - 40) && isNotGrey(n - 30) && isNotGrey(n - 20) && isNotGrey(n - 10)) {
                    p.getComponent(n - 40).setBackground(Color.GREEN);
                    p.getComponent(n - 30).setBackground(Color.GREEN);
                    p.getComponent(n - 20).setBackground(Color.GREEN);
                    p.getComponent(n - 10).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n + 40) && isNotGrey(n + 40) && isNotGrey(n + 30) && isNotGrey(n + 20) && isNotGrey(n + 10)) {
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
                if (boundscheck(p, n + 2) && rightplacement(n, n + 2, selectedItem) && isNotGrey(n + 2) && isNotGrey(n + 1)) {
                    p.getComponent(n + 2).setBackground(Color.GREEN);
                    p.getComponent(n + 1).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 2) && rightplacement(n, n - 2, selectedItem) && isNotGrey(n - 2) && isNotGrey(n - 1)) {
                    p.getComponent(n - 2).setBackground(Color.GREEN);
                    p.getComponent(n - 1).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n - 20) && isNotGrey(n - 20) && isNotGrey(n - 10)) {
                    p.getComponent(n - 20).setBackground(Color.GREEN);
                    p.getComponent(n - 10).setBackground(Color.GREEN);
                }
                if (boundscheck(p, n + 20) && isNotGrey(n + 20) && isNotGrey(n + 10)) {
                    p.getComponent(n + 20).setBackground(Color.GREEN);
                    p.getComponent(n + 10).setBackground(Color.GREEN);
                }
                confirm(p, n, selectedItem);
                return true;
            }
        } else {
            shipAmounts[shipList.indexOf(selectedItem) - 1]--;
            state++;
        }
        return false;

    }

    public boolean confirm(JPanel p, int n, Object selectedItem) {

        if (selectedItem.equals("U-Boot")) {
            deactivate(p);
            if (boundscheck(p, n + 1) && isNotGrey(n + 1) && rightplacement(n, n + 1, selectedItem)) {
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
            if (boundscheck(p, n - 1) && isNotGrey(n - 1) && rightplacement(n, n - 1, selectedItem)) {
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
            if (boundscheck(p, n + 10) && isNotGrey(n + 10)) {
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
            if (boundscheck(p, n - 10) && isNotGrey(n - 10)) {
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
            if (boundscheck(p, n + 3) && isNotGrey(n + 3) && rightplacement(n, n + 3, selectedItem)) {
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
            if (boundscheck(p, n - 3) && isNotGrey(n - 3) && rightplacement(n, n - 3, selectedItem)) {
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

            if (boundscheck(p, n + 30) && isNotGrey(n + 30)) {
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
            if (boundscheck(p, n - 30) && isNotGrey(n - 30)) {
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
        if (selectedItem.equals("Schlachtschiff")) {
            deactivate(p);
            if (boundscheck(p, n + 4) && isNotGrey(n + 4) && rightplacement(n, n + 4, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n + 4);
                p.getComponent(n + 4).setEnabled(true);
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
            if (boundscheck(p, n - 4) && isNotGrey(n - 4) && rightplacement(n, n - 4, selectedItem)) {
                JButton b1 = (JButton) p.getComponent(n - 4);
                p.getComponent(n - 4).setEnabled(true);
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

            if (boundscheck(p, n + 40) && isNotGrey(n + 40)) {
                JButton b1 = (JButton) p.getComponent(n + 40);
                p.getComponent(n + 40).setEnabled(true);
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
            if (boundscheck(p, n - 40) && isNotGrey(n - 40)) {
                JButton b1 = (JButton) p.getComponent(n - 40);
                p.getComponent(n - 40).setEnabled(true);
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
            if (boundscheck(p, n + 2) && isNotGrey(n + 2) && rightplacement(n, n + 2, selectedItem)) {
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
            if (boundscheck(p, n - 2) && isNotGrey(n - 2) && rightplacement(n, n - 2, selectedItem)) {
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

            if (boundscheck(p, n + 20) && isNotGrey(n + 20)) {
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
            if (boundscheck(p, n - 20) && isNotGrey(n - 20)) {
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
        if (selectedItem.equals("Kreuzer")) {
            if (n % 10 == 0 && ((n1 + 3) % 10 == 0)) {
                return false;
            }
            if (((n1 - 2) % 10 == 0 && ((n + 1) % 10 == 0))) {
                return false;
            }

            if ((n - 1) % 10 == 0 && ((n1 + 2) % 10 == 0)) {
                return false;
            }
            if (((n1 - 1) % 10 == 0 && ((n + 2) % 10 == 0))) {
                return false;
            }

            if ((n - 2) % 10 == 0 && ((n1 + 1) % 10 == 0)) {
                return false;
            }
            if ((n1 % 10 == 0 && ((n + 3) % 10 == 0))) {
                return false;
            }
        }
        if (selectedItem.equals("Schlachtschiff")) {
            if ((n + 4) % 10 == 0 && (n1 % 10 == 0)) {
                return false;
            }
            if (((n1 + 4) % 10 == 0 && ((n) % 10 == 0))) {
                return false;
            }
            if (((n1 - 1) % 10 == 0 && ((n + 3) % 10 == 0))) {
                return false;
            }
            if (((n1 - 3) % 10 == 0 && ((n + 1) % 10 == 0))) {
                return false;
            }
            if (((n1 + 1) % 10 == 0 && ((n - 3) % 10 == 0))) {
                return false;
            }

            if ((n - 1) % 10 == 0 && ((n1 + 3) % 10 == 0)) {
                return false;
            }
            if (((n1 - 2) % 10 == 0 && ((n + 2) % 10 == 0))) {
                return false;
            }
            if ((n - 2) % 10 == 0 && ((n1 + 2) % 10 == 0)) {
                return false;
            }
        }
        if (selectedItem.equals("Zerstoerer")) {
            if ((n + 2) % 10 == 0 && ((n1) % 10 == 0)) {
                return false;
            }
            if (((n1 - 1) % 10 == 0 && ((n + 1) % 10 == 0))) {
                return false;
            }
            if (((n1 + 1) % 10 == 0 && ((n - 1) % 10 == 0))) {
                return false;
            }
            return (n1 + 2) % 10 != 0 || ((n) % 10 != 0);
        }
        return true;
    }

    public boolean preventSoftlock(Object selectedItem, int n, JPanel gameboard) {
        if (selectedItem.equals("U-Boot")) {
            if (boundscheck(gameboard, n + 1) && rightplacement(n, n + 1, selectedItem) && isNotGrey(n + 1)) {
                return true;
            }
            if (boundscheck(gameboard, n - 10) && isNotGrey(n - 10)) {
                return true;
            }
            if (boundscheck(gameboard, n + 10) && isNotGrey(n + 10)) {
                return true;
            }
            if (boundscheck(gameboard, n - 1) && rightplacement(n, n - 1, selectedItem) && isNotGrey(n - 1)) {
                return true;
            }

        }
        if (selectedItem.equals("Kreuzer")) {
            if (boundscheck(gameboard, n + 3) && rightplacement(n, n + 3, selectedItem) && isNotGrey(n + 3) && isNotGrey(n + 2) && isNotGrey(n + 1)) {
                return true;
            }
            if (boundscheck(gameboard, n - 3) && rightplacement(n, n - 3, selectedItem) && isNotGrey(n - 3) && isNotGrey(n - 2) && isNotGrey(n - 1)) {
                return true;
            }
            if (boundscheck(gameboard, n - 30) && isNotGrey(n - 30) && isNotGrey(n - 20) && isNotGrey(n - 10)) {
                return true;
            }
            if (boundscheck(gameboard, n + 30) && isNotGrey(n + 30) && isNotGrey(n + 20) && isNotGrey(n + 10)) {
                return true;
            }

        }
        if (selectedItem.equals("Schlachtschiff")) {
            if (boundscheck(gameboard, n + 4) && rightplacement(n, n + 4, selectedItem) && isNotGrey(n + 4) && isNotGrey(n + 3) && isNotGrey(n + 2) && isNotGrey(n + 1)) {
                return true;
            }
            if (boundscheck(gameboard, n - 4) && rightplacement(n, n - 4, selectedItem) && isNotGrey(n - 4) && isNotGrey(n - 3) && isNotGrey(n - 2) && isNotGrey(n - 1)) {
                return true;
            }
            if (boundscheck(gameboard, n - 40) && isNotGrey(n - 40) && isNotGrey(n - 30) && isNotGrey(n - 20) && isNotGrey(n - 10)) {
                return true;
            }
            if (boundscheck(gameboard, n + 40) && isNotGrey(n + 40) && isNotGrey(n + 30) && isNotGrey(n + 20) && isNotGrey(n + 10)) {
                return true;
            }

        }
        if (selectedItem.equals("Zerstoerer")) {
            if (boundscheck(gameboard, n + 2) && rightplacement(n, n + 2, selectedItem) && isNotGrey(n + 2) && isNotGrey(n + 1)) {
                return true;
            }
            if (boundscheck(gameboard, n - 2) && rightplacement(n, n - 2, selectedItem) && isNotGrey(n - 2) && isNotGrey(n - 1)) {
                return true;
            }
            if (boundscheck(gameboard, n - 20) && isNotGrey(n - 20) && isNotGrey(n - 10)) {
                return true;
            }
            return boundscheck(gameboard, n + 20) && isNotGrey(n + 20) && isNotGrey(n + 10);
        }
        return false;

    }

    public boolean isNotGrey(int n) {
        return !(gameBoardPanel.getComponent(n).getBackground() == Color.GRAY);
    }

    @Override
    public void unload() {
        ArrayList<Tupel<Integer, Integer>> shipCoords = new ArrayList<>();
        for (int i = 0; i < gameBoardPanel.getComponents().length; i++) {
            if (!isNotGrey(i)) {
                int y = i % 10;
                int x = (i - y) / 10;
                shipCoords.add(new Tupel<>(x, y));
            }
        }
        Tupel<Integer, Integer>[] ships = new Tupel[shipCoords.size()];
        for (int i = 0; i < shipCoords.size(); i++) {
            ships[i] = shipCoords.get(i);
        }

        String s = makeItString(ships);
        ClientStore clientstore = ClientStore.getInstance();
        clientstore.setShips(ships);
        Client client = clientstore.getClient();

        Request request = client.newRequest("/setShips");
        request.setQuery("id", (clientstore.getGameId()));
        request.setBody("playerToken", clientstore.getPlayerToken());
        request.setBody("ships", s);

        client.post(request, response -> {
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
        });
    }

    public String makeItString(Tupel<Integer, Integer>[] ships) {
        String s;
        String s1 = "";

        for (int i = 0; i < ships.length; i++) {
            s = String.valueOf(ships[i]._1());
            s1 = s1 + s;
            s1 = s1 + ",";
            s = String.valueOf(ships[i]._2());
            s1 = s1 + s;
            s1 = s1 + "|";
        }
        return s1;
    }
}
