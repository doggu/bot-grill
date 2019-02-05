package events.gameroom.ticTacToe;

import events.commands.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class Game extends Command {
    private final ArrayList<User> players;
    private final MessageChannel channel;
    private User activePlayer;
    private User inactivePlayer;
    private char[] board = {
            ' ', ' ', ' ',
            ' ', ' ', ' ',
            ' ', ' ', ' ',
    };
    private int turns = 0;





    Game(ArrayList<User> players, MessageChannel channel) {
        this.players = players;
        this.channel = channel;
        activePlayer = players.get(0);
        inactivePlayer = players.get(1);
        startGame();
    }



    private void startGame() {
        sendMessage(activePlayer.getName()+" starts!\n"+printBoard());
    }
    private void playMove() {
        String index = args[1];
        int x = index.charAt(0)-'a', y = index.charAt(1)-'1';
        //if both coordinates are within bounds
        try {
            //host is X; player 2 is O
            int boardPos = x*3+y;
            if (board[boardPos]!=' ') {
                sendMessage("you can't mark a spot that's already been marked!");
                return;
            } else {
                board[boardPos] = (players.indexOf(activePlayer) == 0) ? 'X' : 'O';
            }
        } catch (IndexOutOfBoundsException oor) {
            sendMessage("incorrect format! please try again.");
            return;
        }

        sendMessage(printBoard());
        turns++;
        if (hasWon()) {
            sendMessage(activePlayer.getName()+" won!");
            e.getJDA().removeEventListener(this);
            return;
        } else if (turns==9){
            sendMessage("it's a draw!");
            //TODO: create a command for this line below and implement it throughout
            e.getJDA().removeEventListener(this);
            return;
            //continue
        }

        //swap active player and change turns
        User temp = activePlayer;
        activePlayer = inactivePlayer;
        inactivePlayer = temp;
        sendMessage(activePlayer.getName()+"\'s turn!");
    }
    private boolean hasWon() {
        for (int i=0; i<3; i++) {
            //if there's a row
            if (board[3*i]==board[1+3*i]&&board[3*i]==board[2+3*i])
                if (board[3*i]!=' ')
                    return true;
            //if there's a column
            if (board[i]==board[3+i]&&board[i]==board[6+i])
                if (board[i]!=' ')
                    return true;
        }
        //if there's an increasing diagonal
        if (board[2]==board[4]&&board[2]==board[6])
            if (board[2]!=' ')
                return true;
        //if there's a decreasing diagonal
        if (board[0]==board[4]&&board[0]==board[8])
            if (board[0]!=' ') //iF sTaTEMenT cAn bE SimPLiFiEd
                return true;
        return false;
    }

    private String printBoard() {
        return "```\n" +
                "   |   |   \n"+
                " "+board[0]+" | "+board[1]+" | "+board[2]+" \n"+
                "---+---+---\n"+
                " "+board[3]+" | "+board[4]+" | "+board[5]+" \n"+
                "---+---+---\n"+
                " "+board[6]+" | "+board[7]+" | "+board[8]+" \n"+
                "   |   |   \n"+
                "```";
    }





    @Override
    public void onCommand() {
        //oh fuck i forgot this exists
    }

    @Override
    public boolean isCommand() {
        if (!players.contains(e.getAuthor())) return false;
        if (!e.getChannel().equals(channel)) return false;
        if (args.length==0) return false;
        switch(args[0]) {
            case "quit":
                sendMessage(e.getAuthor().getName()+" forfeits!");
                e.getJDA().removeEventListener(this);
                return false;
            case "mark":
                if (!e.getAuthor().equals(activePlayer))
                    return false;
                if (args.length>1) {
                    if (args[1].length() != 2) {
                        sendMessage("incorrect format! please indicate row and column as two conjoined characters" +
                                "(e.x. \"b2\"");
                        return false;
                    } else playMove();
                }
                else {
                    sendMessage("please choose a location to mark!");
                    return false;
                }
            default:
                return false;
        }
    }

    @Override
    protected Message sendMessage(String message) {
        return channel.sendMessage(message).complete();
    }
}
