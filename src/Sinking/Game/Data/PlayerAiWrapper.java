package Sinking.Game.Data;

import Sinking.AiLoader;
import Sinking.IAi;
import Sinking.common.Tupel;

public class PlayerAiWrapper extends Player {
    private IAi aiBackbone;

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

    public void nextMove(Board board) {
        Tupel<Integer, Integer> coordinates = this.aiBackbone.nextMove(board);
        this.shoot(coordinates._1(), coordinates._2(), board);
    }
}
