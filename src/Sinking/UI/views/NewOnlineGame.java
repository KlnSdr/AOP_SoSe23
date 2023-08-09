package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

import static Sinking.UI.Window.baseTitle;

public class NewOnlineGame implements IView {
    @Override
    public void load(JFrame window, Json data) {
        window.setTitle(baseTitle);
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        JLabel linkLabel = new JLabel("ServerURL:");
        GridBagConstraints gbcLinkLabel = new GridBagConstraints();
        gbcLinkLabel.gridx = 0;
        gbcLinkLabel.gridy = 0;
        gbcLinkLabel.anchor = GridBagConstraints.WEST;
        gbcLinkLabel.insets = new Insets(0, 0, 10, 10);
        centerContainer.add(linkLabel, gbcLinkLabel);

        JTextField linkField = new JTextField(20);
        GridBagConstraints gbcLinkField = new GridBagConstraints();
        gbcLinkField.gridx = 1;
        gbcLinkField.gridy = 0;
        gbcLinkField.fill = GridBagConstraints.HORIZONTAL;
        gbcLinkField.insets = new Insets(0, 0, 10, 0);
        centerContainer.add(linkField, gbcLinkField);

        JButton confirmButton = new JButton("Bestätigen");
        confirmButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcConfirmButton = new GridBagConstraints();
        gbcConfirmButton.gridx = 1;
        gbcConfirmButton.gridy = 2;
        gbcConfirmButton.anchor = GridBagConstraints.EAST;
        confirmButton.addActionListener(e -> {
            String url = linkField.getText();
            if (inputVerification(url)) {
                System.out.println("Server URL: " + url);
                System.out.println("Loading Game");
                Json args = new Json();
                args.set("gameUrl", url);
                ViewLoader.getInstance().loadView("WaitingScreen", args); //Link to WaitingScreen
            }
        });
        centerContainer.add(confirmButton, gbcConfirmButton);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcBackButton = new GridBagConstraints();
        gbcBackButton.gridx = 0;
        gbcBackButton.gridy = 2;
        gbcBackButton.anchor = GridBagConstraints.WEST;
        gbcBackButton.insets = new Insets(0, 0, 0, 10);
        backButton.addActionListener(e -> {
            System.out.println("Loading Main Menu");
            ViewLoader.getInstance().loadView("MainMenu");
        });
        centerContainer.add(backButton, gbcBackButton);
    }

    private boolean inputVerification(String url) {
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie eine Server-URL ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Ungültige Server-URL.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    protected void createNewOnlineGame(String url) {
        //code for joining the online game
    }

    @Override
    public void unload() {System.out.println("Unloading NewOnlineGame Screen");}
}
