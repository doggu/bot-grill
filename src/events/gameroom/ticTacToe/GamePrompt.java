package events.gameroom.ticTacToe;

import events.gameroom.Gameroom;
import net.dv8tion.jda.core.entities.Game;

//name subject to change
public class GamePrompt extends Gameroom {



    @Override
    public boolean isCommand() {
        return super.isCommand()&&args[0].equalsIgnoreCase("TicTacToe");
    }

    @Override
    public void onCommand() {

    }
}
