package Sinking.UI.views;

import Sinking.Game.Data.ClientStore;
import Sinking.UI.ViewLoader;
import Sinking.common.Json;
import Sinking.http.client.Client;
import Sinking.http.client.Request;

public class NewOnlineGame extends JoinOnlineGame {
    public NewOnlineGame() {
        this.linkLabelText = "ServerURL:";
    }

    @Override
    protected void onBeforeJoin() {
        ClientStore store = ClientStore.getInstance();
        Client client = store.getClient();

        Request reqNewGame = client.newRequest("/newGame");
        client.get(reqNewGame, (response) -> {
            Json body = response.getBody();
            if (body.hasKey("gameUUID")) {
                store.setGameId(body.get("gameUUID").orElse(""));
                System.out.println("Created game with id " + store.getGameId());
                joinOnlineGame();
            } else {
                System.out.println("Failed to create game");
                ViewLoader.getInstance().loadView("MainMenu");
            }
        }, (error) -> {
            System.out.println("Failed to create game");
            ViewLoader.getInstance().loadView("MainMenu");
        });
    }
}
