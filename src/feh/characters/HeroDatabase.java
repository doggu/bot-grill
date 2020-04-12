package feh.characters;

import feh.FEHeroesCache;
import feh.characters.hero.*;
import feh.characters.hero.constructionSite.HeroConstructor;
import feh.characters.hero.constructionSite.MismatchedInputException;
import feh.characters.skills.SkillDatabase;
import feh.characters.skills.skillTypes.Skill;
import main.BotMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.Stopwatch;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HeroDatabase extends Database<Hero> {
    @Override
    public ArrayList<Hero> findAll(String input) {
        ArrayList<Hero> candidates = new ArrayList<>();

        boolean newestOnly = false, oldestOnly = false;

        ArrayList<String> args =
                new ArrayList<>(Arrays.asList(input.split(" ")));

        for (int i=0; i<args.size(); i++) {
            String x = args.get(i);
            //test for "new" keyword
            if (x.equalsIgnoreCase("new")) {
                newestOnly = true;
                args.remove(i);
                i--;
            } else if (x.equalsIgnoreCase("old")) {
                oldestOnly = true;
                args.remove(i);
                i--;
            }
        }

        //finding units with the correct name
        for (int i=0; i<args.size(); i++) {
            String x = args.get(i);
            //test for name/epithet arguments
            boolean epithetIncluded = false;

            if (x.contains(":")) {
                x = x.substring(0, x.indexOf(":"));
                //System.out.println(x);
                epithetIncluded = true;
            }
            //find HEROES of the correct name
            for (Hero c: HeroDatabase.HEROES) {
                if (c.getFullName().getName().equalsIgnoreCase(x))
                    candidates.add(c);
                if (c.getFullName().getName().toLowerCase()
                        .indexOf(x.toLowerCase()) == 0) {
                    try {
                        if (c.getFullName().getName()
                                .equalsIgnoreCase(
                                        x+" "+args.get(i+1))) {
                            candidates.add(c);
                        }
                    } catch (IndexOutOfBoundsException ioobe) {
                        //break;
                    }
                }
            }

            if (epithetIncluded) {
                boolean foundMatch = HeroDatabase.HEROES.size()==1;
                //find HEROES (from list of valid names) of the correct epithet

                i++;
                while (!foundMatch&&i<args.size()) {
                    for (int j = 0; j < candidates.size(); j++) {
                        Hero c = candidates.get(j);
                        if (!c.getFullName().getEpithet().toLowerCase()
                                .contains(args.get(i).toLowerCase())) {
                            candidates.remove(j);
                            j--;
                        }
                    }

                    i++;
                    foundMatch = HeroDatabase.HEROES.size()==1;
                }
            }
        }

        //whittle down candidates list based on character properties
        if (candidates.size()>1) {
            if (newestOnly) {
                Hero newestHero = candidates.get(0);
                for (Hero x:candidates) {
                    if (x.getReleaseDate().getTimeInMillis() >
                            newestHero.getReleaseDate().getTimeInMillis()) {
                        newestHero = x;
                    }
                }
                candidates.clear();
                candidates.add(newestHero);
            } else if (oldestOnly) {
                Hero oldestHero = candidates.get(0);
                for (Hero x:candidates) {
                    if (x.getReleaseDate().getTimeInMillis() <
                            oldestHero.getReleaseDate().getTimeInMillis()) {
                        oldestHero = x;
                    }
                }
                candidates.clear();
                candidates.add(oldestHero);
            } else {
                for (String x : args) {
                    x = x.toLowerCase();

                    //find movement type hints
                    //TODO: use big brein to make this less "coincidental"
                    //these heroes have some of these keywords in their names:
                    //Clair: Highborn Flier, Florina: Lovely Flier,
                    //Shanna: Sprightly Flier
                    MovementClass move;
                    switch (x) {
                        case "infantry":
                        case "inf":
                        case "foot":
                            move = MovementClass.INFANTRY;
                            break;
                        case "armor":
                        case "armored":
                            move = MovementClass.ARMORED;
                            break;
                        case "horse":
                        case "cavalry":
                        case "cav":
                            move = MovementClass.CAVALRY;
                            break;
                        case "flying":
                        case "flier":
                        case "flyer": //debatable
                            move = MovementClass.FLYING;
                            break;
                        default:
                            move = null;
                            break;
                    }

                    char gender; //bool?
                    switch (x) {
                        case "m":
                        case "man":
                        case "male":
                        case "boy":
                        case "boi":
                        case "gentleman":
                        case "dude":
                            gender = 'm';
                            break;
                        case "f":
                        case "female":
                        case "feman":
                        case "woman":
                        case "wamen":
                        case "lady":
                        case "girl":
                        case "dudette":
                            gender = 'f';
                            break;
                        default:
                            gender = 'n';
                    }

                    char color;
                    //find color hints
                    switch (x) {
                        case "r":
                        case "red":
                            color = 'r';
                            break;
                        case "b":
                        case "blue":
                            color = 'b';
                            break;
                        case "g":
                        case "green":
                            color = 'g';
                            break;
                        case "c":
                        case "gray":
                        case "grey":
                        case "colorless":
                            color = 'c';
                            break;
                        default:
                            color = 'n';
                            break;
                    }

                    //find weapon type hints
                    WeaponClass weapon;
                    switch (x) {
                        case "sword":
                            weapon = WeaponClass.SWORD;
                            break;
                        case "lance":
                            weapon = WeaponClass.LANCE;
                            break;
                        case "axe":
                            weapon = WeaponClass.AXE;
                            break;
                        case "tome":
                        case "magic":
                            weapon = WeaponClass.TOME;
                            break;
                        case "staff":
                        case "stave":
                            weapon = WeaponClass.STAFF;
                            break;
                        case "bow":
                        case "archer":
                            weapon = WeaponClass.BOW;
                            break;
                        case "dagger":
                            weapon = WeaponClass.DAGGER;
                            break;
                        case "breath":
                        case "dragon":
                            weapon = WeaponClass.BREATH;
                            break;
                        default:
                            weapon = null;
                            break;
                    }

                    for (int j = 0; j < candidates.size(); j++) {
                        Hero c = candidates.get(j);
                        if (move!=null) {
                            if (c.getMoveType()!=move) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                        if (color!='n') {
                            if (c.getColor()!=color) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                        if (weapon!=null) {
                            if (!c.getWeaponType().matches(weapon)) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                        if (gender!='n') {
                            if (c.getGender()!=gender) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                    }
                }
            }
        }

        return candidates;
    }


    public static HeroDatabase DATABASE;
    public static ArrayList<Hero> HEROES;


    private static final String HERO_SUBDIR = "/herodata/";

    private static final String
            LV1_STATS = "Level_1_stats_table",
            GROWTH_RATES = "Growth_rate_table",
            HERO_LIST = "List_of_Heroes";

    private static final FEHeroesCache
            LV1_STATS_FILE,
            GROWTH_RATES_FILE,
            HERO_LIST_FILE;

    private static final FEHeroesCache[] HERO_FILES;

    static {
        LV1_STATS_FILE = new FEHeroesCache(LV1_STATS, HERO_SUBDIR);
        GROWTH_RATES_FILE = new FEHeroesCache(GROWTH_RATES, HERO_SUBDIR);
        HERO_LIST_FILE = new FEHeroesCache(HERO_LIST, HERO_SUBDIR);

        HERO_GENDERS = getGenders();
        ARTISTS = getArtists();

        DATABASE = new HeroDatabase();
        HEROES = DATABASE.getList();
        HERO_FILES = new FEHeroesCache[]{
                LV1_STATS_FILE,
                GROWTH_RATES_FILE,
                HERO_LIST_FILE,
        };
    }


    @Override
    protected WebCache[] getOnlineResources() {
        return HERO_FILES; }

    @Override
    public Hero getRandom() {
        return HEROES.get((int) (Math.random()*HEROES.size())); }

    protected ArrayList<Hero> getList() {
        System.out.print("processing heroes... ");

        Stopwatch pTime = new Stopwatch();
        pTime.start();

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

        Elements lv1StatsTable = lv1StatsFile
                        .select("table").select("tbody").select("tr"),
                growthRatesTable = growthRatesFile
                        .select("table").select("tbody").select("tr"),
                heroListTable = heroListFile
                        .select("table").select("tbody").select("tr"),
                artistsTable = heroListFile
                        .select("table").select("tbody").select("tr");

        lv1StatsTable.remove(0);
        growthRatesTable.remove(0);
        heroListTable.remove(0); //why is it getting the header

        if (lv1StatsTable.size()!=growthRatesTable.size() ||
                growthRatesTable.size()!=heroListTable.size()) {
            System.out.println("unevenness detected; " +
                    "some units will be missing!");
        }

        while (lv1StatsTable.size()>0 &&
                growthRatesTable.size()>0 &&
                heroListTable.size()>0) {
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
                greatest(greatest(lv1StatsTable, growthRatesTable), heroListTable)
                        .remove(0);
                continue;
            }

            try {
                merge = HeroConstructor.merge(merge,heroListMerge);
            } catch (MismatchedInputException mie) {
                System.out.println("something was mismatched! (second)");
                heroListTable.remove(0);
            }

            merge.setBaseKit(addBaseKit(merge.getFullName()));
            merge.setGender(HERO_GENDERS.get(merge.getFullName().toString()));
            merge.setArtist(ARTISTS.get(merge.getFullName().toString()));

            try {
                merge.setGamepediaLink(
                        new URL("https://feheroes.gamepedia.com/" +
                        merge.getFullName().toString()
                                .replace(" ", "_")));
            } catch (MalformedURLException murle) {
                System.out.println(merge.toString()+" couldn't produce a link!");
                merge.setGamepediaLink(null);
            }

            try {
                heroes.add(merge.createHero());
            } catch (Error e) {
                e.printStackTrace();
            }

            lv1StatsTable.remove(0);
            growthRatesTable.remove(0);
            heroListTable.remove(0);
            artistsTable.remove(0);
        }

        System.out.println(pTime.presentResult());
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

        try {
            c.setPortraitLink(new URL(urlSet[2]));
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
        }

        c.setFullName(new HeroName(d.get(1).text()));
        c.setOrigin(Origin.getOrigin(d.get(2).text()));

        c.setMoveType(row.attributes().get("data-move-type"));
        c.setWeaponType(row.attributes().get("data-weapon-type"));
        //4 is weapon

        String r = d.get(5).text();
        try {
            c.setRarity(Integer.parseInt(String.valueOf(r.charAt(0))));
        } catch (NumberFormatException nfe) {
            if (BotMain.DEBUG)
                System.out.println("issues getting rarity for " +
                        c.getFullName()+": "+nfe.getMessage()+
                        "\n\t\tsubstituting 3*-4*");
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

    private ArrayList<Skill> addBaseKit(HeroName heroName) {
        ArrayList<Skill> baseKit;

        try {
            baseKit = SkillDatabase.HERO_SKILLS.get(heroName.toString());
        } catch (NoSuchElementException f) {
            throw new Error("could not find base kit for "+heroName);
        }

        return baseKit;
    }

    private static final HashMap<String, Character> HERO_GENDERS;
    private static HashMap<String, Character> getGenders() {
        File f = new File("./src/feh/characters/grenders.txt");
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

    private static final HashMap<String, String> ARTISTS;
    private static HashMap<String, String> getArtists() {
        Document artistsFile;
        try {
            artistsFile = Jsoup.parse(
                    new FEHeroesCache("List_of_artists", HERO_SUBDIR),
                    "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }

        Elements rows = artistsFile
                .select("tr");
        rows.remove(0); //remove header which is in body for some reason

//        System.out.println(rows);

        HashMap<String, String> artists = new HashMap<>();

        for (Element row:rows) {
            Elements items = row.children();

            String artist = items.get(0).text();

            Elements characters = items.get(1).children().get(0).children();

            for (Element character:characters) {
                String name = character.select("a").get(0).attr("title");
                artists.put(name, artist);
            }
        }

        //todo: retrieve individual artists manually each time this happens
//        artists.put("Lyn: Lady of the Beach", "teffish");
//        artists.put("Mareeta: The Blade's Pawn", "kiyu");
//        artists.put("Tanith: Forthright Heart", "mattsun! (まっつん！)");
//        artists.put("Ewan: Eager Student", "azu‐taro (azuタロウ)");
//        artists.put("Tethys: Beloved Dancer", "tokki");
//        artists.put("Mareeta: Sword of Stars", "idk lmao");
//        artists.put("Tanya: Dagdar's Kid", "idk lmao");
//        artists.put("Jaffar: Angel of Night", "motsutsu");
//        artists.put("Anna: Wealth-Wisher", "hanekoto (はねこと)");
//        artists.put("Tsubasa: Madcap Idol", "azu‐taro (azuタロウ)");

        return artists;
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
        //honestly am i really going to make a switch case for an int to find a field which is just another int
                                                            //(which is one less than the already-defined int)
        //yes
        return new GregorianCalendar(year, month, day);
    }


    public static void main(String[] args) {
        HashMap<String, String> artists = getArtists();

        for (String art : artists.keySet()) {
            System.out.println(art);
        }
    }
}
