package events;

import main.BotMain;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.OffsetDateTime;

abstract class Listener extends ListenerAdapter {
    protected void commitSuicide() { BotMain.removeListener(this); }

    protected void log(String log) {
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
                        log);
    }
}
