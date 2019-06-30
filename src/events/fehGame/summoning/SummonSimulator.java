package events.fehGame.summoning;

import events.gameroom.Gameroom;
import main.BotMain;
import feh.heroes.character.Hero;
import feh.players.Summoner;
import feh.summoning.Banner;
import feh.summoning.BannerDatabase;
import net.dv8tion.jda.core.MessageBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SummonSimulator extends Gameroom {
    public static List<Summoner> summoners = new ArrayList<>();



    private static String printDate(GregorianCalendar date) {
        return "" +
                date.get(GregorianCalendar.MONTH) +
                "/" + date.get(GregorianCalendar.DAY_OF_MONTH) +
                "/" + date.get(GregorianCalendar.YEAR);
    }



    public void onCommand() {
        //
        for (Summoner x:summoners) {
            if (x.getUser().getId().equals(e.getAuthor().getId())) {
                System.out.println("found a registered summoner");
                if (x.isSummoning()) {
                    //inaccurate logic for ease of use
                    CircleSimulator currentSession = x.getCurrentSession();
                    if (currentSession.canClose()) {
                        currentSession.closeCircle();
                    } else {
                        sendMessage("please summon at least one stone before starting a new session. " +
                                "link: "+currentSession.getSessionMessage().getJumpUrl());
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

        //find summoner corresponding to user
        Summoner summoner = null;
        String authorID = e.getAuthor().getId();
        for (Summoner x:summoners) {
            if (x.getUser().getId().equals(authorID)) {
                summoner = x;
            }
        }

        //register new summoner if user has not summoned before
        if (summoner==null) {
            summoner = new Summoner(e.getAuthor());
            summoners.add(summoner);
        }



        //"Your circle for: "+[banner title]+"\n"
        //"rates: focus = " +[focus rate]+" 5* = "+[5* rate]

        Banner banner = BannerDatabase.BANNERS.get((int)(Math.random()*BannerDatabase.BANNERS.size())); //temp

        if (args.length>1) {
            StringBuilder bannerName = new StringBuilder();
            for (int i=1; i<args.length; i++) {
                bannerName.append(args[i]);
                if (i+1!=args.length) bannerName.append(" ");
            }

            for (Banner x:BannerDatabase.BANNERS) {
                if (x.getName().equals(bannerName.toString())) {
                    banner = x;
                    break;
                }
            }
        }

        MessageBuilder message = new MessageBuilder("your summons for: \n")
                .append(banner.getName())
                .append('\n')
                .append("featured units: ");
        for (Hero x:banner.getRarityFPool()) { //Character is not a good name for a class (changed to Hero)
            message.append(x.getFullName().getName());
            if (x.getFullName().isAmbiguousName())
                message
                        .append(" (")
                        .append(x.getWeaponType())
                        .append(' ')
                        .append(x.getMoveType())
                        .append(')');
            //there are three axe armor hectors btw
            message.append(", ");
        }
        message.replaceLast(", ", "");

        GregorianCalendar startDate = banner.getStartDate();
        //MONTH IS ZERO-BASED (again)
        message.append("\n").append(startDate.get(Calendar.MONTH)+1)
                .append("/").append(startDate.get(Calendar.DAY_OF_MONTH))
                .append("/").append(startDate.get(Calendar.YEAR));

        GregorianCalendar endDate = banner.getEndDate();
        //MONTH IS ZERO-BASED (again)
        message.append(" - ").append(endDate.get(Calendar.MONTH)+1)
                .append("/").append(endDate.get(Calendar.DAY_OF_MONTH))
                .append("/").append(endDate.get(Calendar.YEAR));



        CircleSimulator circle = new CircleSimulator(
                sendMessage(message.build()),
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
            if (x.getFullName().isAmbiguousName()) //TODO: units like Olwen (same mov/weapon per alt) are not clarified
                featuredUnitsSB.append(x.getFullName());
            else
                featuredUnitsSB.append(x.getFullName().getName());
            featuredUnitsSB.append(", ");
            if (i%2==1&&i+1!=banner.getRarityFPool().size()) featuredUnitsSB.append("\n\t\t\t\t\t\t\t\t\t\t");
        }
        String featuredUnits = featuredUnitsSB.substring(0, featuredUnitsSB.length()-2);
        report+= featuredUnits + "\n\t\t\t\t\t\t";
        report+= "banner date: " +
                printDate(banner.getStartDate()) + " - "+printDate(banner.getEndDate());
        /*
        report+= "3* pool size: " + banner.getRarity3Pool().size() + "\n\t\t\t\t\t\t" +
                "4* pool size: " + banner.getRarity4Pool().size();
        */
        log(report);
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("summon");
    }
}
