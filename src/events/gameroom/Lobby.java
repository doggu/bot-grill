package events.gameroom;

import events.ReactionListener;
import main.BotMain;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public abstract class Lobby extends ReactionListener {
    protected final User host;
    protected final MessageChannel channel;
    protected ArrayList<User> players = new ArrayList<>();
    protected Message joinMessage;

    public Lobby(User author, MessageChannel channel) {
        this.host = author;
        this.channel = channel;
        players.add(host);
        if (getMaxPlayers()==1)
            startGame();
        else {
            joinMessage = sendMessage(
                    "join " + host.getName() + " for a game of " + getName() + "!\n" +
                            "current players: " + players.size() + "/" + getMaxPlayers());
            joinMessage.addReaction("✅").complete();
            checkLobbyReady();
        }
    }



    private void checkLobbyReady() {
        if (players.size()>getMaxPlayers()) {
            sendMessage("something went wrong, please start a new lobby.");
            BotMain.bot_grill.removeEventListener(this);
        } else if (players.size()>=getMinPlayers()) {
            //the game could start if the host wants it to
            // ( i'll probably never program this since there're only two real users)
        } else if (players.size()==getMaxPlayers()) {
            startGame();
        }
    }

    private void startGame() {
        sendMessage("a game of "+getName()+" begins!");
        TextGame game = getGame();
        BotMain.removeListener(this);
        BotMain.addListener(game);
    }




    public boolean isCommand() {
        if (!e.getReactionEmote().toString().equals("RE:✅(null)")) {
            System.out.println(e.getReactionEmote().toString());
            return false;
        }

        //if it's this bot; this kinda sucks (what if other bots want to play?!)
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
                "join "+host.getName()+" for a game of "+getName()+"\n" +
                        "current players: "+players.size()+"/"+getMaxPlayers()).queue();
        checkLobbyReady();
    }

    public Message sendMessage(String message) { return channel.sendMessage(message).complete(); }



    public abstract int getMinPlayers();
    public abstract int getMaxPlayers();
    public abstract String getName();
    public abstract TextGame getGame();
}
