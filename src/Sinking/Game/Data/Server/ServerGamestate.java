package Sinking.Game.Data.Server;

import Sinking.Game.Data.Gamestate;
import Sinking.Game.Data.Player;

import java.util.Optional;
import java.util.UUID;

public class ServerGamestate {
    private Gamestate game;
    private ServerPlayerWrapper playerA;
    private ServerPlayerWrapper playerB;

    public Gamestate getGame() {
        return game;
    }

    public boolean needsPlayer() {
        return playerA == null || playerB == null;
    }

    public String addPlayer(Player player) {
        String token = generateAccessToken();
        if (playerA == null) {
            playerA = new ServerPlayerWrapper();
            playerA.setPlayer(player);
            playerA.setAccessToken(token);
        } else {
            playerB = new ServerPlayerWrapper();
            playerB.setPlayer(player);
            playerB.setAccessToken(token);
        }
        if (playerA != null && playerB != null) {
            this.game = new Gamestate(playerA.getPlayer(), playerB.getPlayer());
        }
        return token;
    }

    private String generateAccessToken() {
        return (UUID.randomUUID() + UUID.randomUUID().toString()).replaceAll("-", "");
    }

    public Optional<Player> getPlayerByToken(String token) {
        if (playerA != null && playerA.getAccessToken().equals(token)) {
            return Optional.of(playerA.getPlayer());
        } else if (playerB != null && playerB.getAccessToken().equals(token)) {
            return Optional.of(playerB.getPlayer());
        }
        return Optional.empty();
    }

    public Optional<Player> getOpponentByToken(String token) {
        if (playerA != null && !playerA.getAccessToken().equals(token) && playerB != null && playerB.getAccessToken().equals(token)) {
            return Optional.of(playerA.getPlayer());
        } else if (playerB != null && !playerB.getAccessToken().equals(token) && playerA != null && playerA.getAccessToken().equals(token)) {
            return Optional.of(playerB.getPlayer());
        }
        return Optional.empty();
    }
}
