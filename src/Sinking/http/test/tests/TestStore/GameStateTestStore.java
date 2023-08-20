package Sinking.http.test.tests.TestStore;

public class GameStateTestStore {
    private static GameStateTestStore instance;
    private String gameId;
    private String player1Token;

    private GameStateTestStore() {
    }

    public static GameStateTestStore getInstance() {
        if (instance == null) {
            instance = new GameStateTestStore();
        }
        return instance;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setPlayerToken(String player1Id) {
        this.player1Token = player1Id;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerToken() {
        return player1Token;
    }
}
