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
        WaterTile w = new WaterTile();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j].getState() == TileState.UNKNOWN) {
                    System.out.print(w + "\t");
                }
                else{
                    System.out.print(board[i][j] + "\t");
                }

            }
            System.out.print("\n");
        }
        System.out.println(board[5][5].getState());
    }

    public boolean setShip(int x, int y) {
        boolean status = bounces(x,y);
        if (status) {
            board[x][y] = new ShipTile();
        }
        return status;
    }

    public boolean fire(int x, int y) {
        boolean status = bounces(x,y);
        if (status){
            board[x][y].shoot();
            System.out.println(board[x][y].getState());
        }
        return status;
    }

    public boolean bounces(int x, int y){
        boolean status = false;

        if (x >= 11 || x < 0) {
            System.out.println(" Deine Eingabe (x) ist nicht mehr in den Grenzen des Spielfeldes.");
            return status;
        }
        if (y >= 11 || y < 0) {
            System.out.println(" Deine Eingabe (y) ist nicht mehr in den Grenzen des Spielfeldes.");
            return status;
        }
        status = true;
        return status;
    }
}