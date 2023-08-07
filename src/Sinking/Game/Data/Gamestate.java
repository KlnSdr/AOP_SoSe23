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

    public void intro(){
        String name;
        System.out.println("Spieler 1 gib bitte deinen Namen ein");
        name = eingabe.next();
        spieler1.setName(name);
        System.out.println("Spieler 2 gib bitte deinen Namen ein");
        name = eingabe.next();
        spieler2.setName(name);
    }

    public void order(){
        int x = 0;
        int y = 0;
        System.out.println( spieler1 + ":" + spieler1.getHitEnemyShips());
        System.out.println(spieler2 + ":" + spieler2.getHitEnemyShips());
        if (move%2 == 0){
            System.out.println(spieler1+ " schieß und Sieg");
            y = inputY(y);
            x = inputX(x);
            spieler1.shoot(x,y, gameboardSpieler2);
            move++;
        }
        else{
            System.out.println(spieler2+ " schieß und Sieg");
            y = inputY(y);
            x = inputX(x);
            spieler2.shoot(x,y, gameboardSpieler2);
            move++;
        }
    }

    //just testing
    public void setShips(){
        int x = 0;
        int y = 0;
        
        System.out.println(spieler1+ " setze bitte dein Schiff");
        y = inputY(y);
        x = inputX(x);
        gameboardSpieler1.setShip(x,y);
        gameboardSpieler2.setShip(x,y);
        gameboardSpieler1.setShip(x+1,y);
        gameboardSpieler2.setShip(x+1,y);
    }

    public Player playthrough(){
        intro();
        setShips();
        while (winner() == false){
            order();
        }
        return winner;
    }

    public int inputY(int y){
       do {
           y = eingabe.next().charAt(0);
           if(!(y >= 'A' && y <= 'Z') && !(y >= 'a' && y <= 'z')){
               System.out.println("Falsche Eingabe");
           }
       }while  (!(y >= 'A' && y <= 'Z') && !(y >= 'a' && y <= 'z'));
        y = Character.toUpperCase(y) - 65;
        return y;
    }

    public int inputX(int x){
        do{
            x= eingabe.nextInt() -1;
            if(!(x >=11 ) && !(x < 0)){
                System.out.println("Falsche Eingabe");
            }
        }while(!(x < 11 && x > 0));

        return x;
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