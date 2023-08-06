package Sinking.Game.Data;

public class Player {
    private String name;
    private int hitEnemyShips = 0;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getHitEnemyShips() {
        return hitEnemyShips;
    }

    public boolean shoot(int x, int y, Board gameboard) {
        // todo call the board's shoot method
        boolean didHitShip = false;
       didHitShip= gameboard.fire(x,y);
        if (didHitShip) {
            hitEnemyShips++;
        }
        return didHitShip;
    }
}
