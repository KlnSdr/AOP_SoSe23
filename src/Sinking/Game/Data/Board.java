package Sinking.Game.Data;

public class Board {
    private Tile[][] board;

    public Board() {
        createBoard();
    }

    private void createBoard() {
        board = new Tile[10][10];
        Tile waterTile = new WaterTile();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = waterTile;
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.print("\n");
        }
        System.out.println(board[5][5].getState());
    }

    public void setShip(int x, int y) {
        board[x][y] = new ShipTile();
    }

    public void fire(int x, int y) {
        System.out.println("Schieß spieß");
        board[x][y].shoot();
        System.out.println(board[x][y].getState());
    }
}
