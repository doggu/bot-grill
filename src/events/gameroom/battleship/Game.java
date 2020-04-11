package events.gameroom.battleship;

import events.gameroom.TextGame;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Game extends TextGame {
    public Game(ArrayList<User> players, MessageChannel channel) {
        super(players, channel);
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
