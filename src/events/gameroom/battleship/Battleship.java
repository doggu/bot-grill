package events.gameroom.battleship;

import events.gameroom.Lobby;
import events.gameroom.TextGame;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Battleship extends Lobby {
    public Battleship(User author, MessageChannel channel) {
        super(author, channel);
    }



    public int getMinPlayers() { return 2; }
    public int getMaxPlayers() { return 2; }
    public String getName() { return "Battleship"; }
    public TextGame getGame() { return new Game(players, channel); }
}
