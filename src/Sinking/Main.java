package Sinking;

import Sinking.UI.ViewLoader;

public class Main {
    public static void main(String[] args) {
        System.out.println("How are you, world?");
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
        ViewLoader.getInstance().loadView("Example1");
    }
}