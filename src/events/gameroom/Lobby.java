package events.gameroom;

import events.ReactionListener;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Lobby<T extends Game> extends ReactionListener {
    private final User author;
    private final MessageChannel channel;

    public Lobby(User author, MessageChannel channel) {
        this.author = author;
        this.channel = channel;
    }

    public boolean isCommand() {
        return false;
    }

    public void onCommand() {

    }
}
