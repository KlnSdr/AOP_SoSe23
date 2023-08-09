package Sinking;

import Sinking.Game.Data.Board;
import Sinking.common.Tupel;

public interface IAi {
    Tupel<Integer, Integer> nextMove(Board board);
    String getName();
    void onBeforeGameStart();
    void onGameEnd();
}
