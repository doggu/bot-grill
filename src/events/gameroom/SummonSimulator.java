package events.gameroom;

import com.sun.scenario.effect.Offset;
import net.dv8tion.jda.core.entities.Message;
import utilities.fehUnits.summoning.Banner;
import utilities.fehUnits.summoning.BannerDatabase;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.List;

public class SummonSimulator extends Gameroom {

    public void onCommand() {
        //"Your circle for: "+[banner title]+"\n"
        //"rates: focus = " +[focus rate]+" 5* = "+[5* rate]

        List<Banner> list = BannerDatabase.getList();
        Banner banner = list.get((int)(Math.random()*list.size())); //temp

        if (args.length>1) {
            StringBuilder bannerName = new StringBuilder();
            for (int i=1; i<args.length; i++) {
                bannerName.append(args[i]);
                if (i+1!=args.length) bannerName.append(" ");
            }

            for (Banner x:list) {
                if (x.getName().equals(bannerName.toString())) {
                    banner = x;
                    break;
                }
            }
        }

        e.getJDA().addEventListener(
                new CircleSimulator(
                        sendMessage("your summons for: "+banner.getName()),
                        e.getAuthor(),
                        banner));
    }

    private static String printDate(OffsetDateTime date) {
        return date.getMonth()+"/"+date.getDayOfMonth()+"/"+date.getYear();
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("summon");
    }
}
