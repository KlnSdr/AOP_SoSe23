package Sinking.Game.Data;

import Sinking.AiLoader;
import Sinking.IAi;
import Sinking.common.Exceptions.CoordinatesOutOfBoundsException;
import Sinking.common.Json;
import Sinking.common.Tupel;
import Sinking.http.client.Client;
import Sinking.http.client.Request;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PlayerAiWrapper extends Player {
    private IAi aiBackbone;
    private Client client;
    private Tupel<Integer, Integer>[] ships;
    private String token;
    private String gameId;

    public PlayerAiWrapper() {
    }

    public PlayerAiWrapper(String aiName) {
        setAi(AiLoader.getInstance().getInstanceOf(aiName));
    }

    public void setAi(IAi ai) {
        this.aiBackbone = ai;
        setName(ai.getName());
        this.isHuman = false;
    }

    public void startParallelRunner(String gameId, String playerToken) {
        this.gameId = gameId;
        this.token = playerToken;

        setupClient();
        this.ships = aiBackbone.setShips();
        submitShipsTimer();
    }

    private void submitShipsTimer() {
        Timer setShipsTimer = new Timer(2000, null);
        ActionListener setShipsListener = e -> {
            if (this.ships == null) {
                setShipsTimer.stop();
                return;
            }
            submitShips(setShipsTimer);
        };
        setShipsTimer.addActionListener(setShipsListener);
        setShipsTimer.setRepeats(true);
        setShipsTimer.start();
    }

    private void submitShips(Timer timer) {
        Request req = client.newRequest("/setShips");
        req.setQuery("id", gameId);
        req.setBody("playerToken", token);
        req.setBody("ships", makeItString(ships));

        client.post(req, res -> {
            if (res.getStatusCode() == 200) {
                timer.stop();
                getBoardTimer();
            }
        });
    }

    private void getBoardTimer() {
        Timer getBoardTimer = new Timer(1000, null);
        ActionListener setShipsListener = e -> {
            getGameBoard(getBoardTimer);
        };
        getBoardTimer.addActionListener(setShipsListener);
        getBoardTimer.setRepeats(true);
        getBoardTimer.start();
    }

    private void getGameBoard(Timer timer) {
        Request req = client.newRequest("/getGamestate");
        req.setQuery("id", gameId);
        req.setBody("playerToken", token);

        client.post(req, res -> {
            System.out.println("[AI]" + res.getBody());
            System.out.println("[AI]" + res.getStatusCode());

            if (res.getStatusCode() == 200) {
                doNextAction(res.getBody(), timer);
            }
        });
    }

    private void doNextAction(Json body, Timer timer) {
        if (body.hasKey("hasWinner") && body.get("hasWinner").orElse("false").equals("true")) {
            timer.stop();
            return;
        }

        if (body.hasKey("isNext") && body.get("isNext").orElse("false").equals("true")) {
            try {
                nextMove();
            } catch (CoordinatesOutOfBoundsException e) {
                // throw exception so AI can rage-quit, very important
                throw new RuntimeException(e);
            }
        }
    }

    private void setupClient() {
        this.client = new Client("http://localhost:13000", 5000);
    }

    public void nextMove() throws CoordinatesOutOfBoundsException {
        Tupel<Integer, Integer> coordinates = this.aiBackbone.nextMove();

        Request req = client.newRequest("/fireAt");
        req.setQuery("id", gameId);
        req.setBody("playerToken", token);
        req.setBody("x", String.valueOf(coordinates._1()));
        req.setBody("y", String.valueOf(coordinates._2()));

        client.post(req, res -> {
            System.out.println("[AI]" + res.getBody());
            System.out.println("[AI]" + res.getStatusCode());
        });
    }

    private String makeItString(Tupel<Integer, Integer>[] ships) {
        String s;
        StringBuilder s1 = new StringBuilder();

        for (Tupel<Integer, Integer> ship : ships) {
            s = String.valueOf(ship._1());
            s1.append(s);
            s1.append(",");
            s = String.valueOf(ship._2());
            s1.append(s);
            s1.append("|");
        }
        return s1.toString();
    }
}
