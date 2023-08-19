package Sinking.Game.Data.Server;

import Sinking.Game.Data.Board;
import Sinking.Game.Data.Gamestate;
import Sinking.Game.Data.Player;
import Sinking.common.Exceptions.*;
import Sinking.Game.Data.Tile;
import Sinking.common.Tupel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * not really a repository because it's not an abstraction of storage, but storage itself,
 * and it's more of a game *controller*
 * but hey who cares about etymologie and design patterns, this sounds way better. fight me!
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
        return Optional.ofNullable(this.games.get(id));
    }

    public String addPlayer(UUID id, Player player) throws GameNotFoundException, GameFinishedException, NoPlayerNeededException {
        Optional<ServerGamestate> optGame = get(id);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(id);
        }
        ServerGamestate game = optGame.get();
        Gamestate gameState = game.getGame();

        if (gameState != null) {
            if (gameState.hasWinner()) {
                throw new GameFinishedException(id);
            }

            if (!game.needsPlayer()) {
                throw new NoPlayerNeededException(id);
            }
        }

        return game.addPlayer(player);
    }

    public boolean fireAt(int x, int y, UUID gameId, String playerToken) throws GameNotFoundException, PlayerNotFoundException, NeedsPlayerException, CoordinatesOutOfBoundsException {
        Optional<ServerGamestate> optGame = get(gameId);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
        ServerGamestate game = optGame.get();

        Optional<Player> optPlayer = game.getPlayerByToken(playerToken);
        Optional<Player> optOpponent = game.getOpponentByToken(playerToken);
        if (optOpponent.isEmpty() || optPlayer.isEmpty()) {
            throw new PlayerNotFoundException(gameId, playerToken);
        }

        Player player = optPlayer.get();
        Player opponent = optOpponent.get();
        Gamestate gamestate = game.getGame();
        if (gamestate == null) {
            throw new NeedsPlayerException(gameId);
        }
        Board gameBoard = gamestate.getspecificBoard(opponent);
        if (gameBoard == null) {
            throw new PlayerNotFoundException(gameId, playerToken);
        }

        return player.shoot(x, y, gameBoard);
    }

    public boolean hasWinner(UUID gameId) throws GameNotFoundException {
        Optional<ServerGamestate> optGame = get(gameId);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
        ServerGamestate game = optGame.get();
        return game.getGame().hasWinner();
    }

    public boolean isPlayerNext(UUID gameId, String playerToken) throws GameNotFoundException, PlayerNotFoundException {
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
        return game.getGame().isNext(player);
    }

    public boolean needsPlayer(UUID gameId) throws GameNotFoundException {
        Optional<ServerGamestate> optGame = get(gameId);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
        ServerGamestate game = optGame.get();
        return game.needsPlayer();
    }

    public void deleteGame(UUID id) {
        this.games.remove(id);
    }

    public Map<String, Tile[][]> getBoard(UUID gameId, String playerToken) throws GameNotFoundException, PlayerNotFoundException, NeedsPlayerException {
        Optional<ServerGamestate> optGame = get(gameId);
        if (optGame.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
        ServerGamestate game = optGame.get();

        Optional<Player> optPlayer = game.getPlayerByToken(playerToken);
        Optional<Player> optOpponent = game.getOpponentByToken(playerToken);
        if (optPlayer.isEmpty() || optOpponent.isEmpty()) {
            throw new PlayerNotFoundException(gameId, playerToken);
        }
        Player player = optPlayer.get();
        Player opponent = optOpponent.get();

        Gamestate gamestate = game.getGame();
        if (gamestate == null) {
            throw new NeedsPlayerException(gameId);
        }
        HashMap<String, Tile[][]> boards = new HashMap<>();
        boards.put("own", gamestate.getspecificBoard(player).getBoard());
        boards.put("opponent", gamestate.getspecificBoard(opponent).getBoard());
        return boards;
    }

    public void setShips(UUID gameId, String playerToken, Tupel<Integer, Integer>[] coordinates) throws GameNotFoundException, PlayerNotFoundException, NeedsPlayerException, InternalError {
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
        if (gamestate == null) {
            throw new NeedsPlayerException(gameId);
        }

        Board board = gamestate.getspecificBoard(player);
        if (board == null) {
            throw new InternalError();
        }

        for (Tupel<Integer, Integer> coordinate : coordinates) {
            board.setShip(coordinate._1(), coordinate._2());
        }
    }

    private static class Holder {
        public static GameRepository instance = new GameRepository();
    }
}
