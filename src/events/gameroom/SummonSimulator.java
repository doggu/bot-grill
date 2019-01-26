package events.gameroom;

import utilities.feh.heroes.Hero;
import utilities.feh.players.Summoner;
import utilities.feh.summoning.Banner;
import utilities.feh.summoning.BannerDatabase;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SummonSimulator extends Gameroom {
    private static List<Summoner> summoners = new ArrayList<>();
    private ArrayList<CircleSimulator> sessions = new ArrayList<>();



    private void simulateDay() {
        GregorianCalendar day;
        try {
            day = parseDate(args[1]);
        } catch (Exception g) {
            sendMessage("incorrect date format!");
            log("user formatted their date wrong: "+args[1]);
            return;
        }

        List<Banner> openBanners = new ArrayList<>();
        for (Banner x: BannerDatabase.BANNERS) {
            System.out.print(x.getName()+": ");
            if (x.getStartDate().compareTo(day)<0&&
                        x.getEndDate().compareTo(day)>0) {
                System.out.println("added");
                openBanners.add(x);
            } else {
                System.out.println("rejected");
            }
        }

        String msg = "list of open banners:\n";
        StringBuilder bannerNames = new StringBuilder();
        for (Banner x:openBanners) {
            bannerNames.append(x.getName())
                    .append("\n");
        }

        msg+= bannerNames.toString();

        if (msg.length()>2000) {
            sendMessage("sorry, there was a problem...");
            log("damn son "+args[1]+" had a lotta banners");
            return;
        } else {
            sendMessage(msg);
            log("simulated a date in FEH summoning");
        }
    }

    private void createSession() {
        //"Your circle for: "+[banner title]+"\n"
        //"rates: focus = " +[focus rate]+" 5* = "+[5* rate]

        List<Banner> list = BannerDatabase.BANNERS;
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

        boolean ambiguousName = true;
        boolean ambiguousDate = true;
        /*
        boolean ambiguousName = false;
        boolean ambiguousDate = false;
        for (Banner x:list) {
            if (banner!=x) {
                if (banner.getName().equals(x.getName())) {
                    //if the focus units are the same
                    if (!banner.getRarityFPool().containsAll(x.getRarityFPool())) {
                        ambiguousName = false;
                        ambiguousDate = true;
                    } else {
                        ambiguousName = true;
                        ambiguousDate = false;
                    }
                }
            }
        }
       */
        String message = "your summons for: \n"+banner.getName();
        if (ambiguousName) {
            message+= " (feat. ";
            for (Hero x:banner.getRarityFPool()) //Hero is not a good name for a class
                message+= x.getName()+", ";
            message = message.substring(0,message.length()-2);
            message+= ")";
        }

        if (ambiguousDate) {
            GregorianCalendar x = banner.getStartDate();
            //MONTH IS ZERO-BASED (again)
            message+= "\n"+(x.get(Calendar.MONTH)+1)+"/"+x.get(Calendar.DAY_OF_MONTH)+"/"+x.get(Calendar.YEAR);
        }

        for (CircleSimulator x:sessions) {
            if (x.getSummoner().equals(e.getAuthor())) {
                if (x.getServer().equals(e.getGuild())) {
                    if (!x.canClose()) {
                        sendMessage("you haven't pulled at least one orb " +
                                "from your previous session! please do so " +
                                "before beginning a new one.");
                        log("summoner " + e.getAuthor().getName() + " attempted to start a new session " +
                                "without finishing their previous one.");
                        return;
                    }
                }
            }
        }

        CircleSimulator circle = new CircleSimulator(
                sendMessage(message),
                e.getAuthor(),
                banner);

        e.getJDA().addEventListener(circle);
        sessions.add(circle);

        String report = "created new summoning circle for " + e.getAuthor().getName() + "\n\t\t\t\t\t\t" +
                "banner: " + banner.getName() + "\n\t\t\t\t\t\t" +
                "5* focus rate: "+banner.getRarityFRate()+"%\n\t\t\t\t\t\t" +
                "5* rate: "+banner.getRarity5Rate()+"%\n\t\t\t\t\t\t";
        StringBuilder featuredUnitsSB = new StringBuilder("featured units: ");
        for (int i=0; i<banner.getRarityFPool().size(); i++) {
            Hero x = banner.getRarityFPool().get(i);
            featuredUnitsSB
                    .append(x.getName())
                    .append(": ")
                    .append(x.getEpithet())
                    .append(", ");
            if (i%2==1&&i+1!=banner.getRarityFPool().size()) featuredUnitsSB.append("\n\t\t\t\t\t\t\t\t\t\t");
        }
        String featuredUnits = featuredUnitsSB.substring(0, featuredUnitsSB.length()-2);
        report+= featuredUnits.toString() + "\n\t\t\t\t\t\t";
        report+= "banner start/end: " +
                printDate(banner.getStartDate()) + " - "+printDate(banner.getEndDate())+"\n\t\t\t\t\t\t";
        report+= "3* pool size: " + banner.getRarity3Pool().size() + "\n\t\t\t\t\t\t" +
                "4* pool size: " + banner.getRarity4Pool().size();
        log(report);
    }



    private GregorianCalendar parseDate(String date) throws NumberFormatException {
        //TODO: make this less basic
        String[] nums = date.split("-");
        int year = Integer.parseInt(nums[2]),
                month = Integer.parseInt(nums[0])-1, //guess i'll make this note again to drill it into my head
                day = Integer.parseInt(nums[1]);
        return new GregorianCalendar(year, month, day);
    }

    private static String printDate(GregorianCalendar date) {
        return "" +
                date.get(GregorianCalendar.MONTH) +
                "/" + date.get(GregorianCalendar.DAY_OF_MONTH) +
                "/" + date.get(GregorianCalendar.YEAR);
    }

    private static String printDate(OffsetDateTime date) {
        return date.getMonth() +
                "/" + date.getDayOfMonth() +
                "/" + date.getYear();
    }



    public boolean isCommand() {
        return args[0].equalsIgnoreCase("summon")||args[0].equalsIgnoreCase("simulateDay");
    }

    public void onCommand() {
        switch (args[0]) {
            case "summon":
                createSession();
                break;
            case "simulateDay":
                simulateDay();
                break;
        }
    }
}
