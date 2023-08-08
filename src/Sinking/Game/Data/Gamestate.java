package Sinking.Game.Data;

import java.util.Scanner;

public class Gamestate {
    Scanner eingabe = new Scanner(System.in);
    Player spieler1 = new Player();
    Player spieler2 = new Player();
    
    Player winner;

    Board gameboardSpieler1 = new Board();


    Board gameboardSpieler2 = new Board();
    int move = 0;


    public Board getGameboardSpieler1() {
        return gameboardSpieler1;
    }

    public void setGameboardSpieler1(Board gameboardSpieler1) {
        this.gameboardSpieler1 = gameboardSpieler1;
    }

    public Board getGameboardSpieler2() {
        return gameboardSpieler2;
    }

    public void setGameboardSpieler2(Board gameboardSpieler2) {
        this.gameboardSpieler2 = gameboardSpieler2;
    }

    public Player order(){
        if (move%2 == 0){
            move++;
            return spieler1;
        }
        else{
            move++;
            return spieler2;
        }
    }

    public boolean ishit( int x, int y, Board gameboard){
        if(gameboard.hit(x,y)){
            return true;
        }else{
            return false;
        }
    }

    public boolean winner(){
        
        if(spieler1.getHitEnemyShips() == 2|| spieler2.getHitEnemyShips() == 2 ){
            if(spieler1.getHitEnemyShips() == 2){
                winner = spieler1;
            }
            else{
                winner = spieler2;
            }
            return true;
        }
        return false;
    }
    
    public void ausgabe(){
        System.out.println(winner);
    }


}