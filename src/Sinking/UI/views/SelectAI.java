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

public class SelectAI implements IView {
    @Override
    public void load(JFrame window, Json data) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        window.add(container);

        JLabel msg = new JLabel("Wähle eine KI aus:");
        msg.setBounds(10, 10, 200, 20);
        container.add(msg);

        JComboBox<String> aiList = new JComboBox<>();
        aiList.setBounds(10, 40, 200, 20);
        AiLoader.getInstance().getAiNames().forEach(aiList::addItem);
        container.add(aiList);

        JButton start = new JButton("Start");
        start.setBounds(10, 70, 200, 20);
        start.addActionListener(e -> {
            String aiName = (String) aiList.getSelectedItem();
            System.out.println("Selected AI: " + aiName);
            initLocalServer();
            setupClientStore();
            createAndJoinLocalGame(aiName);
        });
        container.add(start);

        JButton back = new JButton("Zurück");
        back.setBounds(10, 100, 200, 20);
        back.addActionListener(e -> {
            ViewLoader.getInstance().loadView("MainMenu");
        });
        container.add(back);
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
