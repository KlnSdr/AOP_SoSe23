package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

import static Sinking.UI.Window.baseTitle;

public class JoinOnlineGame implements IView {
    @Override
    public void load(JFrame window) {
        window.setTitle(baseTitle);
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        JLabel linkLabel = new JLabel("ServerURL/Einladungslink:");
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

        JLabel nicknameLabel = new JLabel("Nickname:");
        GridBagConstraints gbcNicknameLabel = new GridBagConstraints();
        gbcNicknameLabel.gridx = 0;
        gbcNicknameLabel.gridy = 1;
        gbcNicknameLabel.anchor = GridBagConstraints.WEST;
        gbcNicknameLabel.insets = new Insets(0, 0, 10, 10);
        centerContainer.add(nicknameLabel, gbcNicknameLabel);

        JTextField nicknameField = new JTextField(20);
        GridBagConstraints gbcNicknameField = new GridBagConstraints();
        gbcNicknameField.gridx = 1;
        gbcNicknameField.gridy = 1;
        gbcNicknameField.fill = GridBagConstraints.HORIZONTAL;
        gbcNicknameField.insets = new Insets(0, 0, 10, 0);
        centerContainer.add(nicknameField, gbcNicknameField);

        JButton confirmButton = new JButton("Bestätigen");
        confirmButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcConfirmButton = new GridBagConstraints();
        gbcConfirmButton.gridx = 1;
        gbcConfirmButton.gridy = 2;
        gbcConfirmButton.anchor = GridBagConstraints.EAST;
        confirmButton.addActionListener(e -> {
            String url = linkField.getText();
            String nickname = nicknameField.getText().trim();
            if (inputVerification(url, nickname)) {
                System.out.println("Server URL: " + url);
                System.out.println("Nickname: " + nickname);
                System.out.println("Loading Game");
                ViewLoader.getInstance().loadView("DevScreen"); //Link to WaitingScreen
                joinOnlineGame(url, nickname);
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

    private boolean inputVerification(String url, String nickname) {
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
        if (nickname.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie einen Nickname ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    protected void joinOnlineGame(String url, String nickname) {
        //code for joining the online game
    }

    @Override
    public void unload() {System.out.println("Unloading JoinOnlineGame Screen");}
}
