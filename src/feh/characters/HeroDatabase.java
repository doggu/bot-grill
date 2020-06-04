package feh.characters;

import com.google.gson.stream.JsonReader;
import feh.FEHeroesCache;
import feh.characters.hero.*;
import feh.characters.hero.constructionSite.HeroConstructor;
import feh.characters.hero.constructionSite.MismatchedInputException;
import feh.characters.skills.SkillDatabase;
import org.jetbrains.annotations.NotNull;
import utilities.Stopwatch;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HeroDatabase extends Database<Hero> {

    //todo: rewrite with scalper objects like the other listeners
    // maybe even create an abstraction to that whole process too
    @Override
    public ArrayList<Hero> findAll(@NotNull String input) {
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
                            weapon = WeaponClass.RED_SWORD;
                            break;
                        case "lance":
                            weapon = WeaponClass.BLUE_LANCE;
                            break;
                        case "axe":
                            weapon = WeaponClass.GREEN_AXE;
                            break;
                        case "tome":
                        case "magic":
                            weapon = WeaponClass.TOME;
                            break;
                        case "staff":
                        case "stave":
                            weapon = WeaponClass.COLORLESS_STAFF;
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
            HERO_INFO = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=Units" +
                    "&&fields=" +
                            "_pageName%3D" +
                            "Page%2C" +
                            "Name%3D" +
                            "Name%2C" +
                            "Title%3D" +
                            "Title%2C" +
                            "WikiName%3D" +
                            "WikiName%2C" +
                            "Person%3D" +
                            "Person%2C" +
                            "Origin%3D" +
                            "Origin%2C" +
                            "Entries__full%3D" +
                            "Entries%2C" +
                            "TagID%3D" +
                            "TagID%2C" +
                            "IntID%3D" +
                            "IntID%2C" +
                            "Gender%3D" +
                            "Gender%2C" +
                            "WeaponType%3D" +
                            "WeaponType%2C" +
                            "MoveType%3D" +
                            "MoveType%2C" +
                            "GrowthMod%3D" +
                            "GrowthMod%2C" +
                            "Artist%3D" +
                            "Artist%2C" +
                            "ActorEN__full%3D" +
                            "ActorEN%2C" +
                            "ActorJP__full%3D" +
                            "ActorJP%2C" +
                            "AdditionDate%3D" +
                            "AdditionDate%2C" +
                            "ReleaseDate%3D" +
                            "ReleaseDate%2C" +
                            "Properties__full%3D" +
                            "Properties%2C" +
                            "Description%3D" +
                            "Description" +
                    "&&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60Name%60%2C" +
                            "%60Title%60%2C" +
                            "%60WikiName%60%2C" +
                            "%60Person%60" +
                    "&limit=1024" +
                    "&format=json",

            // (Hero and WikiName) lv1 5* stats and 3* growth rates
            HERO_STATS = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=UnitStats" +
                    "&fields=" +
                            "_pageName%3DHero%2C" +
                            "WikiName%3DWikiName%2C" +
                            "Lv1HP5%3DLv1HP5%2C" +
                            "Lv1Atk5%3DLv1Atk5%2C" +
                            "Lv1Spd5%3DLv1Spd5%2C" +
                            "Lv1Def5%3DLv1Def5%2C" +
                            "Lv1Res5%3DLv1Res5%2C" +
                            "HPGR3%3DHPGR3%2C" +
                            "AtkGR3%3DAtkGR3%2C" +
                            "SpdGR3%3DSpdGR3%2C" +
                            "DefGR3%3DDefGR3%2C" +
                            "ResGR3%3DResGR3" +
                    "&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60WikiName%60%2C" +
                            "%60Lv1HP5%60%2C" +
                            "%60Lv1Atk5%60%2C" +
                            "%60Lv1Spd5%60" +
                    "&limit=1000" +
                    "&format=json",
            // complete rarity info, each object entry represents a period of
            // for a hero's rarity during a given time.
            HERO_RARITY = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=SummoningAvailability" +
                    "&fields=" +
                            "_pageName%3DPage%2C" +
                            "Rarity%3DRarity%2C" +
                            "NewHeroes%3DNewHeroes%2C" +
                            "StartTime%3DStartTime%2C" +
                            "EndTime%3DEndTime" +
                    "&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60Rarity%60%2C" +
                            "%60NewHeroes%60%2C" +
                            "%60StartTime%60%2C" +
                            "%60EndTime%60" +
                    "&limit=5000" +
                    "&format=json";

    private static final FEHeroesCache
            HERO_STATS_FILE,
            HERO_INFO_FILE,
            HERO_RARITY_FILE;

    private static final FEHeroesCache[] HERO_FILES;

    static {
        HERO_STATS_FILE = new FEHeroesCache(HERO_STATS, HERO_SUBDIR);
        HERO_INFO_FILE = new FEHeroesCache(HERO_INFO, HERO_SUBDIR);
        HERO_RARITY_FILE = new FEHeroesCache(HERO_RARITY, HERO_SUBDIR);

        HERO_FILES = new FEHeroesCache[]{
                HERO_STATS_FILE,
                HERO_INFO_FILE,
                HERO_RARITY_FILE,
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

                //it's actually a special unit (seasonal, legendary, etc.)
                merge.setSummonableRarity(
                        Objects.requireNonNullElse(rarity, 5));

                merge.setPortraitLink(
                        new URL("https://i.redd.it/a8ezuq39lvn21.jpg"));

                merge.setBaseKit(SkillDatabase.HERO_SKILLS.get(merge.getFullName().toString()));
                heroes.add(merge.createHero());
            } catch (MismatchedInputException|MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return heroes;
    }

    private ArrayList<HeroConstructor> getHeroInfo() throws IOException {
        //noinspection MismatchedQueryAndUpdateOfCollection
        ArrayList<String> allProperties = new ArrayList<>();

        ArrayList<HeroConstructor> merges = new ArrayList<>();

        JsonReader reader = new JsonReader(new FileReader(HERO_INFO_FILE));
        reader.beginArray();
        while (reader.hasNext()) {

            //open new hero
            reader.beginObject();

            //page
            reader.nextName();
            reader.nextString();

            //todo: create name relationship between different identifiers
            //e.x.  Subaki: Perfect Expert
            //      Subaki Perfect Expert
            //      the tagID thing idk

            //name
            reader.nextName();
            String name = reader.nextString();
            //epithet
            reader.nextName();
            String epithet = reader.nextString();
            //honestly fuck this not gonna import an entire fucking library so i
            //can fix Tharja: "Normal Girl"
            epithet = epithet.replaceAll("&quot;", "\"");


            //wikiname
            reader.nextName();
            reader.skipValue(); //String wikiname = reader.nextString();

            //"person"
            reader.nextName();
            reader.nextString();

            //origin
            reader.nextName();
            String origin = reader.nextString();

            //"entries"
            //Lif: Unifier of Thracia and Naga: Dragon Divinity are the only
            //units who have multiple cameos in the franchise (3 June 2020)
            reader.nextName();
            reader.skipValue();
//            reader.beginArray();
//            while (reader.hasNext()) {
//                String entry = reader.nextString();
//                if (!origin.equals(entry)) {
//                    System.out.println("found inequality for " +
//                            "\""+name+": "+epithet+"\": " +
//                            origin+" vs. "+entry);
//                }
//            }
//            reader.endArray();

            //"TagID"
            //todo: use this data to write a top-level "FEHObject" class
            reader.nextName();
            reader.nextString();

            //"IntID"
            reader.nextName();
            reader.nextInt();

            //gender
            reader.nextName();
            String gender = reader.nextString(); //Male, Female, MF or N

            //weapon type
            reader.nextName();
            String weaponType = reader.nextString();

            //move type
            reader.nextName();
            String moveType = reader.nextString();

            //"GrowthMod" (contains hidden growth info such as trainee status)
            reader.nextName();
            reader.skipValue(); //reader.nextString();

            //artist (unicode)
            //todo: create artist class with normal and romanized name
            reader.nextName();
            String artist = reader.nextString();

            //english VA
            reader.nextName();
            reader.skipValue();
//            reader.beginArray();
//            while (reader.hasNext())
//                reader.skipValue();
//            reader.endArray();

            //japanese VA
            reader.nextName();
            reader.skipValue();
//            reader.beginArray();
//            while (reader.hasNext())
//                reader.skipValue();
//            reader.endArray();

            //"AdditionDate" (probably when assets were first made available)
            reader.nextName();
            String additionDate = reader.nextString();

            //release date
            reader.nextName();
            String releaseDate = reader.nextString();

            //properties
            reader.nextName();
            reader.beginArray();
            ArrayList<String> properties = new ArrayList<>();
            while (reader.hasNext()) {
                properties.add(reader.nextString());
            }
            allProperties.addAll(properties);
            reader.endArray();
            Availability availability = Availability.NORMAL;
            boolean addToList = true;
            for (String s:properties) {
                switch(s) {
                    case "enemy":
                    case "generic":
                        addToList = false;
                        break;
                    case "story":
                        //alfonse?
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        //break;
                    case "prologue":
                        //mask man? no actually just matthew and takumi i guess
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        availability = Availability.STORY;
                        break;
                    case "special":
                        availability = Availability.SEASONAL;
                        break;
                    case "legendary":
                        availability = Availability.LEGENDARY;
                        break;
                    case "mythic":
                        availability = Availability.MYTHIC;
                        break;
                    case "limited":
                        //describes units who have a finite number of copies
                        //per barracks (ghb, tempest, story, etc.)
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "ghb":
                        availability = Availability.GHB;
                        break;
                    case "tempest":
                        availability = Availability.TT;
                        break;
                    case "brave":
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "refresher":
                        //this'll be useful to keep for game logic
                        // (no dancing dancers)
                        // (though you can unequip their refresh skill)
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "duo":
                        break;
                    case "revivalOnly":
                        //denotes units who were cut from the 5* random pool
                        // (they'll only appear in revival banners)
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "demoted_240":
                    case "demoted_201904":
                    case "demoted_202004":
                        availability = Availability.NORMAL_RARITY_CHANGED;
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "removed_201904":
                        //units who were removed from 5* random pool
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "resplendent":
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "specDisplay":
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                    case "hair":
                    case "hat":
                    case "tiara":
//                        System.out.println("me "+s+"\n"+name+": "+epithet);
                        break;
                }
            }
            if (!addToList) {
                while (reader.hasNext()) {
                    reader.skipValue();
                }
                reader.endObject();
                continue;
            }

            //description
            reader.nextName();
            String description = reader.nextString();

            //"AdditionDate__precision"
            reader.nextName();
            reader.nextString(); //i sure hope numbers resolve to strings

            //"ReleaseDate__precision
            reader.nextName();
            reader.nextString();

            reader.endObject();

            HeroConstructor merge = new HeroConstructor();

            merge.setFullName(new HeroName(name, epithet));
            merge.setOrigin(Origin.getOrigin(origin));
            merge.setGender(gender.equals("Male")?'m':'f');
            merge.setWeaponType(weaponType);
            merge.setMoveType(moveType);
            merge.setArtist(artist);
            merge.setAvailability(availability);
            merge.setDescription(description);

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

//        for (int i=0; i<allProperties.size(); i++) {
//            if (allProperties.get(i).equals("")) {
//                allProperties.remove(i);
//                i--;
//            }
//        }
//
//        for (int i=0; i<allProperties.size(); i++) {
//            String iS = allProperties.get(i);
//            for (int j=i+1; j<allProperties.size(); j++) {
//                if (iS.equals(allProperties.get(j))) {
//                    allProperties.remove(j);
//                    j--;
//                }
//            }
//        }

//        for (String s:allProperties)
//            System.out.println(s);

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

        JsonReader reader = new JsonReader(new FileReader(HERO_RARITY_FILE));
        reader.beginArray();
        while (reader.hasNext()) {
            //open new hero
            reader.beginObject();

            //page
            reader.nextName();
            String name = reader.nextString();
            //honestly fuck this not gonna import an entire fucking library so i
            //can fix Tharja: "Normal Girl"
            name = name.replaceAll("&quot;", "\"");

            //rarity
            reader.nextName();
            int rarity = reader.nextInt();

            //"NewHeroes" (special?)
            reader.nextName();
            reader.skipValue();
//            boolean newHeroes = reader.nextInt()==1;

            //start time
            reader.nextName();
            String startDate = reader.nextString();

            //end time
            reader.nextName();
            String endDate = reader.nextString();

            //date precision data or smtg
            reader.nextName();
            reader.nextInt();
            reader.nextName();
            reader.nextInt();

            reader.endObject();

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
        System.out.println("hello, world!");
    }
}
