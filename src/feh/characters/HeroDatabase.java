package feh.characters;

import com.google.gson.stream.JsonReader;
import feh.FEHeroesCache;
import feh.characters.hero.*;
import feh.characters.hero.constructionSite.HeroConstructor;
import feh.characters.hero.constructionSite.MismatchedInputException;
import feh.characters.skills.SkillDatabase;
import utilities.Stopwatch;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;

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
                        .indexOf(x.toLowerCase()) != 0)
                      //account for incompletely constructed hero names
                    continue;

                try {
                    if (c.getFullName().getName()
                            .equalsIgnoreCase(x+" "+args.get(i+1)))
                        candidates.add(c);
                } catch (IndexOutOfBoundsException ioobe) {
                    //there was no epithet after all
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
            /*
            "page", name, title (epithet), wikiname, person (?),
             origin, entries (array), TagID, IntID, gender, weapontype,
             movetype, growthmod, artist (with spooky moon runes not rōmaji),
             actorEN (array), actorJP (array),
             addition date (release), release date (idk),
             "properties" (useful, tells if read unit was demoted or an enemy)
             description
             precision stuff???
                    i will probably eventually filter out the stuff i don't want
             */
            HERO_INFO = "https://feheroes.gamepedia.com/index.php?title=Special:CargoExport&tables=Units&&fields=_pageName%3DPage%2CName%3DName%2CTitle%3DTitle%2CWikiName%3DWikiName%2CPerson%3DPerson%2COrigin%3DOrigin%2CEntries__full%3DEntries%2CTagID%3DTagID%2CIntID%3DIntID%2CGender%3DGender%2CWeaponType%3DWeaponType%2CMoveType%3DMoveType%2CGrowthMod%3DGrowthMod%2CArtist%3DArtist%2CActorEN__full%3DActorEN%2CActorJP__full%3DActorJP%2CAdditionDate%3DAdditionDate%2CReleaseDate%3DReleaseDate%2CProperties__full%3DProperties%2CDescription%3DDescription&&order+by=%60_pageName%60%2C%60Name%60%2C%60Title%60%2C%60WikiName%60%2C%60Person%60&limit=1024&format=json",

            // (Hero and WikiName) lv1 5* stats and 3* growth rates
            HERO_STATS = "https://feheroes.gamepedia.com/index.php?title=Special:CargoExport&tables=UnitStats&&fields=_pageName%3DHero%2CWikiName%3DWikiName%2CLv1HP5%3DLv1HP5%2CLv1Atk5%3DLv1Atk5%2CLv1Spd5%3DLv1Spd5%2CLv1Def5%3DLv1Def5%2CLv1Res5%3DLv1Res5%2CHPGR3%3DHPGR3%2CAtkGR3%3DAtkGR3%2CSpdGR3%3DSpdGR3%2CDefGR3%3DDefGR3%2CResGR3%3DResGR3&&order+by=%60_pageName%60%2C%60WikiName%60%2C%60Lv1HP5%60%2C%60Lv1Atk5%60%2C%60Lv1Spd5%60&limit=1000&format=json",
            // complete rarity info, each object entry represents a period of
            // for a hero's rarity during a given time.
            HERO_RARITY = "https://feheroes.gamepedia.com/index.php?title=Special:CargoExport&tables=SummoningAvailability&&fields=_pageName%3DPage%2CRarity%3DRarity%2CNewHeroes%3DNewHeroes%2CStartTime%3DStartTime%2CEndTime%3DEndTime&&order+by=%60_pageName%60%2C%60Rarity%60%2C%60NewHeroes%60%2C%60StartTime%60%2C%60EndTime%60&limit=5000&format=json";

//            ARTISTS_URL = "https://feheroes.gamepedia.com/index.php?title=Special:CargoExport&tables=Artists&&fields=_pageName%3DPage%2CNameUSEN%3DNameUSEN%2CName%3DName%2CCompany%3DCompany&&order+by=%60_pageName%60%2C%60NameUSEN%60%2C%60Name%60%2C%60Company%60&limit=1024&format=json";
//            LV1_STATS = "https://feheroes.gamepedia.com/Level_1_stats_table",
//            GROWTH_RATES = "https://feheroes.gamepedia.com/Growth_rate_table",
//            HERO_LIST = "https://feheroes.gamepedia.com/List_of_Heroes",
//            ARTISTS_URL = "https://feheroes.gamepedia.com/List_of_artists";

    private static final FEHeroesCache
            HERO_STATS_FILE,
            HERO_INFO_FILE,
            HERO_RARITY_FILE;
//            LV1_STATS_FILE = null,
//            GROWTH_RATES_FILE = null,
//            HERO_LIST_FILE = null;

    private static final FEHeroesCache[] HERO_FILES;

    static {
        HERO_STATS_FILE = new FEHeroesCache(HERO_STATS, HERO_SUBDIR);
        HERO_INFO_FILE = new FEHeroesCache(HERO_INFO, HERO_SUBDIR);
        HERO_RARITY_FILE = new FEHeroesCache(HERO_RARITY, HERO_SUBDIR);

        HERO_FILES = new FEHeroesCache[]{
                HERO_STATS_FILE,
                HERO_INFO_FILE,
                HERO_RARITY_FILE,
//                LV1_STATS_FILE,
//                GROWTH_RATES_FILE,
//                HERO_LIST_FILE,
        };

        DATABASE = new HeroDatabase();
        HEROES = DATABASE.getList();
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

        ArrayList<HeroConstructor> infoMerges, statMerges;
        HashMap<String, Integer> rarities;

        try {
            infoMerges = getHeroInfo();
            statMerges = getHeroStats();
            rarities = getHeroRarities();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        if (infoMerges.size()!=statMerges.size()) {
            System.out.println("unequal constructor list sizes; aborting");
            System.out.println("\t\t("+infoMerges.size()+", "+statMerges.size()+")");
            return null;
        }

        for (int i=0; i<infoMerges.size(); i++) {
            try {
                HeroConstructor merge = HeroConstructor.merge(
                        infoMerges.get(i),
                        statMerges.get(i));

                Integer rarity = rarities.get(merge.getFullName().toString());

                if (rarity==null) {
                    //it's actually a special unit (seasonal, legendary, etc.)
                    merge.setSummonableRarity(5);
                    //not right but who cares im just tryna compile bro
                    merge.setAvailability(Availability.LEGENDARY);
//                    System.out.println("rarity not found for " +
//                            merge.getFullName());
//                    continue;
                } else {
                    merge.setSummonableRarity(rarity);
                    merge.setAvailability(Availability.NORMAL);
                }

                merge.setPortraitLink(
                        new URL("https://i.redd.it/a8ezuq39lvn21.jpg"));

                heroes.add(merge.createHero());
            } catch (MismatchedInputException|MalformedURLException e) {
                e.printStackTrace();
            }
        }

        /*
        Document
                lv1StatsFile,
                growthRatesFile,
                heroListFile;
        try {
            lv1StatsFile =
                    Jsoup.parse(LV1_STATS_FILE, "UTF-8");
            growthRatesFile =
                    Jsoup.parse(GROWTH_RATES_FILE, "UTF-8");
            heroListFile =
                    Jsoup.parse(HERO_LIST_FILE, "UTF-8");
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

        //todo: temporary fixes in place while the growth rates table is missing
        // all units including and after (alphabetically):
        //      "Wolt: Sunbeam Archer"
        // while i'm at it i should separate the whiles into foreach with mild
        // near-sorted matching algorithm at the end instead to make stuff like
        // this easier to account for
        while (lv1StatsTable.size()>0 &&
//                growthRatesTable.size()>0 &&
                heroListTable.size()>0) {
            HeroConstructor
                    merge,
                    lv1StatsMerge = getLv1Constructor(lv1StatsTable.get(0)),
                    heroListMerge = getListConstructor(heroListTable.get(0));

            //todo: ^
            HeroConstructor growthRatesMerge;
            try {
                growthRatesMerge =
                        getGrowthConstructor(growthRatesTable.get(0));
            } catch (IndexOutOfBoundsException ioobe) {
                growthRatesMerge = new HeroConstructor();
                int[] growths;
                GregorianCalendar dateReleased;
                switch (lv1StatsMerge.getFullName().toString()) {
                    //todo: actually fill in proper values
                    case "Valter: Dark Moonstone":
                    case "Velouria: Wolf Cub":
                    case "Veronica: Brave Princess":
                    case "Veronica: Spring Princess":
                    case "Virion: Elite Archer":
                    case "Walhart: The Conqueror":
                        growths = new int[]{ 50, 60, 50, 65, 30 };
                        dateReleased = new GregorianCalendar(
                                2018,Calendar.JULY,29,
                                23,59);
                        break;
                    case "Wolt: Sunbeam Archer":
                        growths = new int[]{ 50, 60, 60, 45, 30 };
                        dateReleased = new GregorianCalendar(
                                2019,Calendar.JULY,9,
                                23,59);
                        break;
                    case "Wrys: Kindly Priest":
                        growths = new int[]{ 55, 45, 40, 40, 60 };
                        dateReleased = new GregorianCalendar(
                                2017,Calendar.FEBRUARY,2,
                                23,59);
                        break;
                    case "Xander: Dancing Knight":
                        growths = new int[]{ 50, 65, 30, 65, 30 };
                        dateReleased = new GregorianCalendar(
                                2018, Calendar.AUGUST, 10,
                                23,59);
                        break;
                    case "Xander: Paragon Knight":
                        growths = new int[]{ 55, 55, 45, 65, 30 };
                        dateReleased = new GregorianCalendar(
                                2017,Calendar.FEBRUARY,2,
                                23,59);
                        break;
                    case "Xander: Spring Prince":
                        growths = new int[]{ 50, 45, 50, 60, 45 };
                        dateReleased = new GregorianCalendar(
                                2017,Calendar.MARCH,30,
                                23,59);
                        break;
                    case "Xander: Student Swimmer":
                        growths = new int[]{ 55, 55, 55, 65, 25 };
                        dateReleased = new GregorianCalendar(
                                2017,Calendar.JULY,28,
                                23,59);
                        break;
                    case "Yarne: Timid Taguel":
                        growths = new int[]{ 50, 60, 60, 55, 30 };
                        dateReleased = new GregorianCalendar(
                                2019,Calendar.JUNE,11,
                                23,59);
                        break;
                    case "Ylgr: Breaking the Ice":
                        growths = new int[]{ 60, 60, 55, 55, 45 };
                        dateReleased = new GregorianCalendar(
                                2019, Calendar.JUNE, 24,
                                23,59);
                        break;
                    case "Ylgr: Fresh Snowfall":
                        growths = new int[]{ 50, 60, 65, 35, 35 };
                        dateReleased = new GregorianCalendar(
                                2019, Calendar.JUNE, 24,
                                23,59);
                        break;
                    case "Yune: Chaos Goddess":
                        growths = new int[]{ 50, 55, 50, 25, 65 };
                        dateReleased = new GregorianCalendar(
                                2019, Calendar.MARCH, 29,
                                23,59);
                        break;
                    case "Zelgius: Jet-Black General":
                        growths = new int[]{ 55, 60, 60, 65, 35 };
                        dateReleased = new GregorianCalendar(
                                2018, Calendar.JANUARY, 12,
                                23,59);
                        break;
                    case "Zephiel: The Liberator":
                        growths = new int[]{ 70, 60, 30, 60, 45 };
                        dateReleased = new GregorianCalendar(
                                2017, Calendar.APRIL, 20,
                                23,59);
                        break;
                    case "Zephiel: Winter's Crown":
                        growths = new int[]{ 60, 65, 35, 70, 55 };
                        dateReleased = new GregorianCalendar(
                                2019, Calendar.DECEMBER, 16,
                                23,59);
                        break;
                    default:
                        System.out.println("HEY HEY HYE LISTEN LISTEN" +
                                "THERE'S ANOTHER UNIT MISSING FROM GROWTHS\n" +
                                "it's "+lv1StatsMerge.getFullName().toString() +
                                " btw");
                        growths = new int[5];
                        dateReleased = new GregorianCalendar(
                                1, Calendar.DECEMBER, 1,
                                23,59);
                }

                growthRatesMerge.setGrowths(growths);
                growthRatesMerge.setDateReleased(dateReleased);
            }

            try {
                merge = HeroConstructor.merge(lv1StatsMerge,growthRatesMerge);
            } catch (MismatchedInputException mie) {
                System.out.println("something was mismatched! (first)");
                mie.printStackTrace();
                greatest(
                        greatest(lv1StatsTable, growthRatesTable),
                        heroListTable)
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
                System.out.println(merge.toString() +
                        " couldn't produce a link!");
                merge.setGamepediaLink(null);
            }

            try {
                heroes.add(merge.createHero());
            } catch (Error e) {
                e.printStackTrace();
            }


            //todo: ^
            try {
                growthRatesTable.remove(0);
            } catch (IndexOutOfBoundsException ioobe) {
                //don't need that
            }

            lv1StatsTable.remove(0);
            heroListTable.remove(0);
            artistsTable.remove(0);
        }

        System.out.println(pTime.presentResult());
         */

        return heroes;
    }

    private ArrayList<HeroConstructor> getHeroInfo() throws IOException {
        ArrayList<HeroConstructor> merges = new ArrayList<>();

        JsonReader infoReader = new JsonReader(new FileReader(HERO_INFO_FILE));
        infoReader.beginArray();
        while (infoReader.hasNext()) {
            //open new hero
            infoReader.beginObject();

            //page
            infoReader.nextName();
            infoReader.nextString();

            //name
            infoReader.nextName();
            String name = infoReader.nextString();
            //epithet
            infoReader.nextName();
            String epithet = infoReader.nextString();
            //honestly fuck this not gonna import an entire fucking library so i
            //can fix Tharja: "Normal Girl"
            epithet = epithet.replaceAll("&quot;", "\"");

            //wikiname
            infoReader.nextName();
            infoReader.nextString();

            //"person"
            infoReader.nextName();
            infoReader.nextString();

            //origin
            infoReader.nextName();
            String origin = infoReader.nextString();

            //"entries"
            infoReader.nextName();
            infoReader.skipValue();
//            infoReader.beginArray();
//            //idk what this means, seems identical to origin so far
//            while (infoReader.hasNext())
//                infoReader.skipValue();
//            infoReader.endArray();

            //"TagID"
            infoReader.nextName();
            infoReader.nextString();

            //"IntID"
            infoReader.nextName();
            infoReader.nextInt();

            //gender
            infoReader.nextName();
            String gender = infoReader.nextString(); //Male, Female, MF or N

            //weapon type
            infoReader.nextName();
            String weaponType = infoReader.nextString();

            //move type
            infoReader.nextName();
            String moveType = infoReader.nextString();

            //"GrowthMod"
            infoReader.nextName();
            infoReader.nextString();

            //artist (unicode)
            infoReader.nextName();
            String artist = infoReader.nextString();

            //english VA
            infoReader.nextName();
            infoReader.skipValue();
//            infoReader.beginArray();
//            while (infoReader.hasNext())
//                infoReader.skipValue();
//            infoReader.endArray();

            //japanese VA
            infoReader.nextName();
            infoReader.skipValue();
//            infoReader.beginArray();
//            while (infoReader.hasNext())
//                infoReader.skipValue();
//            infoReader.endArray();

            //"AdditionDate" (probably when assets were first made available)
            infoReader.nextName();
            String additionDate = infoReader.nextString();

            //release date
            infoReader.nextName();
            String releaseDate = infoReader.nextString();

            //properties
            infoReader.nextName();
            infoReader.beginArray();
            ArrayList<String> properties = new ArrayList<>();
            while (infoReader.hasNext())
                properties.add(infoReader.nextString());
            infoReader.endArray();
            if (properties.contains("enemy") || properties.contains("generic")) {
                while (infoReader.hasNext())
                    infoReader.skipValue();
                infoReader.endObject();
                continue;
            }

            //description
            infoReader.nextName();
            String description = infoReader.nextString();

            //"AdditionDate__precision"
            infoReader.nextName();
            infoReader.nextString(); //i sure hope numbers resolve to strings

            //"ReleaseDate__precision
            infoReader.nextName();
            infoReader.nextString();

            infoReader.endObject();

            HeroConstructor merge = new HeroConstructor();

            merge.setFullName(new HeroName(name, epithet));
//            merge.setWikiName(wikiname);
            merge.setOrigin(Origin.getOrigin(origin));
            merge.setGender(gender.equals("Male") ? 'M' : 'F'); //uM
            merge.setWeaponType(weaponType);
            merge.setMoveType(moveType);
            merge.setArtist(artist);

            GregorianCalendar trueRelease;
            try {
                trueRelease = parseDate(releaseDate);
            } catch (Error | Exception e) {
                try {
                    trueRelease = parseDate(additionDate);
                } catch (Error | Exception ee) {
                    trueRelease = null;
//                    ee.printStackTrace();
//                    continue; //safe to continue for reader
                }
            }
            merge.setDateReleased(trueRelease);

//            merge.setDescription(description);

            //heavy interpretation starts here
//            merge.setPortraitLink(new URL(
//                    "https://feheroes.gamepedia.com/File:" +
//                            merge.getFullName().toString()
//                                    .replace(":","")
//                                    .replace(" ", "_") +
//                            "_Face_FC.webp"
//            ));
            //it didn't work

            merges.add(merge);
        }

        return merges;
    }

    private ArrayList<HeroConstructor> getHeroStats() throws IOException {
        ArrayList<HeroConstructor> merges = new ArrayList<>();

        JsonReader infoReader = new JsonReader(new FileReader(HERO_STATS_FILE));
        infoReader.beginArray();
        while (infoReader.hasNext()) {
//            System.out.println("ReADInG sOmE stATs");

            //open new hero
            infoReader.beginObject();

            //name
            infoReader.nextName();
            String name = infoReader.nextString();
            //honestly fuck this not gonna import an entire fucking library so i
            //can fix Tharja: "Normal Girl"
            name = name.replaceAll("&quot;", "\"");

            //wikiname
            infoReader.nextName();
            String wikiname = infoReader.nextString();
            if (wikiname.contains("ENEMY")) {
//                System.out.println("NeveRMINd");
                while (infoReader.hasNext())
                    infoReader.skipValue();
                infoReader.endObject();
                continue;
            }

            //lv1 5* stats
            int[] stats = new int[5];
            for (int i=0; i<5; i++) {
                infoReader.nextName();
                stats[i] = infoReader.nextInt();
            }

            //3* growths
            int[] growths = new int[5];
            for (int i=0; i<5; i++) {
                infoReader.nextName();
                growths[i] = infoReader.nextInt();
            }

            infoReader.endObject();

            HeroConstructor merge = new HeroConstructor();

            merge.setFullName(new HeroName(name));
            merge.setStats(stats);
            merge.setGrowths(growths);

            merge.setGamepediaLink(
                    new URL("https://feheroes.gamepedia.com/" +
                            merge.getFullName().toString()
                                    .replace(" ", "_")));

            merge.setBaseKit(
                    SkillDatabase.HERO_SKILLS.get(
                            merge.getFullName().toString()));

            merges.add(merge);
        }

        return merges;
    }

    //todo: temporarily just a hashmap of hero names and integers until i get
    // all the rarity stuff figured out and compiled
    private HashMap<String, Integer> getHeroRarities() throws IOException {
        HashMap<String, Integer> rarities = new HashMap<>();

        JsonReader infoReader = new JsonReader(new FileReader(HERO_RARITY_FILE));
        infoReader.beginArray();
        while (infoReader.hasNext()) {
            //open new hero
            infoReader.beginObject();

            //page
            infoReader.nextName();
            String name = infoReader.nextString();
            //honestly fuck this not gonna import an entire fucking library so i
            //can fix Tharja: "Normal Girl"
            name = name.replaceAll("&quot;", "\"");

            //rarity
            infoReader.nextName();
            int rarity = infoReader.nextInt();

            //"NewHeroes" (special?)
            infoReader.nextName();
            boolean newHeroes = infoReader.nextInt()==1;

            //start time
            infoReader.nextName();
            String startDate = infoReader.nextString();

            //end time
            infoReader.nextName();
            String endDate = infoReader.nextString();

            //date precision data or smtg
            infoReader.nextName();
            infoReader.nextInt();
            infoReader.nextName();
            infoReader.nextInt();

            infoReader.endObject();

            Integer currentRarity = rarities.get(name);
            if (currentRarity==null || currentRarity>rarity) {
                rarities.put(name, rarity);
            }
        }

        return rarities;
    }


    private static GregorianCalendar parseDate(String date)
            throws NumberFormatException {
        //TODO: make this less basic
        String[] nums = date.split("[-/]");
        int year = Integer.parseInt(nums[0]),
            month = Integer.parseInt(nums[1])-1,
            day = Integer.parseInt(nums[2]);
        //noinspection MagicConstant
        return new GregorianCalendar(year, month, day);
    }


    public static void main(String[] args) {

    }
}
