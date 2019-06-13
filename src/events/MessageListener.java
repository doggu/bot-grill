package events;

import main.BotMain;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.time.OffsetDateTime;

public abstract class MessageListener extends ListenerAdapter {
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



    //prolly wanna avoid using this first one for habitual reasons
    protected Message sendMessage(Message message) { return e.getChannel().sendMessage(message).complete(); }
    protected Message sendMessage(MessageEmbed message) { return e.getChannel().sendMessage(message).complete(); }
    protected Message sendMessage(String message) {
        return e.getChannel().sendMessage(message).complete();
    }
    protected Message sendMessage(StringBuilder message) { return sendMessage(message.toString()); }
    protected Message sendMessage(double message) {
        return sendMessage(String.valueOf(message));
    }
    protected Message sendMessage(char message) {
        return sendMessage(String.valueOf(message));
    }
    protected Message sendMessage(boolean message) { return sendMessage(String.valueOf(message)); }

    protected Message sendFile(File file) { return e.getChannel().sendFile(file).complete(); }

    protected void addReaction(Emote emote) {
        e.getMessage().addReaction(emote).queue();
    }

    protected void commitSuicide() {
        BotMain.removeListener(this);
    }



    protected void log(String message) {
        OffsetDateTime g = OffsetDateTime.now();
        int hour = g.getHour(),
            minute = g.getMinute(),
            second = g.getSecond(),
            month = g.getMonthValue(),
            day = g.getDayOfMonth(),
            year = g.getYear();
        String timestamp = (Math.log10(hour)<1?"0":"")+hour+":"+
                (Math.log10(minute)<1?"0":"")+minute+":"+
                (Math.log10(second)<1?"0":"")+second+" "+
                (Math.log10(month)<1?"0":"")+month+"-"+
                (Math.log10(day)<1?"0":"")+day+"-"+
                (Math.log10(year)<1?"0":"")+year+": ";
        message = message.replace("\n", "\n\t\t\t\t\t ");
        System.out.println(timestamp+message);
    }
}
