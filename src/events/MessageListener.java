package events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

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
        if (isCommand())
            onCommand();
    }



    //prolly wanna avoid using this first one for habitual reasons
    protected Message sendMessage(StringBuilder message) { return sendMessage(message.toString()); }
    protected Message sendMessage(String message) {
        return e.getChannel().sendMessage(message).complete();
    }

    protected void addReaction(Emote emote) {
        e.getMessage().addReaction(emote).queue();
    }



    protected void log(String message) {
        OffsetDateTime g = OffsetDateTime.now();
        int hour = g.getHour(),
            minute = g.getMinute(),
            second = g.getSecond(),
            month = g.getMonthValue(),
            day = g.getDayOfMonth(),
            year = g.getYear();
        System.out.println(
                        (Math.log10(hour)<1?"0":"")+hour+":"+
                        (Math.log10(minute)<1?"0":"")+minute+":"+
                        (Math.log10(second)<1?"0":"")+second+" "+
                        (Math.log10(month)<1?"0":"")+month+"-"+
                        (Math.log10(day)<1?"0":"")+day+"-"+
                        (Math.log10(year)<1?"0":"")+year+": "+
                        message);
    }
}
