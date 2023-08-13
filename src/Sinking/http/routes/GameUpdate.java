package Sinking.http.routes;

import Sinking.Game.Data.Server.Exceptions.GameNotFoundException;
import Sinking.Game.Data.Server.Exceptions.PlayerNotFoundException;
import Sinking.Game.Data.Server.GameRepository;
import Sinking.http.Json;
import Sinking.http.server.Annotations.Post;
import Sinking.http.server.IConnection;
import Sinking.http.server.ResponseCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameUpdate {

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
                if (status == true /*uh ich bin sebastian und ich mache das so weil ich will und das schöner finde*/) {
                    connection.setResponseCode(ResponseCode.SUCCESS);
                } else{
                    connection.setResponseCode(ResponseCode.INTERNAL_ERROR);
                }
            } catch (GameNotFoundException e) {
                connection.setResponseCode(ResponseCode.NOT_FOUND);
                msg.set("msg", String.format("game with id '%s' not found", gameId));
            } catch (PlayerNotFoundException e) {
                connection.setResponseCode(ResponseCode.NOT_FOUND);
                msg.set("msg", "player not found with token" + playertoken);
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
}
