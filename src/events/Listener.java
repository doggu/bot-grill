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
                (hour/10>0?"":"0")+hour+":"+
                (minute/10>0?"":"0")+minute+":"+
                (second/10>0?"":"0")+second+" "+
                (month/10>0?"":"0")+month+"-"+
                (day/10>0?"":"0")+day+"-"+
                (year/10>0?"":"0")+year+": "+
                log);
    }
}
