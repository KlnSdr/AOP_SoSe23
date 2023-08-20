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
import java.net.MalformedURLException;
import java.net.URL;

import static Sinking.UI.Window.baseTitle;

public class JoinOnlineGame implements IView {
    protected String linkLabelText = "Einladungslink:";

    @Override
    public void load(JFrame window, Json data) {
        window.setTitle(baseTitle);
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        JLabel linkLabel = new JLabel(this.linkLabelText);
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
        confirmButton.setPreferredSize(new Dimension(250, 20));
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
                startJoinSequence(url, nickname);
            }
        });
        centerContainer.add(confirmButton, gbcConfirmButton);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.setPreferredSize(new Dimension(250, 20));
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

    private void startJoinSequence(String url, String nickname) {
        setUpClientStore(url, nickname);
        onBeforeJoin();
    }

    private void setUpClientStore(String url, String nickname) {
        ClientStore clientStore = ClientStore.getInstance();
        Tupel<String, String> gameUrlAndId = splitJoinUrl(url);
        clientStore.setServerUrl(gameUrlAndId._1());
        clientStore.setGameId(gameUrlAndId._2());
        clientStore.setNickname(nickname);

        Client httpClient = new Client(gameUrlAndId._1(), 5000);
        clientStore.setClient(httpClient);
    }

    /**
     * Override to execute custom code before joining the game.
     * This method is called after the client store has been set up.
     * To join the game call {@link #joinOnlineGame()} at the end.
     */
    protected void onBeforeJoin() {
        joinOnlineGame();
    }

    protected void joinOnlineGame() {
        ClientStore store = ClientStore.getInstance();

        if (store.getGameId() == null) {
            System.out.println("Failed to join game");
            ViewLoader.getInstance().loadView("MainMenu");
            return;
        }

        Request joinRequest = store.getClient().newRequest("/join");
        joinRequest.setQuery("id", store.getGameId());
        joinRequest.setBody("nickname", store.getNickname());

        store.getClient().post(joinRequest, response -> {
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            if (response.getStatusCode() == 200) {
                System.out.println("Joined game");
                store.setPlayerToken(response.getBody().get("playerToken").orElse(""));
                Json payload = new Json();
                payload.set("gameUrl", store.getServerUrl() + "/join?id=" + store.getGameId());
                ViewLoader.getInstance().loadView("WaitingScreen", payload);
            } else {
                System.out.println("Failed to join game");
                ViewLoader.getInstance().loadView("MainMenu");
            }
        }, error -> {
            System.out.println("Failed to join game");
            ViewLoader.getInstance().loadView("MainMenu");
        });
    }

    private Tupel<String, String> splitJoinUrl(String url) {
        String[] splitUrl = url.split("\\?");

        if (splitUrl.length < 1) {
            System.out.println("Invalid join URL");
            return new Tupel<>("", "");
        }

        String serverUrl = splitUrl[0];
        String[] splitServerUrl = serverUrl.split("/");
        if (splitServerUrl.length < 3) {
            System.out.println("Invalid join URL");
            return new Tupel<>("", "");
        }
        serverUrl = splitServerUrl[0] + "//" + splitServerUrl[2];

        if (splitUrl.length < 2) {
            return new Tupel<>(serverUrl, null);
        }

        String query = splitUrl[1];

        String[] queryParts = query.split("&");
        String gameId = findIdInQuery(queryParts);

        if (gameId == null) {
            System.out.println("Invalid join URL");
            return new Tupel<>(serverUrl, null);
        }

        System.out.println("Server URL: " + serverUrl);
        System.out.println("Game ID: " + gameId);
        return new Tupel<>(serverUrl, gameId);
    }

    private String findIdInQuery(String[] query) {
        for (String queryPart : query) {
            String[] queryPartSplit = queryPart.split("=");
            if (queryPartSplit.length != 2) {
                continue;
            }
            if (queryPartSplit[0].equals("id")) {
                return queryPartSplit[1];
            }
        }
        return null;
    }

    @Override
    public void unload() {
        System.out.println("Unloading JoinOnlineGame Screen");
    }
}
