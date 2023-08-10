package Sinking.http.routes;

import Sinking.Game.Data.Server.Exceptions.GameFinishedException;
import Sinking.Game.Data.Server.Exceptions.GameNotFoundException;
import Sinking.Game.Data.Server.Exceptions.NoPlayerNeededException;
import Sinking.Game.Data.Server.GameRepository;
import Sinking.Game.Data.Server.ServerGamestate;
import Sinking.Game.Data.Server.ServerPlayerWrapper;
import Sinking.http.Json;
import Sinking.http.server.Annotations.Get;
import Sinking.http.server.IConnection;

import java.io.IOException;
import java.util.UUID;

public class GameSetup {

    @Get(route = "/Schiffeversenken/Titanic/Ahoi")
    public void SpielErstellen(IConnection verbindung) throws IOException, GameFinishedException, NoPlayerNeededException, GameNotFoundException {
        Json msg = new Json();
        msg.set("Spiel", "Schiffeversenken");


        GameRepository spielstand = GameRepository.getInstance();
        UUID werBinIch= spielstand.newGame();


        msg.set("UUID", werBinIch.toString());


        verbindung.sendResponse(msg);

    }
}
