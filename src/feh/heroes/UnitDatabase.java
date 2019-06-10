package feh.heroes;

import utilities.WebScalper;
import feh.heroes.character.Availability;
import feh.heroes.character.Hero;
import feh.heroes.character.HeroConstructor;
import feh.heroes.character.HeroName;
import feh.skills.Skill;
import feh.skills.SkillDatabase;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class UnitDatabase extends WebScalper {
    public static final ArrayList<Hero> HEROES = getList();

    private static final String
            LV1_STATS = "https://feheroes.gamepedia.com/Level_1_stats_table",
            GROWTH_RATES = "https://feheroes.gamepedia.com/Growth_rate_table",
            HERO_LIST = "https://feheroes.gamepedia.com/List_of_Heroes";



    private static int HERO_INDEX = 1;

    // one hero for each document:
    //
    // lv1Stats
    //
    // [7 lines of useless stuff]
    // [name]: [epithet]
    // [hp]
    // [atk]
    // [spd]
    // [def]
    // [res]
    // [total]
    // [name]: [epithet]
    // [hp]
    // [atk]
    // ...
    //
    //
    //
    // growths
    //
    // [12 lines of useless stuff]
    // [name]: [epithet]
    // [color] [weapon]
    // [movement]
    // [lv1 stat total]
    // [growth total]%
    // [lv1 stat total], [growth total]%
    // [hp growth]
    // [atk]
    // [spd]
    // [def]
    // [res]
    // [release date]
    // [name]: [epithet]
    // [color] [weapon]
    // ...
    //
    //
    //
    // heroList
    //
    // [7 lines of useless stuff]
    // [name]: [epithet]
    // [origin]
    // [rarity - lower bound]
    // -[rarity - upper bound]
    // [special indicator (*|Story|Grand Hero Battle|Tempest Trials|Legendary)]
    // [release date]
    // [name]: [epithet]
    // [origin]
    // [rarity - lower bound]
    // -[rarity - upper bound]
    // ...

    private static void updateCache() {
        BufferedReader lv1Stats, growthRates, heroList;

        lv1Stats = readWebsite(LV1_STATS);
        growthRates = readWebsite(GROWTH_RATES);
        heroList = readWebsite(HERO_LIST);



        IntStream lv1StatsTable = null, growthRatesTable = null, heroListTable = null;

        try {
            String line;
            while ((line = lv1Stats.readLine()) != null) {
                //the entire fucking table is on one line...
                if (line.contains("<table class=\"wikitable sortable\"")) lv1StatsTable = line.chars();
            }
            if (lv1StatsTable == null) {
                throw new Error("lv1StatsTable could not find the table");
            }

            while ((line = growthRates.readLine()) != null) {
                //same for all of em
                if (line.contains("<table class=\"wikitable default sortable\"")) growthRatesTable = line.chars();
            }
            if (growthRatesTable == null) {
                throw new Error("growthRatesTable could not find the table");
            }

            while ((line = heroList.readLine()) != null) {
                //ugh
                if (line.contains("<table class=\"wikitable default sortable\"")) heroListTable = line.chars();
            }
            if (heroListTable == null) {
                throw new Error("heroListTable could not find the table");
            }
        } catch (IOException g) {
            throw new Error("table finding ran into an error");
        }

        Iterator<String> lv1StatsData = getItems(lv1StatsTable).iterator();
        Iterator<String> growthRatesData = getItems(growthRatesTable).iterator();
        Iterator<String> heroListData = getItems(heroListTable).iterator();

        //remove initial junk data
        /*
        for (int i=0; i<7; i++)
            System.out.println(lv1StatsData.next());
        for (int i=0; i<12; i++)
            System.out.println(growthRatesData.next());
        for (int i=0; i<7; i++)
            System.out.println(heroListData.next());
        */

        for (int i=0; i<7; i++)
            lv1StatsData.next();
        for (int i=0; i<12; i++)
            growthRatesData.next();
        for (int i=0; i<7; i++)
            heroListData.next();

        File lv1StatsFile = new File("./src/feh/webCache/lv1StatsData.txt");
        File growthRatesFile = new File("./src/feh/webCache/growthRatesData.txt");
        File heroListFile = new File("./src/feh/webCache/heroListData.txt");
        File path = new File("./src/feh/webCache/");
        if (!path.mkdirs()) throw new Error("couldn't create filepath for hero data");
        try {
            if (!lv1StatsFile.createNewFile()) throw new Error("couldn't create lv1");
        } catch (IOException f) {
            throw new Error("IOException creating lv1");
        }
        try {
            if (!growthRatesFile.createNewFile()) throw new Error("couldn't create growths");
        } catch (IOException f) {
            throw new Error("IOException creating growths");
        }
        try {
            if (!heroListFile.createNewFile()) throw new Error("couldn't create heroList");
        } catch (IOException f) {
            throw new Error("IOException creating heroList");
        }


        FileWriter writer;

        try {
            writer = new FileWriter(lv1StatsFile);
            while (lv1StatsData.hasNext()) {
                writer.write(lv1StatsData.next());
                if (lv1StatsData.hasNext()) writer.write('\n');
            }
            writer.close();
        } catch (IOException f) {
            throw new Error("lv1StatsFile didnt exist or something");
        }

        try {
            writer = new FileWriter(growthRatesFile);
            while (growthRatesData.hasNext()) {
                writer.write(growthRatesData.next());
                if (growthRatesData.hasNext()) writer.write('\n');
            }
            writer.close();
        } catch (IOException f) {
            throw new Error("growthRatesFile didnt exist or something");
        }

        try {
            writer = new FileWriter(heroListFile);
            while (heroListData.hasNext()) {
                writer.write(heroListData.next());
                if (heroListData.hasNext()) writer.write('\n');
            }
            writer.close();
        } catch (IOException f) {
            throw new Error("heroListFile didnt exist or something");
        }
    }

    private static ArrayList<Hero> getList() {
        ArrayList<HeroConstructor> heroConstructors = new ArrayList<>();



        File    lv1StatsFile = new File("./src/feh/webCache/lv1StatsData.txt"),
                growthRatesFile = new File("./src/feh/webCache/growthRatesData.txt"),
                heroListFile = new File("./src/feh/webCache/heroListData.txt");



        Scanner lv1StatsData = null, growthRatesData = null, heroListData = null;

        int tries = 0;
        while (lv1StatsData==null||growthRatesData==null||heroListData==null) {
            try {
                lv1StatsData = new Scanner(lv1StatsFile);
                growthRatesData = new Scanner(growthRatesFile);
                heroListData = new Scanner(heroListFile);
            } catch (FileNotFoundException f) {
                updateCache();
                tries++;
                if (tries>5) {
                    throw new Error("it's time to stop");
                }
            }
        }



        while (lv1StatsData.hasNext()&&growthRatesData.hasNext()&&heroListData.hasNext()) {
            HeroConstructor x = new HeroConstructor();

            processLv1Stats(x, lv1StatsData);
            processGrowthRates(x, growthRatesData);
            processListOfHeroes(x, heroListData);
            addBaseKit(x);

            heroConstructors.add(x);
            HERO_INDEX++;
        }

        ArrayList<Hero> heroes = new ArrayList<>();
        for (HeroConstructor z:heroConstructors)
            heroes.add(z.createHero());



        System.out.println("finished processing heroes.");
        return heroes;
    }

    private static void processLv1Stats(HeroConstructor x, Scanner input) {
        String identification = input.nextLine();
        String[] id = identification.split(": ");
        x.setFullName(new HeroName(id[0], id[1]));



        int[] stats = new int[5];

        for (int i=0; i<stats.length; i++)
            stats[i] = Integer.parseInt(input.nextLine());

        x.setStats(stats);



        input.nextLine(); //total lv1 stats
    }
    private static void processGrowthRates(HeroConstructor x, Scanner input) {
        String[] id = input.nextLine().split(": ");

        if (!id[0].equals(x.getName()))
            System.out.println("GrR: misalignment detected for unit "+HERO_INDEX+" ("+id[0]+")");



        String typing = input.nextLine();
        x.setWeaponType(typing);



        String moveType = input.nextLine();
        x.setMoveType(moveType);



        input.nextLine(); //total lv1 stats
        input.nextLine(); //total stat growths
        input.nextLine(); //total lv1 stats and stat growths



        int[] statGrowths = new int[5];

        for (int i=0; i<statGrowths.length; i++) {
            String growth = input.nextLine().replace("%", "");
            statGrowths[i] = Integer.parseInt(growth);
        }

        x.setGrowths(statGrowths);



        GregorianCalendar releaseDate = parseDate(input.nextLine());
        x.setDateReleased(releaseDate);
    }
    private static void processListOfHeroes(HeroConstructor x, Scanner input) {
        String[] id = input.nextLine().split(": ");
        String name = id[0];
        if (id.length<2) throw new Error("improper name detected for unit "+HERO_INDEX);
        if (!id[0].equals(x.getName()))
            System.out.println("GrR: misalignment detected for unit "+HERO_INDEX+" ("+id[0]+")");



        String origin = input.nextLine();
        x.setOrigin(origin);



        String rarity = input.nextLine();
        int lowerRarityBound;
        try {
            lowerRarityBound = Integer.parseInt(rarity);
        } catch (NumberFormatException g) {
            throw new Error("error for character #"+HERO_INDEX+" ("+name+")\n" +
                    "attempted rarity: \""+rarity+"\"");
        }
        x.setRarity(lowerRarityBound);



        String indeterminate = input.nextLine();

        /*
         * parsing these nextLine few lines:
         *
         * line 1
         * if "-" index = 0, upper rarity bound
         *      nextLine
         * if "-" is contained, release date
         * if "-" is not contained, availability note
         *      nextLine
         *      parse release date (or not)
         */

        if (indeterminate.indexOf("–")==0) {
            //int upperRarityBound = Integer.parseInt(indeterminate.substring(1));
            indeterminate = input.nextLine();
        }                         //this is an em dash btw

        Availability availability;

        if (indeterminate.contains("-")) {
            availability = Availability.NORMAL;
            //release date
        } else {
            switch (indeterminate) { //reassigns to the same value a lot but whatever
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
                    throw new Error("obtaining method wasn't accounted for: "+indeterminate);
            }

            input.nextLine(); //release date
        }

        x.setAvailability(availability);
    }
    private static void addBaseKit(HeroConstructor x) {
        ArrayList<Skill> baseKit;
        try {
            baseKit = SkillDatabase.HERO_SKILLS.get(x.getFullName().toString());
        } catch (NoSuchElementException f) {
            throw new Error("could not find base kit for "+x.getFullName().toString());
        }
        x.setBaseKit(baseKit);
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



    public static void main(String[] args) {
        updateCache();
        /*
        ArrayList<Hero> heroes;
        heroes = getList();

        Scanner console = new Scanner(System.in);

        String input;
        while (!(input = console.nextLine()).equals("quit")) {
            for (Hero x:heroes) {
                if (x.getFullName().getName().equalsIgnoreCase(input)) {
                    System.out.println(x.getFullName());
                }
            }
        }
        console.close();
        */
    }
}
