package feh.summoning;

import feh.FEHeroesCache;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import feh.characters.hero.Hero;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class BannerDatabase extends Database<Banner> {
    public static BannerDatabase DATABASE;
    public static ArrayList<Banner> BANNERS;

    private static final String FOCUS_ARCHIVE = "Summoning_Focus_archive";

    private static FEHeroesCache FOCUS_ARCHIVE_FILE;

    static {
        FOCUS_ARCHIVE_FILE = new FEHeroesCache(FOCUS_ARCHIVE);

        DATABASE = new BannerDatabase();
        BANNERS = DATABASE.getList();
    }



    @Override
    protected WebCache[] getOnlineResources() {
        return new WebCache[] { FOCUS_ARCHIVE_FILE };
    }



    protected ArrayList<Banner> getList() {
        System.out.print("processing banners... ");
        long start = System.nanoTime();

        Document bannersFile;
        try {
            bannersFile = Jsoup.parse(FOCUS_ARCHIVE_FILE, "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new ArrayList<>();
        }

        Elements tables = bannersFile.select("table");

        ArrayList<Banner> banners = new ArrayList<>();

        for (Element table:tables) {
            banners.add(createBanner(table)); //@Nullable
        }

        while (banners.contains(null))
            banners.remove(null);



        System.out.println("done (" +
                new BigDecimal((System.nanoTime()-start)/1000000000.0).round(new MathContext(3)) +
                " s)!");
        return banners;
    }

    private static Banner createBanner(Element table) {
        Elements rows = table.select("tr");



        String name = rows.get(0).text();

        //1 is image, "Featured Units", and first row of heroes
                    //second row, children, second item, children, first item, heroes
        Element h1 = rows.get(1).children().get(2).children().get(0);

        ArrayList<Hero> summonables;
        int i;
        try {
            summonables = getSummonables(h1);

            for (i = 2; i<rows.size()-2; i++) {
                Element h = rows.get(i).children().get(0).children().get(0);
                summonables.addAll(getSummonables(h));
            }
        } catch (Error e) {
            e.printStackTrace();
            return null;
        }

        GregorianCalendar startDate, endDate;

        try {
            startDate = getDate(rows.get(i).children().get(1).text());
        } catch (IllegalArgumentException iae) {
            startDate = null;
        }
        try {
            endDate = getDate(rows.get(i + 1).children().get(1).text());
        } catch (IllegalArgumentException iae) {
            endDate = null;
        }

        return new Banner(name, summonables, startDate, endDate);
    }
    private static ArrayList<Hero> getSummonables(Element heroes) {
        Elements items = heroes.select("div").get(0).children();
        ArrayList<Hero> summonables = new ArrayList<>();

        for (Element hero:items) {
            summonables.add(new Hero(hero.text()));
        }

        return summonables;
    }


    @Override
    public ArrayList<Banner> findAll(String input) {
        ArrayList<Banner> all = new ArrayList<>();
        for (Banner x:BANNERS) {
            if (x.getName().equals(input)) {
                all.add(x);
            }
        }

        return all;
    }

    @Override
    public Banner getRandom() {
        return BANNERS.get((int)(Math.random()*BANNERS.size()));
    }

    private static GregorianCalendar getDate(String date) throws IllegalArgumentException {
        String[] endDateStr = date.split("-");
        int year = Integer.parseInt(endDateStr[0]);
        int month;
        //this shit so fuckin useless (other than alerting of invalid numbers)
        switch (Integer.parseInt(endDateStr[1])) {
            case 1:
                month = Calendar.JANUARY;
                break;
            case 2:
                month = Calendar.FEBRUARY;
                break;
            case 3:
                month = Calendar.MARCH;
                break;
            case 4:
                month = Calendar.APRIL;
                break;
            case 5:
                month = Calendar.MAY;
                break;
            case 6:
                month = Calendar.JUNE;
                break;
            case 7:
                month = Calendar.JULY;
                break;
            case 8:
                month = Calendar.AUGUST;
                break;
            case 9:
                month = Calendar.SEPTEMBER;
                break;
            case 10:
                month = Calendar.OCTOBER;
                break;
            case 11:
                month = Calendar.NOVEMBER;
                break;
            case 12:
                month = Calendar.DECEMBER;
                break;
            default:
                throw new Error();
        }
        int day = Integer.parseInt(endDateStr[2]);
        return new GregorianCalendar(year, month, day, 23, 59, 59);
    }



    public static void main(String[] args) {
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
