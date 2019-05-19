package events.fehGame.summoning;

import events.commands.Command;
import utilities.feh.summoning.Banner;
import utilities.feh.summoning.BannerDatabase;

import java.util.*;

public class SimulateDay extends Command {
    private static GregorianCalendar parseDate(String date) throws NumberFormatException {
        //TODO: make this less basic
        String[] nums = date.split("[-/]");
        int year, month, day;
        year = Integer.parseInt(nums[2]);
        switch (Integer.parseInt(nums[0])-1) {
            case Calendar.JANUARY:
                month = Calendar.JANUARY;
                break;
            case Calendar.FEBRUARY:
                month = Calendar.FEBRUARY;
                break;
            case Calendar.MARCH:
                month = Calendar.MARCH;
                break;
            case Calendar.APRIL:
                month = Calendar.APRIL;
                break;
            case Calendar.MAY:
                month = Calendar.MAY;
                break;
            case Calendar.JUNE:
                month = Calendar.JUNE;
                break;
            case Calendar.JULY:
                month = Calendar.JULY;
                break;
            case Calendar.AUGUST:
                month = Calendar.AUGUST;
                break;
            case Calendar.SEPTEMBER:
                month = Calendar.SEPTEMBER;
                break;
            case Calendar.OCTOBER:
                month = Calendar.OCTOBER;
                break;
            case Calendar.NOVEMBER:
                month = Calendar.NOVEMBER;
                break;
            case Calendar.DECEMBER:
                month = Calendar.DECEMBER;
                break;
            default:
                //well shit
                throw new NumberFormatException();
        }
        day = Integer.parseInt(nums[1]);
        //honestly am i really going to make a switch case for an int to find a field which is just another int (which is one less than the already-defined int)
                                                    //yes
        return new GregorianCalendar(year, month, day);
    }

    public void onCommand() {
        GregorianCalendar day;
        try {
            day = parseDate(args[1]);
        } catch (Exception g) {
            sendMessage("incorrect date format!");
            log("user formatted their date wrong: \""+args[1]+"\"");
            return;
        }

        List<Banner> openBanners = new ArrayList<>();
        for (Banner x: BannerDatabase.BANNERS) {
            if (x.getStartDate().compareTo(day)<0&&
                    x.getEndDate().compareTo(day)>0) {
                //System.out.println("added");
                openBanners.add(x);
            } else {
                //System.out.println("rejected");
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
        } else {
            sendMessage(msg);
            log("simulated "+args[1]+" in FEH summoning");
        }
    }

    public boolean isCommand() { return args[0].equalsIgnoreCase("simulateDay"); }



    public String getName() { return "SimulateDay"; }
    public String getDescription() { return "Provides a list of banners that were active for a given day!"; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "\tSyntax: \"?SimulateDay [date]\"" +
                "the format of the date is MM-DD-YYYY. if the date is only a single digit, " +
                "do not put leading zeroes.";
    }



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            GregorianCalendar date;
            try {
                date = parseDate(input.nextLine());
                System.out.println(date);
            } catch (NumberFormatException f) {
                System.out.println("incorrect format! please try again.");
            }
        }
    }
}
