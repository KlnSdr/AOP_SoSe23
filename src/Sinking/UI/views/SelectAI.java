package Sinking.UI.views;

import Sinking.AiLoader;
import Sinking.Game.Data.ClientStore;
import Sinking.Game.Data.PlayerAiWrapper;
import Sinking.Main;
import Sinking.UI.IView;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;
import Sinking.http.client.Client;
import Sinking.http.client.Request;

import javax.swing.*;
import java.awt.*;

public class SelectAI implements IView {
    @Override
    public void load(JFrame window, Json data) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(container);

        JLabel aiSelectLabel = new JLabel("Wähle eine KI aus:");
        GridBagConstraints gbcAiSelectLabel = new GridBagConstraints();
        gbcAiSelectLabel.gridx = 0;
        gbcAiSelectLabel.gridy = 0;
        gbcAiSelectLabel.anchor = GridBagConstraints.CENTER;
        gbcAiSelectLabel.insets = new Insets(10, 10, 10, 10);
        container.add(aiSelectLabel, gbcAiSelectLabel);

        JComboBox<String> aiListComboBox = new JComboBox<>();
        AiLoader.getInstance().getAiNames().forEach(aiListComboBox::addItem);
        aiListComboBox.setPreferredSize(new Dimension(250, 20));
        GridBagConstraints gbcAiListComboBox = new GridBagConstraints();
        gbcAiListComboBox.gridx = 0;
        gbcAiListComboBox.gridy = 1;
        gbcAiListComboBox.anchor = GridBagConstraints.CENTER;
        gbcAiListComboBox.insets = new Insets(10, 10, 10, 10);
        container.add(aiListComboBox, gbcAiListComboBox);

        JButton startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(250, 20));
        GridBagConstraints gbcStartButton = new GridBagConstraints();
        gbcStartButton.gridx = 0;
        gbcStartButton.gridy = 2;
        gbcStartButton.anchor = GridBagConstraints.CENTER;
        gbcStartButton.insets = new Insets(10, 10, 10, 10);
        startButton.addActionListener(e -> {
            String aiName = (String) aiListComboBox.getSelectedItem();
            System.out.println("Selected AI: " + aiName);
            initLocalServer();
            setupClientStore();
            createAndJoinLocalGame(aiName);
        });
        container.add(startButton, gbcStartButton);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.setPreferredSize(new Dimension(250, 20));
        GridBagConstraints gbcBackButton = new GridBagConstraints();
        gbcBackButton.gridx = 0;
        gbcBackButton.gridy = 3;
        gbcBackButton.anchor = GridBagConstraints.CENTER;
        gbcBackButton.insets = new Insets(10, 10, 10, 10);
        backButton.addActionListener(e -> {
            System.out.println("Loading Main Menu");
            ViewLoader.getInstance().loadView("MainMenu");
        });
        container.add(backButton, gbcBackButton);
    }

    private void initLocalServer() {
        Json args = new Json();
        args.set("port", "13000");
        Main.startInServerMode(args);
    }

    private void setupClientStore() {
        ClientStore store = ClientStore.getInstance();
        store.setServerUrl("http://localhost:13000");
        store.setNickname("Du");

        Client httpClient = new Client(store.getServerUrl(), 5000);
        store.setClient(httpClient);
    }

    private void createAndJoinLocalGame(String aiName) {
        ClientStore store = ClientStore.getInstance();
        Client client = store.getClient();

        Request req = client.newRequest("/newGame");
        client.get(req, response -> {
            Json body = response.getBody();
            if (body.hasKey("gameUUID")) {
                store.setGameId(body.get("gameUUID").orElse(""));
                System.out.println("Created game with id " + store.getGameId());
                joinLocalGame(true, aiName);
            } else {
                System.out.println("Failed to create game");
                ViewLoader.getInstance().loadView("MainMenu");
            }
        }, error -> {
            System.out.println("Failed to create game");
            ViewLoader.getInstance().loadView("MainMenu");
        });
    }

    private void joinLocalGame(boolean isAi, String nickname) {
        ClientStore store = ClientStore.getInstance();
        Client client = store.getClient();

        Request req = client.newRequest("/join");
        req.setQuery("id", store.getGameId());
        req.setBody("nickname", nickname);

        client.post(req, response -> {
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            if (response.getStatusCode() == 200) {
                if (isAi) {
                    PlayerAiWrapper ai = new PlayerAiWrapper(nickname);
                    ai.startParallelRunner(store.getGameId(), response.getBody().get("playerToken").orElse(""));
                    joinLocalGame(false, "Du");
                } else {
                    store.setPlayerToken(response.getBody().get("playerToken").orElse(""));
                    ViewLoader.getInstance().loadView("PlacingShips");
                }
            } else {
                System.out.println("Failed to join game");
                ViewLoader.getInstance().loadView("MainMenu");
            }
        }, error -> {
            System.out.println("Failed to join game");
            ViewLoader.getInstance().loadView("MainMenu");
        });
    }

    @Override
    public void unload() {

    }
}
