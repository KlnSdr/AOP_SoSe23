package Sinking.Game.Data;

import Sinking.common.Tupel;
import Sinking.http.client.Client;

public class ClientStore {
    private static ClientStore instance;
    private String serverUrl;
    private String gameId;
    private String nickname;
    private String playerToken;
    private Client client;
    private Tupel<Integer, Integer>[] ships;

    private ClientStore() {
    }

    public static ClientStore getInstance() {
        if (instance == null) {
            instance = new ClientStore();
        }
        return instance;
    }

    public Tupel<Integer, Integer>[] getShips() {
        return ships;
    }

    public void setShips(Tupel<Integer, Integer>[] ships) {
        this.ships = ships;
    }

    public String getPlayerToken() {
        return playerToken;
    }

    public void setPlayerToken(String playerToken) {
        this.playerToken = playerToken;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
