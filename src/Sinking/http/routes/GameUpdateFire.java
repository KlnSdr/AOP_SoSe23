package Sinking.http.routes;

import Sinking.Game.Data.Server.Exceptions.GameNotFoundException;
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
    int x;
    int y;
    UUID gameId;
    Map<String, List<String>> query;
    String playertoken;
    @Post(route = "/fireAt")
    public void fire(IConnection connection) throws PlayerNotFoundException, GameNotFoundException, IOException {
        Map<String, List<String>> query = connection.getUriParams();
        Json body = connection.getRequestBody();
        //Get gibt hier einen int zurück weiß aber nicht ob das so passt
        gameId = UUID.fromString(query.get("id").get(0));
        playertoken = body.get("playertoken").orElse("");
        x = Integer.parseInt(body.get("x").orElse(""));
        y = Integer.parseInt(body.get("y").orElse(""));

        if(correctRequest(query,body)) {
            GameRepository.getInstance().fireAt(x, y, gameId, "playertoken");
        }else{
            connection.setResponseCode(ResponseCode.BAD_REQUEST);
            Json msg= new Json();
            msg.set("msg", "malformed request object. missing gameId, player nick name, x-coordinate or y-coordinate");
            connection.sendResponse(msg);
        }


    }

    private boolean correctRequest(Map<String, List<String>> query, Json body) {
        if (!query.containsKey("id") || query.get("id").isEmpty()) {
            return false;
        }
        if (!(body.hasKey("playertoken")) && !body.hasKey("x") && !body.hasKey("y")) {
            return false;
        }
        if(body.get("x").isEmpty() && body.get("y").isEmpty() && body.get("playertoken").isEmpty()){
            return false;
        }
        return true;
    }
}
