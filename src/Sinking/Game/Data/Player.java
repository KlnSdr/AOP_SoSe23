package Sinking.Game.Data;

import Sinking.common.Exceptions.CoordinatesOutOfBoundsException;

public class Player {
    protected boolean isHuman = false;
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

    public boolean shoot(int x, int y, Board gameboard) throws CoordinatesOutOfBoundsException {
        boolean didHitShip;
        gameboard.fire(x, y);
        didHitShip = gameboard.hit(x, y);
        if (didHitShip) {
            hitEnemyShips++;
        }
        return didHitShip;
    }

    @Override
    public String toString() {
        return (name);
    }
}
