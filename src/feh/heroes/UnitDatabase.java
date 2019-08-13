package feh.heroes;

import feh.FEHeroesCache;
import feh.heroes.character.*;
import feh.heroes.character.constructionSite.MismatchedInputException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.WebScalper;
import feh.heroes.character.constructionSite.HeroConstructor;
import feh.skills.skillTypes.Skill;
import feh.skills.SkillDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class UnitDatabase extends WebScalper {
    public static ArrayList<Hero> HEROES = getList();

    private static final String HERO_SUBDIR = "/herodata/";

    private static final String
            LV1_STATS = "Level_1_stats_table",
            GROWTH_RATES = "Growth_rate_table",
            HERO_LIST = "List_of_Heroes";

    private static final String[] HERO_URLS = {
            LV1_STATS,
            GROWTH_RATES,
            HERO_LIST,
    };



    private static FEHeroesCache
            LV1_STATS_FILE,
            GROWTH_RATES_FILE,
            HERO_LIST_FILE;

    private static FEHeroesCache[] HERO_FILES = {
            LV1_STATS_FILE,
            GROWTH_RATES_FILE,
            HERO_LIST_FILE,
    };



    private static boolean DEBUG = true;



    public static void updateCache() {
        for (int i=0; i<HERO_FILES.length; i++) {
            try {
                if (!HERO_FILES[i].update()) throw new Error("unable to update "+HERO_FILES[i].getName());
            } catch (NullPointerException npe) {
                HERO_FILES[i] = new FEHeroesCache(HERO_URLS[i]);
                i--;
            }
        }
    }



    private static ArrayList<Hero> getList() {
        System.out.print("processing heroes... ");
        long start = System.nanoTime();



        //todo: this is still fucking dumb
        LV1_STATS_FILE = new FEHeroesCache(LV1_STATS, HERO_SUBDIR);
        GROWTH_RATES_FILE = new FEHeroesCache(GROWTH_RATES, HERO_SUBDIR);
        HERO_LIST_FILE = new FEHeroesCache(HERO_LIST, HERO_SUBDIR);

        HERO_GENDERS = getGenders();

        ArrayList<Hero> heroes = new ArrayList<>();

        Document
                lv1StatsFile,
                growthRatesFile,
                heroListFile;
        try {
            lv1StatsFile = Jsoup.parse(LV1_STATS_FILE, "UTF-8");
            growthRatesFile = Jsoup.parse(GROWTH_RATES_FILE, "UTF-8");
            heroListFile = Jsoup.parse(HERO_LIST_FILE, "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new ArrayList<>();
        }

        Elements lv1StatsTable = lv1StatsFile.select("table").select("tbody").select("tr"),
                growthRatesTable = growthRatesFile.select("table").select("tbody").select("tr"),
                heroListTable = heroListFile.select("table").select("tbody").select("tr");

        lv1StatsTable.remove(0);
        growthRatesTable.remove(0);
        heroListTable.remove(0); //why is it getting the header

        if (lv1StatsTable.size()!=growthRatesTable.size()||growthRatesTable.size()!=heroListTable.size()) {
            System.out.println("unevenness detected; some units will be missing!");
        }

        while (lv1StatsTable.size()>0&&growthRatesTable.size()>0&&heroListTable.size()>0) {
            HeroConstructor
                    merge,
                    lv1StatsMerge = getLv1Constructor(lv1StatsTable.get(0)),
                    growthRatesMerge = getGrowthConstructor(growthRatesTable.get(0)),
                    heroListMerge = getListConstructor(heroListTable.get(0));

            try {
                merge = HeroConstructor.merge(lv1StatsMerge,growthRatesMerge);
            } catch (MismatchedInputException mie) {
                System.out.println("something was mismatched! (first)");
                mie.printStackTrace();
                greatest(greatest(lv1StatsTable, growthRatesTable), heroListTable).remove(0);
                continue;
            }

            try {
                merge = HeroConstructor.merge(merge,heroListMerge);
            } catch (MismatchedInputException mie) {
                System.out.println("something was mismatched! (second)");
                heroListTable.remove(0);
            }

            merge.setBaseKit(addBaseKit(merge.getFullName().toString()));
            merge.setGender(HERO_GENDERS.get(merge.getFullName().toString()));

            heroes.add(merge.createHero());

            lv1StatsTable.remove(0);
            growthRatesTable.remove(0);
            heroListTable.remove(0);
        }

        System.out.println("done ("+new BigDecimal((System.nanoTime()-start)/1000000000.0).round(new MathContext(3)) +" s)!");
        return heroes;
    }

    private static Elements greatest(Elements a, Elements b) {
        return a.size()>b.size()?a:b;
    }

    private static HeroConstructor getLv1Constructor(Element row) {
        Elements d = row.select("td");
        HeroConstructor c = new HeroConstructor();

        //0 is portrait
        c.setFullName(new HeroName(d.get(1).text()));

        /*
        String move = d.get(2).attributes().get("alt");
        System.out.println(move);
        c.setMoveType(move.substring(move.indexOf("Icon Class ")+"Icon Class ".length(),
                                move.indexOf(".png")));

        String weapon = d.get(3).attributes().get("alt");
        c.setWeaponType(weapon.substring(weapon.indexOf("Icon Move ")+"Icon Move ".length(),
                                weapon.indexOf(".png")));
        */

        //c.setMoveType(row.attributes().get("data-move-type"));
        //c.setWeaponType(row.attributes().get("data-weapon-type"));

        int[] stats = new int[5];
        stats[0] = Integer.parseInt(d.get(4).text());
        stats[1] = Integer.parseInt(d.get(5).text());
        stats[2] = Integer.parseInt(d.get(6).text());
        stats[3] = Integer.parseInt(d.get(7).text());
        stats[4] = Integer.parseInt(d.get(8).text());
        c.setStats(stats);

        //9 is total

        return c;
    }
    private static HeroConstructor getGrowthConstructor(Element row) {
        Elements d = row.select("td");
        HeroConstructor c = new HeroConstructor();


        c.setFullName(new HeroName(d.get(1).text()));
        //2 is move
        //3 is weapon (already covered)
        //4 is lv1 totals
        //5 is total growths
        //6 is both of those for some reason
        int[] growths = new int[5];
        for (int i=0; i<growths.length; i++) { //7-11
            String t = d.get(i+7).text();
            growths[i] = Integer.parseInt(t.substring(0, t.indexOf('%')));
        }

        c.setGrowths(growths);
        c.setDateReleased(parseDate(d.get(12).text()));

        return c;
    }
    private static HeroConstructor getListConstructor(Element row) {
        Elements d = row.select("td");
        HeroConstructor c = new HeroConstructor();

        String[] urlSet = d.get(0).select("a")
                .get(0).select("img")
                .get(0).attr("srcset")
                .split(" ");

        c.setPortraitLink(urlSet[2]);

        c.setFullName(new HeroName(d.get(1).text()));
        c.setOrigin(d.get(2).text());

        c.setMoveType(row.attributes().get("data-move-type"));
        c.setWeaponType(row.attributes().get("data-weapon-type"));
        //4 is weapon

        String r = d.get(5).text();
        try {
            c.setRarity(Integer.parseInt(String.valueOf(r.charAt(0))));
        } catch (NumberFormatException nfe) {
            if (DEBUG) nfe.printStackTrace();
            c.setRarity(-1);
        }

        Availability availability;

        try {
            if (r.indexOf('–') >= 0)
                r = r.substring(r.indexOf('–') + 4);
            else
                if (r.indexOf(' ')>=0)
                    r = r.substring(r.indexOf(' ') + 1);
                else r = "";
        } catch (IndexOutOfBoundsException ioobe) {
            r = "";
        }

        switch (r) { //reassigns to the same value a lot but whatever
            case "":
                availability = Availability.NORMAL;
                break;
            case "*":
                availability = Availability.NORMAL_RARITY_CHANGED;
                break;
            case "Story":
                availability = Availability.STORY;
                break;
            case "Grand Hero Battle":
                availability = Availability.GHB;
                break;
            case "Tempest Trials":
                availability = Availability.TT;
                break;
            //seasonal/legendary/MyThIC heroes are not part of normal pools
            case "Special":
                availability = Availability.SEASONAL;
                break;
            case "Legendary":
                availability = Availability.LEGENDARY;
                break;
            case "Mythic": //hopefully one day
                availability = Availability.MYTHIC;
                break;
            default:
                throw new Error("obtaining method wasn't accounted for: " +
                        "\""+r+"\" of \""+d.get(1).text()+"\"");
        }

        c.setAvailability(availability);

        //6 is release

        return c;
    }

    private static ArrayList<Skill> addBaseKit(String heroName) {
        ArrayList<Skill> baseKit;

        try {
            baseKit = SkillDatabase.HERO_SKILLS.get(heroName);
        } catch (NoSuchElementException f) {
            throw new Error("could not find base kit for "+heroName);
        }

        return baseKit;
    }



    private static HashMap<String, Character> HERO_GENDERS = getGenders();

    private static HashMap<String, Character> getGenders() {
        File f = new File("./src/feh/heroes/grender.txt");
        HashMap<String, Character> genders = new HashMap<>();

        Scanner input;
        try {
            input = new Scanner(f);
        } catch (FileNotFoundException fnfe) {
            throw new Error();
        }

        while (input.hasNextLine()) {
            String[] items = input.nextLine().split("\t");
            genders.put(items[0], items[1].charAt(0));
        }

        return genders;
    }



    private static GregorianCalendar parseDate(String date) throws NumberFormatException {
        //TODO: make this less basic
        String[] nums = date.split("[-/]");
        int year, month, day;
        year = Integer.parseInt(nums[0]);
        switch (Integer.parseInt(nums[1])-1) {
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
                throw new NumberFormatException("invalid integer: "+nums[0]);
        }
        day = Integer.parseInt(nums[2]);
        //honestly am i really going to make a switch case for an int to find a field which is just another int (which is one less than the already-defined int)
        //yes
        return new GregorianCalendar(year, month, day);
    }
}
