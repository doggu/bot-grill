package events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import java.time.OffsetDateTime;

public abstract class ReactionListener extends ListenerAdapter {
    protected MessageReactionAddEvent e;


    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        this.e = event;
        if (isCommand())
            onCommand();
    }

    protected abstract boolean isCommand();
    protected abstract void onCommand();

    protected Message sendMessage(String message) {
        MessageAction g = e.getChannel().sendMessage(message);
        return g.complete();
    }

    void addReaction(Emote emote) {
        e.getReaction()
                .getChannel()
                .getMessageById(e.getMessageId())
                .complete()
                .addReaction(emote)
                .queue();
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
