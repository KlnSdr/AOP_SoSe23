package Sinking.common;

import Sinking.Game.Data.Board;
import Sinking.IAi;
import Sinking.common.Exceptions.CoordinatesOutOfBoundsException;

public abstract class BaseAi implements IAi {
    protected String name;
    protected Board board = new Board();

    public void onBeforeGameStart() {
    }

    public void onGameEnd() {
    }

    public String getName() {
        return this.name;
    }

    public Tupel<Integer, Integer> nextMove() {
        return new Tupel<>(0, 0);
    }
    protected void updateBoard(boolean didHitShip, int x, int y) {
        if (didHitShip) {
            board.setShip(x, y);
        }
        try {
            board.fire(x, y);
        } catch (CoordinatesOutOfBoundsException e) {
            // This should never happen so we ignore it like professionals
        }
    }
}
