package Sinking.http.routes;

import Sinking.Game.Data.Server.Exceptions.GameNotFoundException;
import Sinking.Game.Data.Server.Exceptions.PlayerNotFoundException;
import Sinking.Game.Data.Server.GameRepository;
import Sinking.Game.Data.Server.ServerPlayerWrapper;
import Sinking.http.Json;
import Sinking.http.server.Annotations.Post;
import Sinking.http.server.IConnection;

import javax.print.DocFlavor;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameUpdate {
    int x;
    int y;
    UUID gameId;
    Map<String, List<String>> query;
    String playertoken;





@Post(route = "/fireAt")
    public void fire(IConnection connection) throws PlayerNotFoundException, GameNotFoundException {
    Map<String, List<String>> query = connection.getUriParams();
    Json body = connection.getRequestBody();






    GameRepository.getInstance().fireAt(x,y,gameId, "playertoken");


    }
}
