package Sinking.UI.views;

import Sinking.Game.Data.ClientStore;
import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;
import Sinking.http.client.Client;
import Sinking.http.client.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import static Sinking.UI.Window.baseTitle;

public class WaitingScreen implements IView {

    private String url;
    private int dotCount = 0;
    private JLabel waitLabel;
    private JFrame window;
    @Override
    public void load(JFrame window, Json data) {
        this.window = window;
        window.setTitle(baseTitle);
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        String url = getGameUrl(data);

        JLabel linkLabel = new JLabel(url);
        GridBagConstraints gbcLinkLabel = new GridBagConstraints();
        gbcLinkLabel.gridx = 0;
        gbcLinkLabel.gridy = 0;
        gbcLinkLabel.gridwidth = 2;
        gbcLinkLabel.anchor = GridBagConstraints.CENTER;
        gbcLinkLabel.insets = new Insets(0, 0, 15, 0);
        centerContainer.add(linkLabel, gbcLinkLabel);

        JButton copyButton = new JButton("Link kopieren");
        copyButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcCopyButton = new GridBagConstraints();
        gbcCopyButton.gridx = 0;
        gbcCopyButton.gridy = 1;
        gbcCopyButton.anchor = GridBagConstraints.CENTER;
        gbcCopyButton.insets = new Insets(0, 0, 30, 10);
        copyButton.addActionListener(e -> {
            System.out.println("Copy Link");
            copyURL(url);
        });
        centerContainer.add(copyButton, gbcCopyButton);

        waitLabel = new JLabel("Warten auf 2. Spieler");
        GridBagConstraints gbcWaitLabel = new GridBagConstraints();
        gbcWaitLabel.gridx = 0;
        gbcWaitLabel.gridy = 2;
        gbcWaitLabel.gridwidth = 2;
        gbcWaitLabel.anchor = GridBagConstraints.CENTER;
        gbcWaitLabel.insets = new Insets(0, 0, 15, 0);
        centerContainer.add(waitLabel, gbcWaitLabel);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcBackButton = new GridBagConstraints();
        gbcBackButton.gridx = 0;
        gbcBackButton.gridy = 3;
        gbcBackButton.anchor = GridBagConstraints.CENTER;
        gbcBackButton.insets = new Insets(0, 0, 15, 0);
        backButton.addActionListener(e -> {
            System.out.println("Loading Main Menu");
            ViewLoader.getInstance().loadView("MainMenu");
        });
        centerContainer.add(backButton, gbcBackButton);

        startWaitingForPlayers();

    }

    private void startWaitingForPlayers() {
        Timer timer = new Timer(1000, null);

        ActionListener taskPerformer = evt -> {
            ClientStore store = ClientStore.getInstance();

            String gameId = store.getGameId();
            Client client = store.getClient();

            Request request = client.newRequest("/gameReady");
            request.setQuery("id", gameId);
            client.get(request, response -> {
                int resCode = response.getStatusCode();
                if (resCode == 204) {
                    timer.stop();
                    ViewLoader.getInstance().loadView("PlacingShips");
                }
                dotCount = (dotCount + 1) % 4;
                waitLabel.setText(".".repeat(dotCount) + "Warten auf 2. Spieler" + ".".repeat(dotCount));
                window.repaint();
            });
        };
        timer.addActionListener(taskPerformer);

        timer.start();
    }

    //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
    private void copyURL(String url) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(url);
        clipboard.setContents(selection, null);

        JDialog dialog = new JDialog((Frame) null, "", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);

        JLabel label = new JLabel("Link erfolgreich kopiert.");
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        gbcLabel.gridheight = GridBagConstraints.REMAINDER;
        gbcLabel.gridwidth = GridBagConstraints.REMAINDER;
        gbcLabel.anchor = GridBagConstraints.CENTER;
        dialog.add(label, gbcLabel);

        ActionListener timerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        };
        Timer timer = new Timer(2000, timerAction);
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
        System.out.println("Text wurde in den Zwischenspeicher kopiert.");
    }
    private String getGameUrl(Json data) {
        Optional<String> url = data.get("gameUrl");
        return url.orElse("https://placeholder.de/join?id={gameId}");
    }

    @Override
    public void unload() {System.out.println("Unloading WaitingScreen");}
}
