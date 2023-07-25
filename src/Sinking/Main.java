package Sinking;

import Sinking.UI.ViewLoader;
import Sinking.http.Json;

public class Main {
    public static void main(String[] args) {
        ViewLoader.getInstance().loadView("MainMenu");
        Json config = ArgsLoader.load(args);
        System.out.println(config);
    }
}
