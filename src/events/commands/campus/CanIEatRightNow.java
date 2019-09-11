package events.commands.campus;

import events.commands.Command;
import main.BotMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.Range;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;

public class CanIEatRightNow extends Command {
    private static final URL PIONEER_HALL_HOURS;
    private static final ArrayList<ArrayList<Range>> OPEN_TIMES;
    static {
        try {
            PIONEER_HALL_HOURS =
                    new URL("http://www.dining.umn.edu/CampusRestaurants/ResidentialRestaurants/pioneer-eb.html");
        } catch (MalformedURLException murle) {
            throw new Error("heh, murle");
        }

        Document soup;
        try {
            soup = Jsoup.parse(PIONEER_HALL_HOURS, 3000);
        } catch (Exception e) {
            throw new Error();
        }

        Element table = soup.select("table").get(0);
        Elements rows = table.select("tr");
        if (rows.size()!=7) throw new Error("this isn't the table i was expecting");
        Elements
                head = rows.get(0).select("td"),
                bekfest = rows.get(1).select("td"),
                coldbekfs = rows.get(2).select("td"),
                lbrunch = rows.get(3).select("td"),
                sandsoup = rows.get(4).select("td"),
                dinner = rows.get(5).select("td"),
                late = rows.get(6).select("td");

        OPEN_TIMES = new ArrayList<>();
        OPEN_TIMES.add(new ArrayList<>());
        OPEN_TIMES.add(new ArrayList<>());
        OPEN_TIMES.add(new ArrayList<>());
        OPEN_TIMES.add(new ArrayList<>());
        //TYPE, MON-THURS, FRI, SAT, SUN
                //ignore top row
        for (int i=1; i<7; i++) {
            Elements items = rows.get(i).select("td");
            String  mt = items.get(1).text(),
                    f = items.get(1).text(),
                    sa = items.get(1).text(),
                    su = items.get(1).text();
            OPEN_TIMES.get(0).add(generateTimeRange(mt));
            OPEN_TIMES.get(1).add(generateTimeRange(f));
            OPEN_TIMES.get(2).add(generateTimeRange(sa));
            OPEN_TIMES.get(3).add(generateTimeRange(su));
        }
    }

    private static Range generateTimeRange(String rng) {
        if (rng.equals("CLOSED")) return null;

        String[] both = rng.split(" - ");
        Time    from = parseTime(both[0]),
                until = parseTime(both[1]);

        //Range<Time> range = new Range<Time>(from, until);



        return null;
    }

    private static Time parseTime(String time) {
        String[] sections = time.split("[:AP]");
        int hour = Integer.parseInt(sections[0]),
            minute = Integer.parseInt(sections[1]);

        if (time.indexOf("PM")>0) hour+= 12;

        return new Time(hour, minute, 0);
    }



    public boolean isCommand() {
        return e.getMessage().getContentRaw()
                .indexOf(e.getJDA().getSelfUser().getAsMention()+" can i go eat right now")==0;
    }

    public void onCommand() {
        sendMessage("idk");
    }

    @Override
    public String getName() {
        return "CanIEatRightNow";
    }

    @Override
    public String getDescription() {
        return "can Everett go to eat at Pioneer Hall?";
    }

    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    protected char getPrefix() {
        return BotMain.bot_grill.getSelfUser().getAsMention().charAt(0);
    }
}
