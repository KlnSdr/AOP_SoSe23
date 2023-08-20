package Sinking.Game.Data;

public class Gamestate {
    Player spieler1;
    Player spieler2;

    Player winner;

    Board gameboardSpieler1 = new Board();

    public Board getGameboardSpieler2() {
        return gameboardSpieler2;
    }

    Board gameboardSpieler2 = new Board();
    int move = 0;

    public Gamestate(Player s1, Player s2) {
        this.spieler1 = s1;
        this.spieler2 = s2;
    }

    public Board getspecificBoard(Player spieler) {
        if (spieler.equals(spieler1)) {
            return gameboardSpieler1;
        } else if (spieler.equals(spieler2)) {
            return gameboardSpieler2;
        }
        return null;
    }

    public Player sequence() {
        if (move % 2 == 0) {
            move++;
            return spieler1;
        } else {
            move++;
            return spieler2;
        }
    }

    public boolean isNext(Player player) {
        // move check inverse to what is checked in sequence because the move count is incremented *after* the check,
        // so at this point right now it is offset by one
        return (player.equals(spieler1) && move % 2 == 1) || (player.equals(spieler2) && move % 2 == 0);
    }

    public boolean ishit(int x, int y, Board gameboard) {
        return gameboard.hit(x, y);
    }

    public boolean hasWinner() {
        if (spieler1 == null || spieler2 == null) {
            return false;
        }
        if (spieler1.getHitEnemyShips() == 30 || spieler2.getHitEnemyShips() == 30) {
            if (spieler1.getHitEnemyShips() == 30) {
                winner = spieler1;
            } else {
                winner = spieler2;
            }
            return true;
        }
        return false;
    }

    public Player getWinner() {
        return winner;
    }
}
