package Sinking.http.test.tests;

import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.JoinGameTestStore;

public class SetShipsTest {
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

    @Test(name = "set ships, needs player")
    public void setShipsNeedsPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/setShips");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        // todo req.setBody("playerToken",);
        req.setBody("ships", "0,0|0,1|0,2|0,3|0,4");

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

    @Test(name = "set ships")
    public void setShips(Client client, ITestResult resolve) {
        Request req = client.newRequest("/setShips");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        // todo req.setBody("playerToken",);
        req.setBody("ships", "0,0|0,1|0,2|0,3|0,4");

        client.post(req, response -> {
            if (response.getStatusCode() != 422) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        } , error -> resolve.returnResult(false));
    }
}
