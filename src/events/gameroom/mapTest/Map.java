package events.gameroom.mapTest;

import events.commands.Command;
import events.gameroom.Lobby;
import events.gameroom.TextGame;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Map extends Lobby {
    public Map(User author, MessageChannel channel) {
        super(author, channel);

    }



    public int getMinPlayers() { return 1; }
    public int getMaxPlayers() { return 1; } //TODO: placeholder while i figure out how to write early lobby start
    public String getName() { return "mapTest"; }
    public TextGame getGame() { return new Game(players, channel); }
}
