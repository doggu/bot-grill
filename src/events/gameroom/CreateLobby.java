package events.gameroom;

import events.commands.Command;
import events.gameroom.ticTacToe.Game;
import events.gameroom.ticTacToe.TicTacToe;

public class CreateLobby extends Command {
    @Override
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("CreateLobby");
    }

    @Override
    public void onCommand() {
        if (args.length==1) {
            sendMessage("incorrect format! please indicate which game you would like to play.\n" +
                    "* format: \"?CreateLobby [game]\"");
            log("invalid lobby creation format");
            return;
        }

        Lobby lobby;

        switch (args[1].toLowerCase()) {
            /*
            case "map":
                lobby = new Map();
                break;
            */
            case "tic-tac-toe":
                lobby = new TicTacToe(e.getAuthor(), e.getChannel());
                break;
            default:
                sendMessage("i don't recognize this game. sorry!");
                log(e.getAuthor().getName()+" attempted to play unknown game \""+args[2]+"\"");
                return;
        }

        e.getJDA().addEventListener(lobby);
    }
}
