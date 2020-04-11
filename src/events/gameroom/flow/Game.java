package events.gameroom.flow;

import events.gameroom.TextGame;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.ArrayList;

public class Game extends TextGame {

    private final Board board = new Board();



    public Game(ArrayList<User> player, MessageChannel channel) {
        super(player, channel);
        channel.sendFile(board.printBoard()).complete();
    }



    public boolean isCommand() {
        if (!e.getAuthor().equals(players.get(0))) return false;
        if (!e.getChannel().equals(channel)) return false;
        if (args[0].equalsIgnoreCase("quit")) {
            commitSuicide();
            return false;
        }
        if (args[0].equalsIgnoreCase("undo")) {
            try {
                board.undo();
            } catch (IndexOutOfBoundsException f) {
                sendMessage("you have no actions to undo!");
                return false;
            }
            sendFile(board.printBoard());
            return false;
        }
        if (args[0].equalsIgnoreCase("clear")||
                args[0].equalsIgnoreCase("restart")) {
            try {
                board.clear();
            } catch (NullPointerException f) {
                sendMessage("you have no actions to clear!");
                return false;
            }
            sendFile(board.printBoard());
            return false;
        }


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
