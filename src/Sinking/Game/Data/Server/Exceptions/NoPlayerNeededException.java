package Sinking.Game.Data.Server.Exceptions;

import java.util.UUID;

public class NoPlayerNeededException extends Exception {
    public NoPlayerNeededException(UUID id) {
        super(String.format("Game with id '%s' does not need another player.", id));
    }
}
