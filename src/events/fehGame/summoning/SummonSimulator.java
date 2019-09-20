package events.fehGame.summoning;

import events.fehGame.FEHCommand;
import feh.heroes.character.Hero;
import feh.players.Summoner;
import feh.summoning.Banner;
import feh.summoning.BannerDatabase;

import java.util.GregorianCalendar;

public class SummonSimulator extends FEHCommand {
    public void onCommand() {
        for (Summoner x:Summoner.SUMMONERS) {
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
            }
        }

        //find summoner corresponding to user
        Summoner summoner = null;
        String authorID = e.getAuthor().getId();
        for (Summoner x:Summoner.SUMMONERS) {
            if (x.getUser().getId().equals(authorID)) {
                summoner = x;
            }
        }

        //register new summoner if user has not summoned before
        if (summoner==null) {
            summoner = new Summoner(e.getAuthor());
            Summoner.SUMMONERS.add(summoner);
        }



        Banner banner = BannerDatabase.BANNERS
                .get((int)(Math.random()*BannerDatabase.BANNERS.size())); //temp

        if (args.length>1) {
            StringBuilder bannerName = new StringBuilder();
            for (int i=1; i<args.length; i++) {
                bannerName.append(args[i]);
                if (i+1!=args.length) bannerName.append(" ");
            }

            for (Banner x:BannerDatabase.BANNERS) {
                if (x.getName().equalsIgnoreCase(bannerName.toString())) {
                    banner = x;
                    break;
                }
            }
        }

        CircleSimulator circle = new CircleSimulator(
                e.getChannel(),
                summoner,
                banner);

        circle.register();



        String report = "created new summoning circle for " + e.getAuthor().getName() + "\n\t\t" +
                "banner: " + banner.getName() + "\n\t\t" +
                "5* focus: "+banner.getRarityFRate()+"%\n\t\t" +
                "5* rates: "+banner.getRarity5Rate()+"%\n\t\t";
        StringBuilder featuredUnitsSB = new StringBuilder("featured units: ");
        for (int i=0; i<banner.getRarityFPool().size(); i++) {
            Hero x = banner.getRarityFPool().get(i);
            if (x.getFullName().isAmbiguousName()) //TODO: units like Olwen (same mov/weapon per alt) are not clarified
                featuredUnitsSB.append(x.getFullName());
            else
                featuredUnitsSB.append(x.getFullName().getName());
            featuredUnitsSB.append(", ");
            if (i%2==1&&i+1!=banner.getRarityFPool().size()) featuredUnitsSB.append("\n\t\t\t\t\t\t");
        }
        String featuredUnits = featuredUnitsSB.substring(0, featuredUnitsSB.length()-2);
        report+= featuredUnits + "\n\t\t\t\t\t";
        report+= "banner date: " +
                printDate(banner.getStartDate()) + " - "+printDate(banner.getEndDate());
        //report+= "3* pool size: " + banner.getRarity3Pool().size() + "\n\t\t\t\t\t\t" +
        //        "4* pool size: " + banner.getRarity4Pool().size();

        log(report);
    }

    private static String printDate(GregorianCalendar date) {
        return "" +
                date.get(GregorianCalendar.MONTH) +
                "/" + date.get(GregorianCalendar.DAY_OF_MONTH) +
                "/" + date.get(GregorianCalendar.YEAR);
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("summon");
    }

    public String getName() { return "SummonSimulator"; }
    public String getDescription() { return "recruit new units, it's free!"; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "Syntax:\n" +
                "\t\t\"summon [banner name]\"";
    }
}
