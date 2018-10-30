package events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.OffsetDateTime;

public class DevTools extends ListenerAdapter {
    private void onCommand() {
        sendMessage("aha");
        log("performed debugging command");
    }
    private boolean isCommand() {
        return (args[0].equals("&debug"));
    }

    private MessageReceivedEvent e;
    private String[] args;

    public void onMessageReceived(MessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        if (e.getAuthor().isBot()) return;
        if (message.length()==0) return;
        this.e = e;
        this.args = message.substring(1).split(" ");
        if (isCommand())
            onCommand();
    }

    private void sendMessage(String message) {
        e.getChannel().sendMessage(message).queue();
    }
    private void log(String message) {
        OffsetDateTime g = e.getMessage().getCreationTime().minusHours(7);
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

    //probably not going to come in handy here
    private void addReaction(Emote emote) { e.getMessage().addReaction(emote).queue(); }
}
