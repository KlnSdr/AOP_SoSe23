package Sinking.common;

import Sinking.Game.Data.Board;
import Sinking.IAi;

public abstract class BaseAi implements IAi {
    protected String name;

    public void onBeforeGameStart() {
    }

    public void onGameEnd() {
    }

    public String getName() {
        return this.name;
    }

    public Tupel<Integer, Integer> nextMove(Board board) {
        return new Tupel<>(0, 0);
    }
}
