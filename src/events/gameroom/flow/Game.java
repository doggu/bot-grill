package events.gameroom.flow;

import events.commands.Command;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Game extends Command {
    private final User player;
    private final MessageChannel channel;



    public Game(User player, MessageChannel channel) {
        this.player = player;
        this.channel = channel;
    }



    public boolean isCommand() {
        if (!e.getAuthor().equals(player)) return false;
        if (!e.getChannel().equals(channel)) return false;
        if (args.length!=3) {
            sendMessage("incorrect format. please try again!");
            return false;
        }
        return args[0].equals("draw");
    }

    public void onCommand() {
        String start = args[1];
        String dx = args[2];


    }
}
