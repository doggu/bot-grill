package utilities.fehUnits.summoning;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import utilities.fehUnits.heroes.Hero;

//RARITY CHANGE DATE: APRIL 10 2018
//TODO: this should be implemented sooner or later
//this should be in Banner tbh

public class BannerDatabase_old {
    public static final List<Banner> banners = getList();

    private static List<Banner> processFiles(Scanner archive) {
        List<Banner> banners = new ArrayList<>();

        while (archive.hasNextLine()) {
            String name;
            List<Hero> focusUnits = new ArrayList<>();
            GregorianCalendar startDate, endDate;

            List<String> bannerLines = new ArrayList<>();

            for (int i=0; true; i++) {
                bannerLines.add(archive.nextLine());
                if (bannerLines.get(i).contains("End Date")) {
                    break;
                }
            }
            //should be "Featured Units"; remove the month data otherwise
            while (!bannerLines.get(3).contains("Featured Units")) {
                bannerLines.remove(0);
                //System.out.println("removed space/month data");
            }
            if (!bannerLines.get(0).contains(bannerLines.get(2))) {
                System.out.println("ERROR: "+bannerLines.get(0));
                System.out.println("ERROR: "+bannerLines.get(2));
                throw new Error();
            }

            name = bannerLines.get(0);
            bannerLines.subList(0,4).clear();

            while (!bannerLines.get(0).contains("Start Date")) {
                bannerLines.remove(0); //remove image
                bannerLines.remove(0); //remove color/weapon/move data
                Hero focusUnit = new Hero(bannerLines.get(0));
                focusUnits.add(focusUnit);
                bannerLines.remove(0);
            }

            //NOTE: Month is zero based; January = 0, etc.
            String  startDateData = bannerLines.get(0), 
                    endDateData = bannerLines.get(1);
            String[] startDateArr = startDateData.substring(startDateData.indexOf("\t")+1).split("-");
            String[] endDateArr = endDateData.substring(endDateData.indexOf("\t")+1).split("-");
            startDate = new GregorianCalendar(
                    Integer.parseInt(startDateArr[0]),  //year
                    Integer.parseInt(startDateArr[1])-1,  //month
                    Integer.parseInt(startDateArr[2])); //day
            endDate = new GregorianCalendar(
                    Integer.parseInt(endDateArr[0]),
                    Integer.parseInt(endDateArr[1])-1,
                    Integer.parseInt(endDateArr[2]));

            bannerLines.remove(0);
            bannerLines.remove(0);
            Banner banner = new Banner(name, focusUnits, startDate, endDate);
            banners.add(banner);
        }



        return banners;
    }



    private static List<Banner> getList() {
        String[] archivePath = {
                ".",
                "src",
                "utilities",
                "fehUnits",
                "summoning",
                "archive.txt"
        };
        File archiveFile = new File("./src/utilities/fehUnits/summoning/archive.txt");
        Scanner archive;
        try {
            archive = new Scanner(archiveFile);
        } catch (FileNotFoundException g) {
            throw new Error();
        }
        return processFiles(archive);
    }

    public static void main(String[] args) {
        List<Banner> banners = getList();

        System.out.println(banners.size());

        Scanner input = new Scanner(System.in);

        String command = input.nextLine();
        while (!command.equalsIgnoreCase("quit")) {
            String[] arghs = command.split(" ");

            List<Banner> requested = new ArrayList<>();

            for (Banner x:banners) {
                if (x.getName().contains(arghs[0])) {
                    requested.add(x);
                }
            }

            for (Banner x:requested) {
                System.out.print(x.getName()+": ");
                GregorianCalendar startDate = (GregorianCalendar) x.getStartDate().clone();
                GregorianCalendar endDate = (GregorianCalendar) x.getEndDate().clone();

                int i = 0;
                while (startDate.compareTo(endDate)<0) {
                    i++;
                    startDate.add(Calendar.DAY_OF_MONTH, 1);
                }
                System.out.println(i);
            }

            command = input.nextLine();
        }
        /* gotta remember this shit
        GregorianCalendar g = new GregorianCalendar(2018, 3, 17);

        g.add(Calendar.DAY_OF_MONTH, 30);

        System.out.println(g.get(Calendar.DAY_OF_MONTH));
        */
    }
}
