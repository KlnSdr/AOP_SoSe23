package Sinking.http.test.tests.TestStore;

public class JoinGameTestStore {
    private static JoinGameTestStore instance;
    private String gameId;

    private JoinGameTestStore() {

    }

    public static JoinGameTestStore getInstance() {
        if (instance == null) {
            instance = new JoinGameTestStore();
        }
        return instance;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
