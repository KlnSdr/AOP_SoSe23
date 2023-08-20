package Sinking.Ai;

import Sinking.Game.Data.Board;
import Sinking.common.BaseAi;
import Sinking.common.Tupel;

import java.util.ArrayList;

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
        ArrayList<Tupel<Integer, Integer>> ships = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                ships.add(new Tupel<>(i, j));
            }
        }
        return ships.toArray(new Tupel[0]);
    }

}