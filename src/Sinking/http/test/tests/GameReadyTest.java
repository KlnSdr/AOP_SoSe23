package Sinking.http.test.tests;

import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.GameReadyTestStore;

public class GameReadyTest {
    @Test(name = "(PRE) create game", order = 0)
    public void createGame(Client client, ITestResult resolve) {
        Request req = client.newRequest("/newGame");
        client.get(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }
            if (!response.getBody().hasKey("gameUUID")) {
                resolve.returnResult(false);
                return;
            }
            String gameId = response.getBody().get("gameUUID").orElse("");
            GameReadyTestStore.getInstance().setGameId(gameId);
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "game needs player test", order = 1)
    public void needsPlayerTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/gameReady");
        req.setQuery("id", GameReadyTestStore.getInstance().getGameId());
        client.get(req, response -> resolve.returnResult(response.getStatusCode() == 202), error -> resolve.returnResult(false));
    }

    @Test(name = "join first player", order = 2)
    public void joinFirstPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Goodra");
        req.setQuery("id", GameReadyTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "game still needs player test", order = 3)
    public void stillNeedsPlayerTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/gameReady");
        req.setQuery("id", GameReadyTestStore.getInstance().getGameId());
        client.get(req, response -> resolve.returnResult(response.getStatusCode() == 202), error -> resolve.returnResult(false));
    }

    @Test(name = "join second player", order = 4)
    public void joinSecondPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Torracat");
        req.setQuery("id", GameReadyTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "game is ready test", order = 5)
    public void gameReadyTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/gameReady");
        req.setQuery("id", GameReadyTestStore.getInstance().getGameId());
        client.get(req, response -> resolve.returnResult(response.getStatusCode() == 204), error -> resolve.returnResult(false));
    }
}
