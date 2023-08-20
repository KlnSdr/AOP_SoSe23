package Sinking.Ai;

import Sinking.Game.Data.Board;
import Sinking.common.BaseAi;
import Sinking.common.Tupel;

public class ExampleAI extends BaseAi {
    private int x = -1;
    private int y = 0;

    public ExampleAI() {
        this.name = "John Doe";
    }

    @Override
    public Tupel<Integer, Integer> nextMove() {
        x++;
        if (x >= 10) {
            x = 0;
            y++;
        }

        if (y >= 10) {
            y = 0;
        }
        return new Tupel<>(x, y);
    }

    @Override
    public Tupel<Integer, Integer>[] setShips() {
        return new Tupel[]{new Tupel(0, 0), new Tupel(0, 1), new Tupel(0, 2), new Tupel(0, 3), new Tupel(0, 4), new Tupel(0, 5), new Tupel(1, 0)};
    }

}