package Sinking.Game.Data;

import Sinking.common.Exceptions.CoordinatesOutOfBoundsException;

public class Board {
    private Tile[][] board;

    public Board() {
        createBoard();
    }

    private void createBoard() {
        board = new Tile[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new WaterTile();
            }
        }
    }

    public Tile[][] getBoard() {
        return board.clone();
    }

    public boolean setShip(int x, int y) {
        boolean status = bounces(x, y);
        if (status) {
            board[x][y] = new ShipTile();
        }
        return status;
    }

    public boolean fire(int x, int y) throws CoordinatesOutOfBoundsException {
        boolean status = bounces(x, y);
        if (status) {
            board[x][y].shoot();
        } else {
            throw new CoordinatesOutOfBoundsException(x, y);
        }
        return true;
    }

    public boolean hit(int x, int y) {
        return board[x][y].state == TileState.HIT;
    }

    public boolean bounces(int x, int y) {
        if (x >= 10 || x < 0) {
            System.out.println(" Deine Eingabe (x) ist nicht mehr in den Grenzen des Spielfeldes.");
            return false;
        }
        if (y >= 10 || y < 0) {
            System.out.println(" Deine Eingabe (y) ist nicht mehr in den Grenzen des Spielfeldes.");
            return false;
        }
        return true;
    }

}
