package events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;

public abstract class MessageListener extends Listener {
    protected MessageReceivedEvent e;
    protected String[] args;



    public abstract void onCommand();
    public abstract boolean isCommand();
    protected abstract char getPrefix();



    public void onMessageReceived(MessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        if (e.getAuthor().isBot()) return;
        if (message.length()==0) return;
        if (message.charAt(0)!=getPrefix()) return;
        this.e = e;
        this.args = message.substring(1).split(" ");
        if (isCommand()) {
            e.getChannel().sendTyping().complete();
            onCommand();
        }
    }



    protected Message sendMessage(Message message) { return e.getChannel().sendMessage(message).complete(); }
    protected Message sendMessage(MessageEmbed message) { return e.getChannel().sendMessage(message).complete(); }
    protected Message sendMessage(String message) {return e.getChannel().sendMessage(message).complete();}
    protected Message sendMessage(StringBuilder message) { return sendMessage(message.toString()); }
    protected Message sendMessage(double message) {return sendMessage(String.valueOf(message));}
    protected Message sendMessage(char message) {return sendMessage(String.valueOf(message));}
    protected Message sendMessage(boolean message) { return sendMessage(String.valueOf(message)); }

    protected Message sendFile(File file) { return e.getChannel().sendFile(file).complete(); }

    protected void addReaction(Emote emote) {
        e.getMessage().addReaction(emote).queue();
    }
}
