package events.gameroom.flow;

import events.commands.Command;
import events.gameroom.Lobby;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Flow extends Lobby {
    public Flow(User author, MessageChannel channel) {
        super(author, channel);
    }



    public int getMinPlayers() { return 1; }
    public int getMaxPlayers() { return 1; }
    public String getName() { return "Flow"; }
    public Command getGame() { return new Game(players.get(0), channel); }
}