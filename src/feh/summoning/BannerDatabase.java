package feh.summoning;

import feh.FEHeroesCache;
import utilities.WebScalper;
import feh.heroes.character.Hero;

import java.util.*;
import java.util.stream.IntStream;

public class BannerDatabase extends WebScalper {
    public static List<Banner> BANNERS = getList();

    private static final String FOCUS_ARCHIVE = "Summoning_Focus_archive";

    private static FEHeroesCache FOCUS_ARCHIVE_FILE;



    public static void updateCache() {
        if (!FOCUS_ARCHIVE_FILE.update()) throw new Error("unable to update "+FOCUS_ARCHIVE_FILE.getName());

        BANNERS = getList();
    }

    private static List<Banner> getList() {
        FOCUS_ARCHIVE_FILE = new FEHeroesCache(FOCUS_ARCHIVE);



        String line = FOCUS_ARCHIVE_FILE.getLongAssLine(100000);

        if (line==null) throw new Error("getList did not find the big-ass, one-line table");

        IntStream data = line.chars();

        Iterator<String> items = getItems(data).iterator();
        ArrayList<Banner> banners = new ArrayList<>();

        Banner x;
        while (items.hasNext()) {
            String n1, n2;
            try {
                n1 = items.next();
                n2 = items.next();
                while (!n1.equals(n2)) {
                    n1 = n2;
                    n2 = items.next();
                }
            } catch (NoSuchElementException endOfSite) {
                System.out.println("fuckin markfeh");
                continue;
            }
            String bannerName = n1;

            //check alignment
            if (!items.next().equals("Featured Units")) {
                System.out.println(n1+"/"+n2+" ran into issue in featured units");
            }

            ArrayList<String> featuredUnitNames = new ArrayList<>();
            String name;
            //gather names until new data appears
            while (!(name = items.next()).equals("Start Date")) {
                featuredUnitNames.add(name);
            }
            ArrayList<Hero> featuredUnits = new ArrayList<>();
            for (String hero:featuredUnitNames) {
                try {
                    featuredUnits.add(new Hero(hero));
                } catch (Error g) {
                    System.out.println("BannerDatabase could not find character: "+hero);
                }
            }



            GregorianCalendar startDate;
            try {
                startDate = getDate(items.next());
            } catch (IndexOutOfBoundsException f) {
                System.out.println("incorrect date format found for \""+name+"\"");
                throw new Error();
            }

            items.next(); //"End Date"

            GregorianCalendar endDate;
            try {
                endDate = getDate(items.next());
            } catch (IndexOutOfBoundsException f) {
                System.out.println("incorrect date format found for \""+name+"\"");
                throw new Error();
            }



            x = new Banner(bannerName, featuredUnits, startDate, endDate);
            banners.add(x);
        }



        System.out.println("finished processing banners.");
        return banners;
    }

    private static GregorianCalendar getDate(String date) {
        String[] endDateStr = date.split("-");
        int year = Integer.parseInt(endDateStr[0]);
        int month;
        //this shit so fuckin useless (other than alerting of invalid numbers)
        switch (Integer.parseInt(endDateStr[1])) {
            case 1: month = Calendar.JANUARY; break;
            case 2: month = Calendar.FEBRUARY; break;
            case 3: month = Calendar.MARCH; break;
            case 4: month = Calendar.APRIL; break;
            case 5: month = Calendar.MAY; break;
            case 6: month = Calendar.JUNE; break;
            case 7: month = Calendar.JULY; break;
            case 8: month = Calendar.AUGUST; break;
            case 9: month = Calendar.SEPTEMBER; break;
            case 10: month = Calendar.OCTOBER; break;
            case 11: month = Calendar.NOVEMBER; break;
            case 12: month = Calendar.DECEMBER; break;
            default: throw new Error();
        }
        int day = Integer.parseInt(endDateStr[2]);
        return new GregorianCalendar(year, month, day, 23, 59, 59);
    }



    public static void main(String[] args) {
        updateCache();

        Scanner input = new Scanner(System.in);
        String line;
        while (!(line = input.nextLine()).equals("quit")) {
            for (Banner x:BANNERS) {
                if (x.getName().contains(line)) {
                    System.out.println(x.getName());
                    System.out.println("featuring:");
                    for (Hero y:x.getRarityFPool()) {
                        System.out.println("\t\t"+y.getFullName());
                    }
                }
            }
        }
    }
}
