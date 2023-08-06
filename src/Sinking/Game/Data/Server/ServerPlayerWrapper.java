package Sinking.Game.Data.Server;

import Sinking.Game.Data.Player;

public class ServerPlayerWrapper {
    private String accessToken;
    private Player player;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
