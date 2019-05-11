package events.gameroom.ticTacToe;

import events.commands.Command;
import events.gameroom.Lobby;
import events.gameroom.TextGame;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class TicTacToe extends Lobby {
    public TicTacToe(User host, MessageChannel channel) {
        super(host, channel);
    }

    public int getMinPlayers() {
        return 2;
    }
    public int getMaxPlayers() {
        return 2;
    }
    public String getName() { return "tic-tac-toe"; }
    public TextGame getGame() {
        return new Game(players, e.getChannel());
    }
}