package events.gameroom.flow;

import events.gameroom.Lobby;
import events.gameroom.TextGame;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Flow extends Lobby {
    public Flow(User author, MessageChannel channel) {
        super(author, channel);
    }



    public int getMinPlayers() { return 1; }
    public int getMaxPlayers() { return 1; }
    public String getName() { return "Flow"; }
    public TextGame getGame() { return new Game(players, channel); }
}
