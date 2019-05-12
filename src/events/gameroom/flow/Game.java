package events.gameroom.flow;

import events.gameroom.TextGame;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.Iterator;

public class Game extends TextGame {
    //TODO: this stuff can most likely go in a Game abstract class
    private final User player;
    private final MessageChannel channel;

    private final Board board = new Board();



    public Game(User player, MessageChannel channel) {
        this.player = player;
        this.channel = channel;
        channel.sendFile(board.printBoard()).complete();
    }



    public boolean isCommand() {
        if (!e.getAuthor().equals(player)) return false;
        if (!e.getChannel().equals(channel)) return false;
        if (!args[0].equalsIgnoreCase("draw")) return false;
        if (args.length != 3) {
            sendMessage("incorrect format. please try again!");
            return false;
        }
        return true;
    }

    public void onCommand() {
        Point start;
        try {
            start = getCoords(args[1]);
        } catch (NumberFormatException f) {
            sendMessage("invalid coordinates! please try again.");
            return;
        }
        char[] dx = args[2].toLowerCase().toCharArray();

        boolean completed;
        try {
            completed = board.drawLine(start, dx);
        } catch (NullPointerException|IndexOutOfBoundsException g) {
            if (g instanceof NullPointerException) {
                sendMessage("please start on a dot.");
            } else {
                sendMessage("your path ran into something! please try again.");
            }
            return;
        }

        if (completed)
            sendFile(board.printBoard());
        else
            sendMessage("you did not draw a complete path! please try again.");

        if (board.completed()) {
            sendMessage("you win!");
            commitSuicide();
        }
    }


    private Point getCoords(String p) throws NumberFormatException {
        char c = p.charAt(0);
        int r = Integer.parseInt(p.substring(1));
        return new Point(board.getDimensions().x-r,c-'a');
    }
}
