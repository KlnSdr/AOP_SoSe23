package Sinking.Ai;

import Sinking.Game.Data.Board;
import Sinking.Game.Data.Tile;
import Sinking.common.BaseAi;
import Sinking.common.Tupel;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public Tupel<Integer, Integer>[] setShips() {
        ArrayList<Tupel<Integer, Integer>> shipCoords = new ArrayList<>();
        int[] shipSizes = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

        for (int size : shipSizes) {
            boolean placed = false;
            do {
                int x = rng.nextInt(10);
                int y = rng.nextInt(10);
                int orientation = rng.nextInt(4);
                placed = isPlacementValid(size, shipCoords, x, y, orientation);
                if (placed) {
                    ArrayList<Tupel<Integer, Integer>> newShip = generateShipCoords(x, y, size, orientation);
                    addShipToCoords(shipCoords, newShip);
                }
            } while (!placed);
        }

        if (shipCoords.size() != 30) {
            setShips();
        }

        return shipCoords.toArray(new Tupel[0]);
    }

    private boolean isPlacementValid(int shipSize, ArrayList<Tupel<Integer, Integer>> shipCoords, int x, int y, int orientation) {
        boolean validPlacement = true;

        if (orientation == 0 && x + shipSize > 10) {
            return false;
        } else if (orientation == 1 && y + shipSize > 10) {
            return false;
        } else if (orientation == 2 && x - shipSize < 0) {
            return false;
        } else if (orientation == 3 && y - shipSize < 0) {
            return false;
        }

        ArrayList<Tupel<Integer, Integer>> newShip = generateShipCoords(x, y, shipSize, orientation);

        for (Tupel<Integer, Integer> ship : shipCoords) {
            int existingX = ship._1();
            int existingY = ship._2();

            for (Tupel<Integer, Integer> newCoord : newShip) {
                int newX = newCoord._1();
                int newY = newCoord._2();

                if (Math.abs(newX - existingX) <= 1 && Math.abs(newY - existingY) <= 1) {
                    validPlacement = false;
                    break;
                }
            }

            if (!validPlacement) {
                break;
            }
        }

        return validPlacement;
    }

    private ArrayList<Tupel<Integer, Integer>> generateShipCoords(int x, int y, int size, int orientation) {
        ArrayList<Tupel<Integer, Integer>> shipCoords = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (orientation == 0) {
                shipCoords.add(new Tupel<>(x + i, y));
            } else if (orientation == 1) {
                shipCoords.add(new Tupel<>(x, y + i));
            } else if (orientation == 2) {
                shipCoords.add(new Tupel<>(x - i, y));
            } else if (orientation == 3) {
                shipCoords.add(new Tupel<>(x, y - i));
            }
        }
        return shipCoords;
    }

    private void addShipToCoords(ArrayList<Tupel<Integer, Integer>> shipCoords, ArrayList<Tupel<Integer, Integer>> newShip) {
        shipCoords.addAll(newShip);
    }

}
