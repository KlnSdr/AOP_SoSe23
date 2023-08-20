package Sinking;

import Sinking.UI.ViewLoader;
import Sinking.common.Json;
import Sinking.http.server.HttpRouteLoader;
import Sinking.http.server.Server;
import Sinking.http.test.TestRunner;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Json config = ArgsLoader.load(args);
        if (config.get("test").isPresent()) {
            TestRunner.runTests();
            return;
        }

        if (config.get("server").isPresent()) {
            startInServerMode(config);
        } else {
            startInClientMode();
        }
    }

    public static void startInServerMode(Json config) {
        int port;

        if (config.get("port").isPresent()) {
            try {
                port = Integer.parseInt(config.get("port").get());
            } catch (NumberFormatException e) {
                port = 3000;
                System.out.println("[WARN]: port is not a number, using default port 3000");
            }
        } else {
            port = 3000;
            System.out.println("[WARN]: using default port 3000");
        }

        System.out.printf("Starting server on port %d...\n", port);
        try {
            Server server = new Server(port);
            HttpRouteLoader.loadRoutes("Sinking.http.routes", server);
            server.run();
        } catch (IOException e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }
    }

    private static void startInClientMode() {
        ViewLoader.getInstance().loadView("MainMenu");
    }
}
