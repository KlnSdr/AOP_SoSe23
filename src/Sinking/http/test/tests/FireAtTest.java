package Sinking.http.test.tests;

import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.JoinGameTestStore;

public class FireAtTest {
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
            JoinGameTestStore.getInstance().setGameId(gameId);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "join first player", order = 2)
    public void joinFirstPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Garry Otter");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "fire at, needs player")
    public void fireAtNeedsPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/fireAt");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        // todo req.setBody("playerToken",);
        req.setBody("x", "0");
        req.setBody("y", "0");

        client.post(req, response -> {
            if (response.getStatusCode() != 422) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        } , error -> resolve.returnResult(false));
    }

    @Test(name = "join second player", order = 2)
    public void joinSecondPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Neinhorn");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "fire at")
    public void fireAt(Client client, ITestResult resolve) {
        Request req = client.newRequest("/fireAt");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        // todo req.setBody("playerToken",);
        req.setBody("x", "0");
        req.setBody("y", "0");

        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }

            if (!response.getBody().hasKey("hitShip") || !response.getBody().get("hitShip").orElse("true").equals("false")) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        } , error -> resolve.returnResult(false));
    }
}
