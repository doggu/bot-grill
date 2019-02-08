package events.gameroom;

import events.ReactionListener;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class Lobby extends ReactionListener {
    private final User author;
    private final MessageChannel channel;

    public Lobby(User author, MessageChannel channel) {
        this.author = author;
        this.channel = channel;
    }

    public abstract int getMinPlayers();
    public abstract int getMaxPlayers();
}
