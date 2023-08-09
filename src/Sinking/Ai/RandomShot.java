package Sinking.Ai;

import Sinking.Game.Data.Board;
import Sinking.Game.Data.Tile;
import Sinking.common.BaseAi;
import Sinking.common.Tupel;

import java.util.ArrayList;
import java.util.Random;

public class RandomShot extends BaseAi {
    private int shotsFired = 0;
    private final Random rng = new Random();
    public RandomShot() {
        this.name = "Admiral Glückskoordinate";
    }

    @Override
    public Tupel<Integer, Integer> nextMove(Board board) {
        shotsFired++;
        if (shotsFired % 5 == 0) {
            return guaranteedHit(board);
        }
        return randomShot(board);
    }

    private Tupel<Integer, Integer> guaranteedHit(Board board) {
        ArrayList<Tupel<Integer, Integer>> ships = filterBoard(board);
        if (ships.isEmpty()) {
            return randomShot(board);
        }
        return ships.get(rng.nextInt(ships.size()));
    }

    private ArrayList<Tupel<Integer, Integer>> filterBoard(Board board) {
        Tile[][] rawBoard = board.getBoard();
        ArrayList<Tupel<Integer, Integer>> out = new ArrayList<>();
        for (int x  = 0; x < rawBoard.length; x++) {
            for (int y = 0; y < rawBoard[x].length; y++) {
                if (!rawBoard[x][y].wasHit() && rawBoard[x][y].toString().equals("Ship") /*it hurts sooooo much!*/)
                {
                    out.add(new Tupel<>(x, y));
                }
            }
        }
        return out;
    }

    private Tupel<Integer, Integer> randomShot(Board board) {
        int x;
        int y;
        do {
            x = rng.nextInt(10);
            y = rng.nextInt(10);
        } while(board.getBoard()[x][y].wasHit());
        return new Tupel<>(x, y);
    }
}
