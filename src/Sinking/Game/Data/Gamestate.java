package Sinking.Game.Data;

import java.util.Scanner;

public class Gamestate {
    Scanner eingabe = new Scanner(System.in);
    Player spieler1 = new Player();
    Player spieler2 = new Player();
    Board gameboardSpieler1 = new Board();
    Board gameboardSpieler2 = new Board();
    int move = 0;

    public void intro(){
        String name;
        name = eingabe.next();
        spieler1.setName(name);
        name = eingabe.next();
        spieler2.setName(name);
    }

    public void order(){
        int x;
        int y;

        if (move%2 == 0){
            x = eingabe.nextInt();
            y = eingabe.nextInt();
            spieler1.shoot(x,y, gameboardSpieler2);
        }
    }


}