package Sinking.http.test.tests.TestStore;

public class SetShipsTestStore {
    private static SetShipsTestStore instance;
    private String gameId;
    private String playerToken;

    public static SetShipsTestStore getInstance() {
        if (instance == null) {
            instance = new SetShipsTestStore();
        }
        return instance;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerToken() {
        return playerToken;
    }

    public void setPlayerToken(String playerToken) {
        this.playerToken = playerToken;
    }
}
