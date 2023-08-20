package Sinking.http.test.tests.TestStore;

public class FireAtTestStore {
    private static FireAtTestStore instance;
    private String gameId;
    private String playerToken;

    private FireAtTestStore() {
    }

    public static FireAtTestStore getInstance() {
        if (instance == null) {
            instance = new FireAtTestStore();
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
