package Sinking.http.test.tests;

import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.FireAtTestStore;

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
            FireAtTestStore.getInstance().setGameId(gameId);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }


    @Test(name = "join first player", order = 1)
    public void joinFirstPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Garry Otter");
        req.setQuery("id", FireAtTestStore.getInstance().getGameId());
        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }
            if (!response.getBody().hasKey("playerToken")) {
                resolve.returnResult(false);
                return;
            }
            String playerToken = response.getBody().get("playerToken").orElse("");
            FireAtTestStore.getInstance().setPlayerToken(playerToken);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "fire at, needs player", order = 2)
    public void fireAtNeedsPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/fireAt");
        req.setQuery("id", FireAtTestStore.getInstance().getGameId());
        req.setBody("playerToken", FireAtTestStore.getInstance().getPlayerToken());
        req.setBody("x", "0");
        req.setBody("y", "0");

        client.post(req, response -> {
            if (response.getStatusCode() != 422) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "join second player", order = 3)
    public void joinSecondPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Neinhorn");
        req.setQuery("id", FireAtTestStore.getInstance().getGameId());
        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }
            if (!response.getBody().hasKey("playerToken")) {
                resolve.returnResult(false);
                return;
            }
            String playerToken = response.getBody().get("playerToken").orElse("");
            FireAtTestStore.getInstance().setPlayerToken(playerToken);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "fire at", order = 4)
    public void fireAt(Client client, ITestResult resolve) {
        Request req = client.newRequest("/fireAt");
        req.setQuery("id", FireAtTestStore.getInstance().getGameId());
        req.setBody("playerToken", FireAtTestStore.getInstance().getPlayerToken());
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
        }, error -> resolve.returnResult(false));
    }
}
