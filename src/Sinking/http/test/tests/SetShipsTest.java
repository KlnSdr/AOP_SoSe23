package Sinking.http.test.tests;

import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.SetShipsTestStore;

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
            SetShipsTestStore.getInstance().setGameId(gameId);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "join first player", order = 1)
    public void joinFirstPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Linkin Park");
        req.setQuery("id", SetShipsTestStore.getInstance().getGameId());
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
            SetShipsTestStore.getInstance().setPlayerToken(playerToken);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "set ships, needs player", order = 2)
    public void setShipsNeedsPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/setShips");
        req.setQuery("id", SetShipsTestStore.getInstance().getGameId());
        req.setBody("playerToken", SetShipsTestStore.getInstance().getPlayerToken());
        req.setBody("ships", "0,0|0,1|0,2|0,3|0,4");

        client.post(req, response -> {
            if (response.getStatusCode() != 422) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        } , error -> resolve.returnResult(false));
    }

    @Test(name = "join second player", order = 3)
    public void joinSecondPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Odesza");
        req.setQuery("id", SetShipsTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "set ships", order = 4)
    public void setShips(Client client, ITestResult resolve) {
        Request req = client.newRequest("/setShips");
        req.setQuery("id", SetShipsTestStore.getInstance().getGameId());
        req.setBody("playerToken", SetShipsTestStore.getInstance().getPlayerToken());
        req.setBody("ships", "0,0|0,1|0,2|0,3|0,4");

        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        } , error -> resolve.returnResult(false));
    }
}
