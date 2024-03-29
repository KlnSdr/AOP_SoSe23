package Sinking.http.test.tests;

import Sinking.common.Json;
import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.GameStateTestStore;

public class GameStateTest {
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
            GameStateTestStore.getInstance().setGameId(gameId);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "join first player", order = 1)
    public void joinFirstPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Manny");
        req.setQuery("id", GameStateTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "join second player", order = 2)
    public void joinSecondPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Eleven");
        req.setQuery("id", GameStateTestStore.getInstance().getGameId());
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
            GameStateTestStore.getInstance().setPlayerToken(playerToken);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "get gamestate initial", order = 3)
    public void getInitialGameState(Client client, ITestResult resolve) {
        Request req = client.newRequest("/getGamestate");
        req.setQuery("id", GameStateTestStore.getInstance().getGameId());
        req.setBody("playerToken", GameStateTestStore.getInstance().getPlayerToken());

        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }

            Json body = response.getBody();

            if (!body.hasKey("own") || !body.hasKey("opponent") || !body.hasKey("isNext") || !body.hasKey("hasWinner")) {
                resolve.returnResult(false);
                return;
            }

            String ownBoard = body.get("own").orElse("own");
            String opponentBoard = body.get("opponent").orElse("opponent");

            if (!ownBoard.equals(opponentBoard) || !ownBoard.equals("U".repeat(100))) {
                resolve.returnResult(false);
                return;
            }

            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "fire at (0, 0)", order = 4)
    public void fireAtTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/fireAt");
        req.setQuery("id", GameStateTestStore.getInstance().getGameId());
        req.setBody("playerToken", GameStateTestStore.getInstance().getPlayerToken());
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

    @Test(name = "get gamestate after shot", order = 5)
    public void getGameStateAfterShot(Client client, ITestResult resolve) {
        Request req = client.newRequest("/getGamestate");
        req.setQuery("id", GameStateTestStore.getInstance().getGameId());
        req.setBody("playerToken", GameStateTestStore.getInstance().getPlayerToken());

        client.post(req, response -> {
            if (response.getStatusCode() != 200) {
                resolve.returnResult(false);
                return;
            }

            Json body = response.getBody();

            if (!body.hasKey("own") || !body.hasKey("opponent") || !body.hasKey("isNext") || !body.hasKey("hasWinner")) {
                resolve.returnResult(false);
                return;
            }

            String ownBoard = body.get("own").orElse("own");
            String opponentBoard = body.get("opponent").orElse("opponent");

            if (ownBoard.equals(opponentBoard)) {
                resolve.returnResult(false);
                return;
            }

            if (opponentBoard.charAt(0) != 'M') {
                resolve.returnResult(false);
                return;
            }

            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }
}
