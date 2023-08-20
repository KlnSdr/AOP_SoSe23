package Sinking;

import Sinking.common.Tupel;

public interface IAi {
    Tupel<Integer, Integer> nextMove();
    Tupel<Integer, Integer>[] setShips();
    String getName();
    void onBeforeGameStart();
    void onGameEnd();
}
