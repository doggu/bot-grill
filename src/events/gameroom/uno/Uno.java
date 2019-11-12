package events.gameroom.uno;

import events.gameroom.Lobby;
import events.gameroom.TextGame;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Uno extends Lobby {
    public Uno(User author, MessageChannel channel) {
        super(author, channel);
    }

    @Override
    public int getMinPlayers() {
        return 2;
    }

    @Override
    public int getMaxPlayers() {
        return 2; //todo: i need to have lobby-owner initiation
    }

    @Override
    public String getName() {
        return "Uno";
    }

    @Override
    public TextGame getGame() {
        return new Game(players, channel);
    }
}
