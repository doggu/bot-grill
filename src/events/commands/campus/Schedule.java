package events.commands.campus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.Range;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Schedule {
    static final Schedule PIONEER_HALL, CARRILLO_COMMONS, DLG_COMMONS;

    private static final Document
            PIONEER_HALL_HOURS,
            UCSB_HOURS;
    static {
        try {
            PIONEER_HALL_HOURS = Jsoup.parse(
                    new URL("http://www.dining.umn.edu/CampusRestaurants/ResidentialRestaurants/pioneer-eb.html"),
                    3000);
            UCSB_HOURS = Jsoup.parse(
                    new URL("https://www.housing.ucsb.edu/dining/dining-commons-hours%E2%80%94academic-year"),
                    3000);
        } catch (Exception e) {
            throw new Error("well fuck");
        }

        PIONEER_HALL = getPioneerHall();
        CARRILLO_COMMONS = getCarrilloCommons();
        DLG_COMMONS = getDlgCommons();
    }

    private static Schedule getPioneerHall() {
        Element t = PIONEER_HALL_HOURS.select("table").get(0);
        Elements rows = t.select("tr");
        if (rows.size()!=7) throw new Error("this isn't the table i was expecting");
        /*
        Elements
                head = rows.get(0).select("td"),
                bekfest = rows.get(1).select("td"),
                coldbekfs = rows.get(2).select("td"),
                lbrunch = rows.get(3).select("td"),
                sandsoup = rows.get(4).select("td"),
                dinner = rows.get(5).select("td"),
                late = rows.get(6).select("td");
         */

        ArrayList<ArrayList<Range<TimeOfDay>>> table = new ArrayList<>();
        for (int i=0; i<7; i++)
            table.add(new ArrayList<>());
        //TYPE, MON-THURS, FRI, SAT, SUN
        //ignore top row
        for (int i=1; i<7; i++) {
            Elements items = rows.get(i).select("td");
            String  mt = items.get(0).text(),
                    f = items.get(1).text(),
                    sa = items.get(2).text(),
                    su = items.get(3).text();
            for (int j=0; j<4; j++)
                table.get(j).add(generateTimeRange(mt));
            table.get(4).add(generateTimeRange(f));
            table.get(5).add(generateTimeRange(sa));
            table.get(6).add(generateTimeRange(su));
        }

        return new Schedule(table);
    }
    private static Schedule getCarrilloCommons() {
        return null;
    }
    private static Schedule getDlgCommons() {
        Element t = UCSB_HOURS.select("table").get(1);
        Elements rows = t.select("tr");

        List<Element>
                MtF = new ArrayList<>(rows.subList(1, 3)),
                SSu = new ArrayList<>(rows.subList(4, 6));

        MtF.addAll(rows.subList(7,8));

        ArrayList<ArrayList<Range<TimeOfDay>>> table = new ArrayList<>();
        for (int i=0; i<7; i++)
            table.add(new ArrayList<>());
        //MON-FRI[LUNCH, DINNER], SAT-SUN[BRUNCH, DINNER]
        //ignore top row
        for (int i=0; i<MtF.size(); i++) {
            Range<TimeOfDay> range = generateTimeRange(MtF.get(i).select("td").get(1).text());

            for (int j=0; j<5; j++)
                table.get(j).add(range);
        }

        for (int i=0; i<SSu.size(); i++) {
            Range<TimeOfDay> range = generateTimeRange(SSu.get(i).select("td").get(1).text());

            table.get(5).add(range);
            table.get(6).add(range);
        }

        return new Schedule(table);
    }

    private static Range<TimeOfDay> generateTimeRange(String rng) {
        if (rng.equals("CLOSED")) return null;

        String[] both = rng.split(" - ");
        TimeOfDay
                from = parseTime(both[0]),
                until = parseTime(both[1]);

        return new Range<>(from, until);
    }
    private static TimeOfDay parseTime(String time) {
        time = time.toLowerCase();
        if (time.equals("midnight")) return new TimeOfDay(23,59,59.999f);
        String[] sections = time.split("[:ap]");
        int hour = Integer.parseInt(sections[0]),
                minute = Integer.parseInt(sections[1]);

        if (time.indexOf("pm")>0) hour+= 12;

        return new TimeOfDay(hour, minute, 0);
    }



    //day, part, time range
    private final ArrayList<ArrayList<Range<TimeOfDay>>> schedule;



    private Schedule(ArrayList<ArrayList<Range<TimeOfDay>>> schedule) {
        this.schedule = schedule;
    }
    private Schedule() {
        this.schedule = new ArrayList<>();
    }



    boolean canGo(TimeZone f) {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(f);
        c.setTime(now);
        int dow = c.get(Calendar.DAY_OF_WEEK);

        int     h = c.get(Calendar.HOUR_OF_DAY),
                m = c.get(Calendar.MINUTE),
                s = c.get(Calendar.SECOND);//+c.get(Calendar.MILLISECOND);
        TimeOfDay currentTime = new TimeOfDay(h, m, s);

        int i = 0;
        switch (dow) {
            case 7:
                i++;
            case 6:
                i++;
            case 5:
                i++;
            case 4:
                i++;
            case 3:
                i++;
            case 2:
                i++;
        }
        ArrayList<Range<TimeOfDay>> day = schedule.get(i);

        for (Range<TimeOfDay> r:day) {
            if (r != null)
                if (r.inThisRange(currentTime))
                    return true;
        }
        return false;
    }



    public String toString() {
        return schedule.toString();
    }



    public static void main(String[] args) {
        System.out.println(DLG_COMMONS);
        //carrillo
        //de la guerra
        //ortega
        //portola
        //eatery
    }
}
