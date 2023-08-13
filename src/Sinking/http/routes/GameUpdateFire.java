package Sinking.http.routes;

import Sinking.Game.Data.Server.Exceptions.GameFinishedException;
import Sinking.Game.Data.Server.Exceptions.GameNotFoundException;
import Sinking.Game.Data.Server.Exceptions.NoPlayerNeededException;
import Sinking.Game.Data.Server.Exceptions.PlayerNotFoundException;
import Sinking.Game.Data.Server.GameRepository;
import Sinking.Game.Data.Server.ServerPlayerWrapper;
import Sinking.http.Json;
import Sinking.http.server.Annotations.Post;
import Sinking.http.server.IConnection;
import Sinking.http.server.ResponseCode;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class GameUpdateFire {

    @Post(route = "/fireAt")
    public void fire(IConnection connection) throws PlayerNotFoundException, GameNotFoundException, IOException {
        int x;
        int y;
        UUID gameId;
        String playertoken;
        Map<String, List<String>> query = connection.getUriParams();
        Json body = connection.getRequestBody();

        if (!correctRequest(query, body)) {
            connection.setResponseCode(ResponseCode.BAD_REQUEST);
            Json msg = new Json();
            msg.set("msg", "malformed request object. missing gameId, player nick name, x-coordinate or y-coordinate");
            connection.sendResponse(msg);
        } else {

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
            } catch (NumberFormatException e) {
                Json msg = new Json();
                msg.set("error", String.format("you got the wrong number", body.get("x")));
                connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
                connection.sendResponse(msg);
                return;
            }

            try {
                y = Integer.parseInt(body.get("y").orElse(""));
            } catch (NumberFormatException e) {
                Json msg = new Json();
                msg.set("error", String.format("you got the wrong letter", body.get("y")));
                connection.setResponseCode(ResponseCode.UNPROCESSABLE_ENTITY);
                connection.sendResponse(msg);
                return;
            }


            try {
                GameRepository.getInstance().fireAt(x, y, gameId, playertoken);
                connection.setResponseCode(ResponseCode.SUCCESS);

            } catch (GameNotFoundException e) {
                Json msg = new Json();
                connection.setResponseCode(ResponseCode.NOT_FOUND);
                msg.set("msg", String.format("game with id '%s' not found", gameId));
            } catch (PlayerNotFoundException e) {
                Json msg = new Json();
                connection.setResponseCode(ResponseCode.NOT_FOUND);
                msg.set("msg", String.format("player not found with token",playertoken));
            }
        }
    }
    private boolean correctRequest(Map<String, List<String>> query, Json body) {
        if (!query.containsKey("id") || query.get("id").isEmpty()) {
            return false;
        }
        if (!(body.hasKey("playertoken")) || !body.hasKey("x") || !body.hasKey("y")) {
            return false;
        }
        if(body.get("x").isEmpty() || body.get("y").isEmpty() || body.get("playertoken").isEmpty()){
            return false;
        }
        return true;
    }
}
