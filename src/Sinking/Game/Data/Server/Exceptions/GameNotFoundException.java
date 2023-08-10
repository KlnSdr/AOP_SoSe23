package Sinking.Game.Data.Server.Exceptions;

import java.util.UUID;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(UUID id) {
        super(String.format("Game with id '%s' not found.", id));
    }
}
