package events.gameroom.battleship;

import events.gameroom.TextGame;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class Game extends TextGame {
    private final ArrayList<User> players;
    private final MessageChannel channel;



    public Game(ArrayList<User> players, MessageChannel channel) {
        this.players = players;
        this.channel = channel;
    }



    public boolean isCommand() {
        if (players.contains(e.getAuthor())) return false;
        if (!e.getChannel().equals(channel)) return false;
        if (args.length!=3) {
            sendMessage("incorrect format. please try again!");
            return false;
        }
        return args[0].equals("draw");
    }

    public void onCommand() {

    }
}
