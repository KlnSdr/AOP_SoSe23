package Sinking.http.test.tests;

import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.JoinGameTestStore;

public class JoinGameTest {
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
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "missing nickname", order = 1)
    public void missingNameTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());

        client.post(req, response -> {
            if (response.getStatusCode() != 400) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "missing gameId", order = 2)
    public void missingIdTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "John Doe");

        client.post(req, response -> {
            if (response.getStatusCode() != 400) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "valid gameId, empty nickname", order = 3)
    public void validIdInvalidNameTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        req.setBody("nickname", "");

        client.post(req, response -> {
            if (response.getStatusCode() != 422) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "invalid gameId, valid nickname", order = 4)
    public void invalidIdValidNameTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setQuery("id", "invalid");
        req.setBody("nickname", "John Doe");

        client.post(req, response -> {
            if (response.getStatusCode() != 422) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "valid but unknown gameId", order = 5)
    public void validUnknownIdTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setQuery("id", "00000000-0000-0000-0000-000000000000");
        req.setBody("nickname", "John Doe");

        client.post(req, response -> {
            if (response.getStatusCode() != 404) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "add first player to game", order = 6)
    public void addFirstPlayerTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        req.setBody("nickname", "John Doe");

        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }
            if (!response.getBody().hasKey("playerToken")) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "add second player to game", order = 7)
    public void addSecondPlayerTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        req.setBody("nickname", "John Wick");

        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }
            if (!response.getBody().hasKey("playerToken")) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }

    @Test(name = "add third player to game", order = 8)
    public void addThirdPlayerFailTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        req.setBody("nickname", "John Wick");

        client.post(req, response -> {
            if (response.getStatusCode() != 500) {
                resolve.returnResult(false);
                return;
            }
            resolve.returnResult(true);
        }, error -> {
            resolve.returnResult(false);
        });
    }
}
