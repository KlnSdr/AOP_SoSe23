package Sinking.http.routes;

import Sinking.Game.Data.Server.GameRepository;
import Sinking.http.Json;
import Sinking.http.server.Annotations.Get;
import Sinking.http.server.IConnection;

import java.io.IOException;
import java.util.UUID;

public class GameSetup {

    @Get(route = "/newGame/Schiffeversenken")
    public void SpielErstellen(IConnection verbindung) throws IOException {
        Json msg = new Json();

        GameRepository spielstand = GameRepository.getInstance();
        UUID gameId= spielstand.newGame();


        msg.set("UUID", gameId.toString());


        verbindung.sendResponse(msg);

    }
}
