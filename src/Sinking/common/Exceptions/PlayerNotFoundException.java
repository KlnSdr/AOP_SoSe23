package Sinking.common.Exceptions;

import java.util.UUID;

public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException(UUID gameId, String playerToken) {
        super(String.format("Game with id '%s' doesn't have a player with the token '%s'.", gameId, playerToken));
    }
}
