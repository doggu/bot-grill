package events.gameroom;

import events.commands.Command;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class Game extends Command {
    private final User player;
    private final MessageChannel channel;



    public Game(User player, MessageChannel channel) {
        this.player = player;
        this.channel = channel;
    }
}
