package Sinking.Ai;

import Sinking.Game.Data.Board;
import Sinking.common.BaseAi;
import Sinking.common.Tupel;

public class ExampleAi extends BaseAi {
    private int x = -1;
    private int y = 0;

    public ExampleAi() {
        this.name = "John Doe";
    }

    @Override
    public Tupel<Integer, Integer> nextMove(Board board) {
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

}
