package Sinking.common.Exceptions;

import java.util.UUID;

public class NotYourTurnException extends Exception {
    public NotYourTurnException(UUID gameId, String playerToken) {
        super(String.format("It's not the turn of player with token '%s' in game '%s'.", gameId, playerToken));
    }
}
