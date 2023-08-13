package Sinking.http.routes;

import Sinking.Game.Data.Server.GameRepository;
import Sinking.Game.Data.Tile;
import Sinking.Game.Data.TileState;
import Sinking.common.Exceptions.*;
import Sinking.common.Exceptions.CoordinatesOutOfBoundsException;
import Sinking.http.Json;
import Sinking.http.server.Annotations.Post;
import Sinking.http.server.IConnection;
import Sinking.http.server.ResponseCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameUpdate {
    @Post(route = "/getBoard")
    public void getBoard(IConnection connection) throws IOException {
        Map<String, List<String>> query = connection.getUriParams();
        Json body = connection.getRequestBody();

        if (!isValidGetBoardRequest(query, body)) {
            connection.setResponseCode(ResponseCode.BAD_REQUEST);
            Json responsePayload = new Json();
            responsePayload.set("msg", "malformed request object. missing gameId or player access token");
            connection.sendResponse(responsePayload);
            return;
        }

        String gameIdStr = query.get("id").get(0);
        String playerToken = body.get("accessToken").orElse("");

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
        ResponseCode resCode = ResponseCode.SUCCESS;

        try {
            Tile[][] board = GameRepository.getInstance().getBoard(gameId, playerToken);
            responsePayload.set("board", boardToString(board));
        } catch (PlayerNotFoundException e) {
            resCode = ResponseCode.NOT_FOUND;
            responsePayload.set("msg", String.format("player with id '%s' not found", gameId));
        } catch (GameNotFoundException e) {
            resCode = ResponseCode.NOT_FOUND;
            responsePayload.set("msg", String.format("game with id '%s' not found", gameId));
        } catch (NeedsPlayerException e) {
            resCode = ResponseCode.UNPROCESSABLE_ENTITY;
            responsePayload.set("msg", String.format("game with id '%s' needs a player", gameId));
        }

        connection.setResponseCode(resCode);
        connection.sendResponse(responsePayload);
    }

    private String boardToString(Tile[][] board) {
        StringBuilder out = new StringBuilder();
        for (Tile[] row : board) {
            for (Tile tile : row) {
                TileState state = tile.getState();
                switch (state) {
                    case UNKNOWN:
                        out.append("U");
                        break;
                    case HIT:
                        out.append("H");
                        break;
                    case MISS:
                        out.append("M");
                        break;
                }
            }
            out.append(";");
        }
        return out.toString();
    }

    @Post(route = "/fireAt")
    public void fire(IConnection connection) throws IOException {
        int x;
        int y;
        UUID gameId;
        String playertoken;
        Map<String, List<String>> query = connection.getUriParams();
        Json body = connection.getRequestBody();

        if (!correctRequest(query, body)){
            connection.setResponseCode(ResponseCode.BAD_REQUEST);
            Json msg = new Json();
            msg.set("msg", "malformed request object. missing gameId, player nick name, x-coordinate or y-coordinate");
            connection.sendResponse(msg);
            return;
        }

        try {
            gameId = UUID.fromString(query.get("id").get(0));
        } catch (IllegalArgumentException e) {
            Json msg = new Json();
            msg.set("error", String.format("invalid game id '%s'", query.get("id").get(0)));
            connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
            connection.sendResponse(msg);
            return;
        }

        playertoken = body.get("playertoken").orElse("");

        try {
            x = Integer.parseInt(body.get("x").orElse(""));
            y = Integer.parseInt(body.get("y").orElse(""));
        } catch (NumberFormatException e) {
            Json msg = new Json();
            msg.set("error", String.format("wrong coordinates (%s, %s)", body.get("x"), body.get("y")));
            connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
            connection.sendResponse(msg);
            return;
        }

        Json msg = new Json();

        try {
            boolean status = GameRepository.getInstance().fireAt(x, y, gameId, playertoken);
            connection.setResponseCode(ResponseCode.SUCCESS);
            if (status == true /*uh ich bin sebastian und ich mache das so weil ich will und das schöner finde*/) {
                msg.set("hitShip", "true");
            } else{
                msg.set("hitShip", "false");
            }
        } catch (GameNotFoundException e) {
            connection.setResponseCode(ResponseCode.NOT_FOUND);
            msg.set("msg", String.format("game with id '%s' not found", gameId));
        } catch (PlayerNotFoundException e) {
            connection.setResponseCode(ResponseCode.NOT_FOUND);
            msg.set("msg", "player not found with token" + playertoken);
        } catch (CoordinatesOutOfBoundsException e) {
            connection.setResponseCode(ResponseCode.INTERNAL_ERROR);
            msg.set("msg", "\uD83D\uDD95 invalid coordinates (" +x+ ", " +y+ ")");
        } catch (NeedsPlayerException e) {
            connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
            msg.set("msg", String.format("game with id '%s' needs another player", gameId));
        }

        connection.sendResponse(msg);
    }
    private boolean correctRequest(Map<String, List<String>> query, Json body) {
        if (!query.containsKey("id") || query.get("id").isEmpty()){
            return false;
        }
        if (!body.hasKey("playertoken") || !body.hasKey("x") || !body.hasKey("y")){
            return false;
        }
        if(body.get("x").isEmpty() || body.get("y").isEmpty() || body.get("playertoken").isEmpty()){
            return false;
        }
        return true;
    }

    private boolean isValidGetBoardRequest(Map<String, List<String>> query, Json body) {
        if (!query.containsKey("id") || query.get("id").isEmpty()) {
            return false;
        }
        return body.hasKey("accessToken");
    }
}
