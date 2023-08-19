package Sinking;

import Sinking.Game.Data.Board;
import Sinking.common.Tupel;

public interface IAi {
    Tupel<Integer, Integer> nextMove(Board board);
    Tupel<Integer, Integer>[] setShips();
    String getName();
    void onBeforeGameStart();
    void onGameEnd();
}
