package Sinking.http.test.tests;

import Sinking.common.Json;
import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;
import Sinking.http.test.tests.TestStore.JoinGameTestStore;

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
            JoinGameTestStore.getInstance().setGameId(gameId);
            resolve.returnResult(true);
        }, error -> resolve.returnResult(false));
    }

    @Test(name = "join first player", order = 2)
    public void joinFirstPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Manny");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "join second player", order = 2)
    public void joinSecondPlayer(Client client, ITestResult resolve) {
        Request req = client.newRequest("/join");
        req.setBody("nickname", "Eleven");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        client.post(req, response -> resolve.returnResult(response.getStatusCode() == 200), error -> resolve.returnResult(false));
    }

    @Test(name = "get gamestate initial")
    public void getInitialGameState(Client client, ITestResult resolve) {
        Request req = client.newRequest("/gameState");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        //todo req.setBody("playerToken", JoinGameTestStore.getInstance().getFirstPlayerToken());

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

    @Test(name = "fire at (0, 0)")
    public void fireAtTest(Client client, ITestResult resolve) {
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

    @Test(name = "get gamestate after shot")
    public void getGameStateAfterShot(Client client, ITestResult resolve) {
        Request req = client.newRequest("/gameState");
        req.setQuery("id", JoinGameTestStore.getInstance().getGameId());
        //todo req.setBody("playerToken", JoinGameTestStore.getInstance().getFirstPlayerToken()); // opponent

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
