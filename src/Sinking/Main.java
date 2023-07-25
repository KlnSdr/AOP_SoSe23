package Sinking;

import Sinking.UI.ViewLoader;
import Sinking.http.Json;

public class Main {
    public static void main(String[] args) {
        Json config = ArgsLoader.load(args);

        if (config.get("server").isPresent()) {
            System.out.println("Server mode");
        } else {
            System.out.println("Client mode");
            ViewLoader.getInstance().loadView("MainMenu");
        }

        System.out.println(config);
    }
}
