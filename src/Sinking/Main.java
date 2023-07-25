package Sinking;

import Sinking.Game.Data.Board;
import Sinking.UI.ViewLoader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         Scanner eingabe = new Scanner(System.in);

        int y = eingabe.next().charAt(0);
        y = Character.toUpperCase(y) - 65;
        int x = eingabe.nextInt() -1;
       // ViewLoader.getInstance().loadView("Example1");
        Board gameboard = new Board();
        gameboard.printBoard();
        gameboard.setShip(x, y);
        System.out.println("====================================================");
        gameboard.printBoard();
        y = 1;
        x= 1;
        gameboard.fire(x,y);

    }
}
