package Sinking.http.routes;

import Sinking.Game.Data.Player;
import Sinking.common.Exceptions.GameFinishedException;
import Sinking.common.Exceptions.GameNotFoundException;
import Sinking.common.Exceptions.NoPlayerNeededException;
import Sinking.Game.Data.Server.GameRepository;
import Sinking.common.Exceptions.PlayerNotFoundException;
import Sinking.common.Json;
import Sinking.http.server.Annotations.Post;
import Sinking.http.server.Annotations.Get;
import Sinking.http.server.IConnection;
import Sinking.http.server.ResponseCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameSetup {
    @Get(route = "/newGame")
    public void SpielErstellen(IConnection verbindung) throws IOException {
        Json msg = new Json();

        GameRepository spielstand = GameRepository.getInstance();
        UUID gameId= spielstand.newGame();


        msg.set("gameUUID", gameId.toString());


        verbindung.sendResponse(msg);

    }

    @Post(route = "/join")
    public void joinGame(IConnection connection) throws IOException {
        Map<String, List<String>> query = connection.getUriParams();
        Json body = connection.getRequestBody();

        if (!isValidJoinRequest(query, body)) {
            connection.setResponseCode(ResponseCode.BAD_REQUEST);
            Json responsePayload = new Json();
            responsePayload.set("msg", "malformed request object. missing gameId or player nick name");
            connection.sendResponse(responsePayload);
            return;
        }

        String gameIdStr = query.get("id").get(0);
        UUID gameId;
        try {
            gameId = UUID.fromString(gameIdStr);
        } catch (IllegalArgumentException e) {
            Json responsePayload = new Json();
            responsePayload.set("msg", String.format("invalid game id '%s'", gameIdStr));
            connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
            connection.sendResponse(responsePayload);
            return;
        }
        String nickname = body.get("nickname").orElse("");

        if (nickname.trim().isEmpty()) {
            connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
            Json responsePayload = new Json();
            responsePayload.set("msg", "invalid nick name");
            connection.sendResponse(responsePayload);
            return;
        }

        Player player = new Player();
        player.setName(nickname);

        Json responsePayload = new Json();
        ResponseCode resCode = ResponseCode.SUCCESS;

        try {
            String playerAccessToken = GameRepository.getInstance().addPlayer(gameId, player);
            responsePayload.set("playerToken", playerAccessToken);
        } catch (GameNotFoundException e) {
            resCode = ResponseCode.NOT_FOUND;
            responsePayload.set("msg", String.format("game with id '%s' not found", gameId));
        } catch (GameFinishedException | NoPlayerNeededException e) {
            resCode = ResponseCode.INTERNAL_ERROR;
            responsePayload.set("msg", String.format("no additional player needed for game with id '%s'", gameId));
        }

        connection.setResponseCode(resCode);
        connection.sendResponse(responsePayload);
    }

    @Get(route = "/gameReady")
    public void checkIfAnotherPlayerIsNeeded(IConnection connection) throws IOException {
        Map<String, List<String>> query = connection.getUriParams();

        if (!isValidCheckGameStartStatusRequest(query)) {
            connection.setResponseCode(ResponseCode.BAD_REQUEST);
            Json responsePayload = new Json();
            responsePayload.set("msg", "malformed request object. missing gameId or player access token");
            connection.sendResponse(responsePayload);
            return;
        }

        String gameIdStr = query.get("id").get(0);

        UUID gameId;
        try {
            gameId = UUID.fromString(gameIdStr);
        } catch (IllegalArgumentException e) {
            Json responsePayload = new Json();
            responsePayload.set("msg", String.format("invalid game id '%s'", gameIdStr));
            connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
            connection.sendResponse(responsePayload);
            return;
        }

        Json responsePayload = new Json();
        ResponseCode resCode;

        try {
            boolean needsPlayer = GameRepository.getInstance().needsPlayer(gameId);
            // 204 -> no player needed -> start game
            // 202 -> player needed -> wait for player
            resCode = needsPlayer ? ResponseCode.ACCEPTED : ResponseCode.NO_CONTENT;
        } catch (GameNotFoundException e) {
            resCode = ResponseCode.NOT_FOUND;
            responsePayload.set("msg", String.format("game with id '%s' not found", gameId));
        }

        connection.setResponseCode(resCode);
        connection.sendResponse(responsePayload);
    }

    @Post(route ="/getName")
    public void getNameEnemie (IConnection connection) throws IOException, GameNotFoundException, PlayerNotFoundException {
        Map<String, List<String>> query = connection.getUriParams();
        Json body = connection.getRequestBody();

        if (!isValidNameRequest(query, body)) {
            connection.setResponseCode(ResponseCode.BAD_REQUEST);
            Json responsePayload = new Json();
            responsePayload.set("msg", "malformed request object. missing gameId or playertoken");
            connection.sendResponse(responsePayload);
            return;
        }

        String gameIdStr = query.get("id").get(0);

        UUID gameId;
        try {
            gameId = UUID.fromString(gameIdStr);
        } catch (IllegalArgumentException e) {
            Json responsePayload = new Json();
            responsePayload.set("msg", String.format("invalid game id '%s'", gameIdStr));
            connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
            connection.sendResponse(responsePayload);
            return;
        }
        String playertoken = body.get("playerToken").orElse("Gegner");
        String opponetName = "Gegner";
        Json msg = new Json();
        ResponseCode resCode = ResponseCode.SUCCESS;
        try{
            opponetName= GameRepository.getInstance().getNameOpponent(gameId, playertoken);
        } catch (GameNotFoundException e) {
            connection.setResponseCode(ResponseCode.NOT_FOUND);
            msg.set("msg", String.format("game with id '%s' not found", gameId));
        } catch (PlayerNotFoundException e) {
            connection.setResponseCode(ResponseCode.NOT_FOUND);
            msg.set("msg", "player not found with token" + playertoken);
        }
        msg.set("Opponentname", opponetName);
        connection.setResponseCode(resCode);
        connection.sendResponse(msg);
    }

    private boolean isValidJoinRequest(Map<String, List<String>> query, Json body) {
        if (!query.containsKey("id") || query.get("id").isEmpty()) {
            return false;
        }
        return body.hasKey("nickname");
    }
    private boolean isValidCheckGameStartStatusRequest(Map<String, List<String>> query) {
        return query.containsKey("id") && !query.get("id").isEmpty();
    }
    private boolean isValidNameRequest(Map<String, List<String>> query, Json body) {
        if (!query.containsKey("id") || query.get("id").isEmpty()) {
            return false;
        }
        if (!body.hasKey("playerToken")){
            return false;
        }
        return true;
    }
}
