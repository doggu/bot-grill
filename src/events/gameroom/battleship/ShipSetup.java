package events.gameroom.battleship;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.processing.Generated;
import java.util.ArrayList;

public class ShipSetup extends ListenerAdapter {
    Ship[] ships = {
            new Ship("Cruiser", 2),
            new Ship("Destroyer", 3),
            new Ship("Submarine", 3),
            new Ship("Battleship", 4),
            new Ship("Aircraft Carrier", 5),
    };

    private final PrivateChannel dm;
    public ShipSetup(User player) {
        dm = player.openPrivateChannel().complete();
        dm.sendMessage("please set up your ships!\n"+
                printBoardProgress())
                .queue();
    }



    private String printBoardProgress() {
        String board =
                        "   | A | B | C | D | E | F | G | H | I | J |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 1 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 2 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 3 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 4 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 5 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 6 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 7 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 8 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        " 9 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+" +
                        "10 | n | n | n | n | n | n | n | n | n | n |" +
                        "---+---+---+---+---+---+---+---+---+---+---+";

        return board;
    }

    public Board getBoard() throws BoardNotReadyException {
        return new Board();
    }



    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        if (!e.getChannel().equals(dm)) return;

        int x = 3;
    }
}
