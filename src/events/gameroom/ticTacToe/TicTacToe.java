package events.gameroom.ticTacToe;

import events.ReactionListener;
import events.gameroom.Gameroom;
import events.gameroom.Lobby;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class TicTacToe extends ReactionListener implements Lobby {
    private final User host;
    private ArrayList<User> players = new ArrayList<>();
    private final Message joinMessage;



    public TicTacToe(User host, MessageChannel channel) {
        this.host = host;
        players.add(host);
        String message =
                "join "+host.getName()+" for a game of tic-tac-toe!\n" +
                "current players: 1/2";
        joinMessage = channel.sendMessage(message).complete();
        joinMessage.addReaction("✅").complete();
    }



    @Override
    public boolean isCommand() {
        if (!e.getReactionEmote().toString().equals("RE:✅(null)")) {
            System.out.println(e.getReactionEmote().toString());
            return false;
        }

        //if it's this bot, this kinda sucks (what if other bots want to play!?)
        if (e.getUser().isBot()) return false;
        //joined players (namely host, who doesn't have to react) cannot join twice
        if (players.contains(e.getUser())) return false;
        //yea i know this can be simplified shut fukc up i'm drafting
        if (!e.getMessageId().equals(joinMessage.getId()))
            return false;

        return true;
    }

    @Override
    public void onCommand() {
        players.add(e.getUser());
        e.getChannel().getMessageById(e.getMessageId()).complete().editMessage(
                "join "+host.getName()+" for a game of tic-tac-toe!\n" +
                        "current players: "+players.size()+"/2").queue();
        if (players.size()>=getMinPlayers()) {
            //the game could start if the host wants it to (idk if i'll ever program this since there's only two of us)
        }
        if (players.size()>getMaxPlayers()) {
            sendMessage("something went wrong");
            e.getJDA().removeEventListener(this);
        }
        if (players.size()==getMaxPlayers()) {
            sendMessage("a game of tic-tac-toe begins!");
            Game game = new Game(players, e.getChannel());
            e.getJDA().addEventListener(game);
            e.getJDA().removeEventListener(this);
        }
    }

    @Override
    public int getMinPlayers() {
        return 2;
    }

    @Override
    public int getMaxPlayers() {
        return 2;
    }

}