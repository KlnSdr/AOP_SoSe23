package Sinking.Game.Data.Server;

import Sinking.Game.Data.Gamestate;
import Sinking.Game.Data.Player;
import Sinking.Game.Data.Server.Exceptions.GameFinishedException;
import Sinking.Game.Data.Server.Exceptions.GameNotFoundException;
import Sinking.Game.Data.Server.Exceptions.NoPlayerNeededException;
import Sinking.Game.Data.Server.Exceptions.PlayerNotFoundException;
import Sinking.Game.Data.Tile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * not really a repository because it's not an abstraction of storage, but storage itself
 * but hey who cares about etymologie and design patterns
 */
public class GameRepository {
    private final ConcurrentHashMap<UUID, ServerGamestate> games;

    private GameRepository() {
        this.games = new ConcurrentHashMap<>();
    }

    public static GameRepository getInstance() {
        return Holder.instance;
    }

    public UUID newGame() {
        UUID id = UUID.randomUUID();
        ServerGamestate game = new ServerGamestate();
        this.games.put(id, game);
        return id;
    }

    private Optional<ServerGamestate> get(UUID id) {
        return Optional.of(this.games.get(id));
    }

    public String addPlayer(UUID id, Player player) throws GameNotFoundException, GameFinishedException, NoPlayerNeededException {
        Optional<ServerGamestate> optGame = get(id);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(id);
        }
        ServerGamestate game = optGame.get();
        Gamestate gameState = game.getGame();

        // todo check if gameState was finished using gameState
        boolean gameFinished = false;
        if (gameFinished) {
            throw new GameFinishedException(id);
        }

        if (!game.needsPlayer()) {
            throw new NoPlayerNeededException(id);
        }

        // todo add player to game
        return game.addPlayer(player);
    }

    public boolean fireAt(int x, int y, UUID gameId, String playerToken) throws GameNotFoundException, PlayerNotFoundException {
        Optional<ServerGamestate> optGame = get(gameId);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
        ServerGamestate game = optGame.get();

        Optional<Player> optPlayer = game.getPlayerByToken(playerToken);
        if (optPlayer.isEmpty()) {
            throw new PlayerNotFoundException(gameId, playerToken);
        }

        Player player = optPlayer.get();
        Gamestate gamestate = game.getGame();
        // todo get board for player from gamestate and pass it to player.shoot

        return player.shoot(x, y, null);
    }

    public void deleteGame(UUID id) {
        this.games.remove(id);
    }

    public Tile[][] getBoard(UUID gameId, String playerToken) throws GameNotFoundException, PlayerNotFoundException {
        Optional<ServerGamestate> optGame = get(gameId);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
        ServerGamestate game = optGame.get();

        Optional<Player> optPlayer = game.getPlayerByToken(playerToken);
        if (optPlayer.isEmpty()) {
            throw new PlayerNotFoundException(gameId, playerToken);
        }

        // todo get and return board for player

        return null;
    }
    private static class Holder {
        public static GameRepository instance = new GameRepository();
    }
}
