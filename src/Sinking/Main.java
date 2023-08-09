package Sinking;

import Sinking.Game.Data.Board;
import Sinking.UI.ViewLoader;
import Sinking.common.Tupel;
import Sinking.http.Json;
import Sinking.http.server.HttpRouteLoader;
import Sinking.http.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.setShip(0, 0);
        board.setShip(0, 1);
        board.setShip(0, 2);
        board.setShip(6, 6);
        board.setShip(7, 6);
        board.setShip(8, 6);

        IAi ai = AiLoader.getInstance().getInstanceOf("Admiral Glückskoordinate");

        for (int i = 0; i < 11; i++) {
            Tupel<Integer, Integer> coord = ai.nextMove(board);
            board.fire(coord._1(), coord._2());
        }

        System.exit(0);
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
    }
}
