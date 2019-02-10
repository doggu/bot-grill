package events.gameroom;

import events.ReactionListener;
import events.commands.Command;
import events.gameroom.ticTacToe.Game;
import main.BotMain;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public abstract class Lobby extends ReactionListener {
    protected final User host;
    protected final MessageChannel channel;
    protected ArrayList<User> players = new ArrayList<>();
    protected final Message joinMessage;

    public Lobby(User author, MessageChannel channel) {
        this.host = author;
        this.channel = channel;
        players.add(host);
        String message =
                "join "+host.getName()+" for a game of "+getName()+"\n" +
                        "current players: 1/2";
        joinMessage = channel.sendMessage(message).complete();
        joinMessage.addReaction("✅").complete();
        checkLobbyReady();
    }
    public boolean isCommand() {
        if (!e.getReactionEmote().toString().equals("RE:✅(null)")) {
            System.out.println(e.getReactionEmote().toString());
            return false;
        }

        //if it's this bot; this kinda sucks (what if other bots want to play!?)
        if (e.getUser().isBot()) return false;
        //joined players (namely host, who doesn't have to react) cannot join twice
        if (players.contains(e.getUser())) return false;
        //yea i know this can be simplified shut fukc up i'm drafting
        if (!e.getMessageId().equals(joinMessage.getId())) return false;

        return true;
    }

    @Override
    public void onCommand() {
        players.add(e.getUser());
        e.getChannel().getMessageById(e.getMessageId()).complete().editMessage(
                "join "+host.getName()+" for a game of tic-tac-toe!\n" +
                        "current players: "+players.size()+"/"+getMaxPlayers()).queue();
        checkLobbyReady();
    }



    private void checkLobbyReady() {
        if (players.size()>=getMinPlayers()) {
            //the game could start if the host wants it to (idk if i'll ever program this since there're only two real users)
        }
        if (players.size()>getMaxPlayers()) {
            sendMessage("something went wrong");
            BotMain.bot_grill.removeEventListener(this);
        }
        if (players.size()==getMaxPlayers()) {
            sendMessage("a game of "+getName()+" begins!");
            Command game = getGame();
            BotMain.bot_grill.addEventListener(game);
            BotMain.bot_grill.removeEventListener(this);
        }
    }



    public Message sendMessage(String message) { return channel.sendMessage(message).complete(); }



    public abstract int getMinPlayers();
    public abstract int getMaxPlayers();
    public abstract String getName();
    public abstract Command getGame();
}
