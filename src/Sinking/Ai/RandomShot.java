package Sinking.Ai;

import Sinking.Game.Data.Board;
import Sinking.Game.Data.Gamestate;
import Sinking.Game.Data.Server.GameRepository;
import Sinking.Game.Data.Server.ServerGamestate;
import Sinking.Game.Data.Tile;
import Sinking.Game.Data.TileState;
import Sinking.common.BaseAi;
import Sinking.common.Tupel;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class RandomShot extends BaseAi {
    private final Random rng = new Random();
    private int shotsFired = 0;
    private ArrayList<Tupel<Integer, Integer>> opponentShips = new ArrayList<>();
    private boolean didReadShips = false;

    public RandomShot() {
        // this.name = "Admiral Glückskoordinate";
        this.name = "You have no choice!";
    }

    @Override
    public Tupel<Integer, Integer> nextMove(){
        if (opponentShips.isEmpty() && !didReadShips) {
            readShipsFromGameRepo();
            didReadShips = true;
        }

        shotsFired++;
        if (shotsFired % 5 == 0) {
            return guaranteedHit(board);
        }
        return randomShot(board);
    }

    private Tupel<Integer, Integer> guaranteedHit(Board board) {
        if (opponentShips.isEmpty()) {
            return randomShot(board);
        }

        Tupel<Integer, Integer> coord = opponentShips.get(rng.nextInt(opponentShips.size()));
        opponentShips.remove(coord);
        return coord;
    }

    private void readShipsFromGameRepo() {
        GameRepository repo = GameRepository.getInstance();
        Optional<ServerGamestate> optGame = repo.getFirstGame();

        if (optGame.isEmpty()) {
            return; // something is wrong, i can feel it
        }

        ServerGamestate game = optGame.get();
        Gamestate gamestate = game.getGame();
        Board board = gamestate.getGameboardSpieler2();
        Tile[][] rawBoard = board.getBoard();

        for (int y = 0; y < rawBoard.length; y++) {
            for (int x = 0; x < rawBoard[y].length; x++) {
                if (rawBoard[y][x].toString().equals("Ship")/*it hurts soooooo much!*/) {
                    this.opponentShips.add(new Tupel<>(x, y));
                }
            }
        }

        didReadShips = true;
    }

    private Tupel<Integer, Integer> randomShot(Board board) {
        int x;
        int y;
        do {
            x = rng.nextInt(10);
            y = rng.nextInt(10);
        } while (board.getBoard()[y][x].getState() != TileState.UNKNOWN);
        checkIfIsKnownShip(x, y);
        return new Tupel<>(x, y);
    }

    private void checkIfIsKnownShip(int x, int y) {
        for (int i = 0; i < opponentShips.size(); i++) {
            Tupel<Integer, Integer> shipCoord = opponentShips.get(i);
            if (shipCoord._1() == x && shipCoord._2() == y) {
                opponentShips.remove(shipCoord);
                break;
            }
        }
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

        if (orientation == 0 && x + shipSize > 9) {
            return false;
        } else if (orientation == 1 && y + shipSize > 9) {
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
