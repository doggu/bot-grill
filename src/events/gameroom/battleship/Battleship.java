package events.gameroom.battleship;

import events.commands.Command;
import events.gameroom.Lobby;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Battleship extends Lobby {
    public Battleship(User author, MessageChannel channel) {
        super(author, channel);
    }



    public int getMinPlayers() { return 2; }
    public int getMaxPlayers() { return 2; }
    public String getName() { return "Battleship"; }
    public Command getGame() { return new Game(players, channel); }
}
