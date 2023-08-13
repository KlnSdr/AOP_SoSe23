package Sinking.common.Exceptions;

import java.util.UUID;

public class NeedsPlayerException extends Exception {
    public NeedsPlayerException(UUID id) {
        super(String.format("Game with id '%s' needs another player.", id));
    }
}
