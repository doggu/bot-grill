package events.gameroom.uno;

import events.gameroom.TextGame;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class Game extends TextGame {
    private int activePlayer = 0;
    private final ArrayList<User> players;
    private final MessageChannel channel;

    private final Deck deck;
    private final ArrayList<Card> pile;



    public Game(ArrayList<User> players, MessageChannel channel) {
        this.players = players;
        this.channel = channel;

        this.deck = new Deck();
        this.pile = new ArrayList<>();

        startGame();
    }



    //game tools
    private void startGame() {
        sendMessage("a ");
    }
    private void nextTurn() {
        activePlayer++;
        activePlayer%= players.size();
    }

    @Override
    public boolean isCommand() {
        return e.getAuthor().equals(players.get(activePlayer));
    }

    @Override
    public void onCommand() {
        switch (args[0]) {
            case "play":

        }
        //should return inside switch case if an invalid move was made

        nextTurn();
        sendMessage(players.get(activePlayer)+"'s turn!");
    }

    @Override
    protected Message sendMessage(String message) {
        return channel.sendMessage(message).complete();
    }
}
