package Sinking.http.test.tests.TestStore;

public class GameReadyTestStore {
    private static GameReadyTestStore instance;
    private String gameId;

    private GameReadyTestStore() {
    }

    public static GameReadyTestStore getInstance() {
        if (instance == null) {
            instance = new GameReadyTestStore();
        }
        return instance;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }
}
