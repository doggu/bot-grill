package events.gameroom.ticTacToe;

import events.gameroom.TextGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Game extends TextGame {
    private User activePlayer;
    private User inactivePlayer;
    private char[] board = {
            ' ', ' ', ' ',
            ' ', ' ', ' ',
            ' ', ' ', ' ',
    };
    private int turns = 0;




    public Game(ArrayList<User> players, MessageChannel channel) {
        super(players, channel);
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
                sendMessage("you can't mark a spot " +
                        "that's already been marked!");
                return;
            } else {
                board[boardPos] =
                        (players.indexOf(activePlayer) == 0) ? 'X' : 'O';
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
            this.commitSuicide();
            return;
        }

        //swap active player and change turns
        User temp = activePlayer;
        activePlayer = inactivePlayer;
        inactivePlayer = temp;
        sendMessage(activePlayer.getName()+"'s turn!");
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
                "    |   |   \n"+
                "3 "+board[2]+" | "+board[5]+" | "+board[8]+" \n"+
                " ---+---+---\n"+
                "2 "+board[1]+" | "+board[4]+" | "+board[7]+" \n"+
                " ---+---+---\n"+
                "1 "+board[0]+" | "+board[3]+" | "+board[6]+" \n"+
                "    |   |   \n"+
                "  a   b   c \n"+
                "```";
    }

    /*
       |   |    |    |   |    |    |   |
    ---+---+--- | ---+---+--- | ---+---+---
       |   |    |    |   |    |    |   |
    ---+---+--- | ---+---+--- | ---+---+---
       |   |    |    |   |    |    |   |
    ------------+-------------+------------
       |   |    |    |   |    |    |   |
    ---+---+--- | ---+---+--- | ---+---+---
       |   |    |    |   |    |    |   |
    ---+---+--- | ---+---+--- | ---+---+---
       |   |    |    |   |    |    |   |
    ------------+-------------+------------
       |   |    |    |   |    |    |   |
    ---+---+--- | ---+---+--- | ---+---+---
       |   |    |    |   |    |    |   |
    ---+---+--- | ---+---+--- | ---+---+---
       |   |    |    |   |    |    |   |
     */





    @Override
    public void onCommand() {
        switch(args[0]) {
            case "quit":
                sendMessage(activePlayer+" forfeits!");
                e.getJDA().removeEventListener(this);
                return;
            case "mark":
                if (args.length>1) {
                    if (args[1].length() != 2) {
                        sendMessage("incorrect format! " +
                                "please indicate row and column " +
                                "as two conjoined characters " +
                                "(e.x. \"b2\")");
                        return;
                    } else playMove();
                } else {
                    sendMessage("please choose a location to mark!");
                    return;
                }
            default:
                //it aint for me bro
        }
    }

    @Override
    public boolean isCommand() {
        if (!e.getChannel().equals(channel)) return false;
        if (!e.getAuthor().equals(activePlayer)) return false;
        if (args.length==0) return false;

        return true;
    }
}
