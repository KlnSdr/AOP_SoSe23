package Sinking.http.routes;

import Sinking.Game.Data.Player;
import Sinking.Game.Data.Server.Exceptions.GameFinishedException;
import Sinking.Game.Data.Server.Exceptions.GameNotFoundException;
import Sinking.Game.Data.Server.Exceptions.NoPlayerNeededException;
import Sinking.Game.Data.Server.GameRepository;
import Sinking.http.Json;
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

    private boolean isValidJoinRequest(Map<String, List<String>> query, Json body) {
        if (!query.containsKey("id") || query.get("id").isEmpty()) {
            return false;
        }
        return body.hasKey("nickname");
    }
}
