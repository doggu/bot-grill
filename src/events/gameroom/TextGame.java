package events.gameroom;

import events.MessageListener;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.AttachmentOption;

import java.io.File;
import java.util.ArrayList;

public abstract class TextGame extends MessageListener {
    protected int activePlayer = 0;
    protected final ArrayList<User> players;
    protected final MessageChannel channel;


    public TextGame(ArrayList<User> players, MessageChannel channel) {
        this.players = players;
        this.channel = channel;
    }


    public abstract boolean isCommand();
    public abstract void onCommand();

    //TODO: may want to change this later (and rework this whole thing entirely)
    public char getPrefix() { return '?'; }

    @Override
    protected Message sendMessage(String message) {
        return channel.sendMessage(message).complete();
    }

    protected Message sendFile(File file, String message) {
        return channel.sendMessage(new MessageBuilder(message).build())
                .addFile(file, (AttachmentOption) null).complete();
    }
}
