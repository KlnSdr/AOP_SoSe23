package Sinking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("How are you, world?");

        Scanner scanner = new Scanner(System.in);
        System.out.print("How old are you? ");
        int age = scanner.nextInt();
        System.out.println(18 - age + " years left until you are 18.");
    }
}
