package Sinking.Game.Data;

public class Player {
    private final String name;
    private int hitEnemyShips = 0;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHitEnemyShips() {
        return hitEnemyShips;
    }

    public boolean shoot(int x, int y) {
        // todo call the board's shoot method
        boolean didHitShip = false;
        if (didHitShip) {
            hitEnemyShips++;
        }
        return didHitShip;
    }
}
