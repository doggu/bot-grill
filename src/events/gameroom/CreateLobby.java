package events.gameroom;

import events.commands.Command;
import events.gameroom.battleship.Battleship;
import events.gameroom.flow.Flow;
import events.gameroom.mapTest.Map;
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
            case "map":
                lobby = new Map(e.getAuthor(), e.getChannel());
                break;
            case "tic-tac-toe":
                lobby = new TicTacToe(e.getAuthor(), e.getChannel());
                break;
            case "battleship":
                lobby = new Battleship(e.getAuthor(), e.getChannel());
                break;
            case "flow":
                lobby = new Flow(e.getAuthor(), e.getChannel());
                break;
            default:
                sendMessage("i don't recognize this game. sorry!");
                log(e.getAuthor().getName()+" attempted to play unknown game \""+args[1]+"\"");
                return;
        }

        e.getJDA().addEventListener(lobby);
    }



    public String getName() { return "CreateLobby"; }
    public String getDescription() { return "Play games with other users!"; }
    public String getFullDescription() {
        //TODO: write another description
        return "i got better stuff to do so ima write this later, sorry";
    }
}
