package Sinking;

import Sinking.UI.ViewLoader;
import Sinking.http.Json;
import Sinking.http.server.HttpRouteLoader;
import Sinking.http.server.Server;

import java.io.IOException;
import Sinking.http.client.Consistency;

public class Main {
    public static void main(String[] args) {
        Json config = ArgsLoader.load(args);

        if (config.get("server").isPresent()) {
            startInServerMode(config);
        } else {
            startInClientMode();
        }
    }

    private static void startInServerMode(Json config) {
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

        System.out.println("Starting server on port " + port + "...");
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

        Consistency.getInstance().checkConnection(() -> {
            System.out.println("success");
        }, () -> {
            System.out.println("failure");
        });
    }
}
