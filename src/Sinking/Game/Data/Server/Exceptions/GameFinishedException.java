package Sinking.Game.Data.Server.Exceptions;

import java.util.UUID;

public class GameFinishedException extends Exception {
    public GameFinishedException(UUID id) {
        super(String.format("Game with id '%s' was already finished.", id));
    }
}
