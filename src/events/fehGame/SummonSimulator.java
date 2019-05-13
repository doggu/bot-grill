package events.fehGame;

import events.gameroom.Gameroom;
import main.BotMain;
import utilities.feh.heroes.character.Hero;
import utilities.feh.players.Summoner;
import utilities.feh.summoning.Banner;
import utilities.feh.summoning.BannerDatabase;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SummonSimulator extends Gameroom {
    static List<Summoner> summoners = new ArrayList<>();
    private List<Banner> BANNERS = BannerDatabase.BANNERS;


    //TODO: move this to a different listener (i feel like i've written this before)
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
        for (Summoner x:summoners) {
            if (x.getUser().getId().equals(e.getAuthor().getId())) {
                System.out.println("found a registered summoner");
                if (x.isSummoning()) {
                    //inaccurate logic for ease of use
                    CircleSimulator currentSession = x.getCurrentSession();
                    if (currentSession.canClose()) {
                        currentSession.closeCircle();
                    } else {
                        sendMessage("please summon at least one stone before starting a new session.");
                        return;
                    }

                    /* more accurate
                    sendMessage("please close your previous session before starting a new one.");
                    return;
                    */
                }
            } else {
                System.out.println(x.getUser().getId()+" does not equal "+e.getAuthor().getId());
            }
        }

        Summoner summoner = null;
        String authorID = e.getAuthor().getId();
        for (Summoner x:summoners) {
            if (x.getUser().getId().equals(authorID)) {
                summoner = x;
            }
        }

        if (summoner==null) {
            summoner = new Summoner(e.getAuthor());
            summoners.add(summoner);
        }



        //"Your circle for: "+[banner title]+"\n"
        //"rates: focus = " +[focus rate]+" 5* = "+[5* rate]

        Banner banner = BANNERS.get((int)(Math.random()*BANNERS.size())); //temp

        if (args.length>1) {
            StringBuilder bannerName = new StringBuilder();
            for (int i=1; i<args.length; i++) {
                bannerName.append(args[i]);
                if (i+1!=args.length) bannerName.append(" ");
            }

            for (Banner x:BANNERS) {
                if (x.getName().equals(bannerName.toString())) {
                    banner = x;
                    break;
                }
            }
        }

        String message = "your summons for: \n"+banner.getName()+'\n';
        message+= "featured units: ";
        for (Hero x:banner.getRarityFPool()) { //Character is not a good name for a class (changed to Hero)
            message+= x.getFullName().getName();
            if (x.getFullName().isAmbiguousName())
                message+= " ("+x.getWeaponType()+" "+x.getMoveType()+")";
                            //there are three axe armor hectors btw
            message+= ", ";
        }
        message = message.substring(0,message.length()-2);

        GregorianCalendar startDate = banner.getStartDate();
        //MONTH IS ZERO-BASED (again)
        message+= "\n"+(startDate.get(Calendar.MONTH)+1)+
                "/"+startDate.get(Calendar.DAY_OF_MONTH)+
                "/"+startDate.get(Calendar.YEAR);

        GregorianCalendar endDate = banner.getStartDate();
        //MONTH IS ZERO-BASED (again)
        message+= " - "+(endDate.get(Calendar.MONTH)+1)+
                "/"+endDate.get(Calendar.DAY_OF_MONTH)+
                "/"+endDate.get(Calendar.YEAR);



        CircleSimulator circle = new CircleSimulator(
                sendMessage(message),
                summoner,
                banner);

        BotMain.addListener(circle);
        summoner.startSummoning(circle);



        String report = "created new summoning circle for " + e.getAuthor().getName() + "\n\t\t\t\t\t\t" +
                "banner: " + banner.getName() + "\n\t\t\t\t\t\t" +
                "5* focus: "+banner.getRarityFRate()+"%\n\t\t\t\t\t\t" +
                "5* rates: "+banner.getRarity5Rate()+"%\n\t\t\t\t\t\t";
        StringBuilder featuredUnitsSB = new StringBuilder("featured units: ");
        for (int i=0; i<banner.getRarityFPool().size(); i++) {
            Hero x = banner.getRarityFPool().get(i);
            if (x.getFullName().isAmbiguousName())
                featuredUnitsSB.append(x.getFullName());
            else
                featuredUnitsSB.append(x.getFullName().getName());
            featuredUnitsSB.append(", ");
            if (i%2==1&&i+1!=banner.getRarityFPool().size()) featuredUnitsSB.append("\n\t\t\t\t\t\t\t\t\t\t");
        }
        String featuredUnits = featuredUnitsSB.substring(0, featuredUnitsSB.length()-2);
        report+= featuredUnits.toString() + "\n\t\t\t\t\t\t";
        report+= "banner date: " +
                printDate(banner.getStartDate()) + " - "+printDate(banner.getEndDate());
        /*
        report+= "3* pool size: " + banner.getRarity3Pool().size() + "\n\t\t\t\t\t\t" +
                "4* pool size: " + banner.getRarity4Pool().size();
        */
        log(report);
    }



    private GregorianCalendar parseDate(String date) throws NumberFormatException {
        //TODO: make this less basic
        String[] nums = date.split("-");
        int year, month, day;
        year = Integer.parseInt(nums[2]);
        month = Integer.parseInt(nums[0])-1; //guess i'll make this note again to drill it into my head
        day = Integer.parseInt(nums[1]);
                                         //honestly am i really going to make a switch case for an int to find a field which is just another int (which is one less than the already-defined int)
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
        switch (args[0].toLowerCase()) {
            case "summon":
                createSession();
                break;
            case "simulateday":
                simulateDay();
                break;
        }
    }
}
