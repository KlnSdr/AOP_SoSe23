package Sinking.Game.Data;

public class Player {
    private String name;
    private int hitEnemyShips = 0;
    protected boolean isHuman = false;

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
        boolean didHitShip = false;
        gameboard.fire(x,y);
       didHitShip= gameboard.hit(x,y);
        if (didHitShip) {
            hitEnemyShips++;
        }
        return didHitShip;
    }
    @Override
    public String toString(){
        return (name);
    }

    public boolean isHuman() {
        return this.isHuman;
    }
}
