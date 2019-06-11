package feh.heroes;

import feh.FEHeroesCache;
import utilities.WebScalper;
import feh.heroes.character.Availability;
import feh.heroes.character.Hero;
import feh.heroes.character.HeroConstructor;
import feh.heroes.character.HeroName;
import feh.skills.Skill;
import feh.skills.SkillDatabase;

import java.util.*;

public class UnitDatabase extends WebScalper {
    public static ArrayList<Hero> HEROES = getList();

    private static final String HERO_SUBDIR = "/herodata/";

    private static final String
            LV1_STATS = "Level_1_stats_table",
            GROWTH_RATES = "Growth_rate_table",
            HERO_LIST = "List_of_Heroes";

    private static FEHeroesCache
            LV1_STATS_FILE,
            GROWTH_RATES_FILE,
            HERO_LIST_FILE;

    private static FEHeroesCache[] HERO_FILES = {
            LV1_STATS_FILE,
            GROWTH_RATES_FILE,
            HERO_LIST_FILE,
    };



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
        for (FEHeroesCache x:HERO_FILES) {
            if (!x.update()) throw new Error("unable to update "+x.getName());
        }

        HEROES = getList();
    }

    private static ArrayList<Hero> getList() {
        LV1_STATS_FILE = new FEHeroesCache(LV1_STATS, HERO_SUBDIR);
        GROWTH_RATES_FILE = new FEHeroesCache(GROWTH_RATES, HERO_SUBDIR);
        HERO_LIST_FILE = new FEHeroesCache(HERO_LIST, HERO_SUBDIR);



        ArrayList<HeroConstructor> heroConstructors = new ArrayList<>();



        ArrayList<String>
                lv1StatsData = LV1_STATS_FILE.getTable("<table class=\"wikitable sortable\""),
                growthRatesData = GROWTH_RATES_FILE.getTable("<table class=\"wikitable default sortable\""),
                heroListData = HERO_LIST_FILE.getTable("<table class=\"wikitable default sortable\"");

        lv1StatsData.subList(0,7).clear();
        growthRatesData.subList(0,12).clear();
        heroListData.subList(0,7).clear();

        Iterator<String>
                lv1StatsIterator = lv1StatsData.iterator(),
                growthRatesIterator = growthRatesData.iterator(),
                heroListIterator = heroListData.iterator();




        while (lv1StatsIterator.hasNext()&&growthRatesIterator.hasNext()&&heroListIterator.hasNext()) {
            HeroConstructor x = new HeroConstructor();

            processLv1Stats(x, lv1StatsIterator);
            processGrowthRates(x, growthRatesIterator);
            processListOfHeroes(x, heroListIterator);
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

    private static void processLv1Stats(HeroConstructor x, Iterator<String> input) {
        String identification = input.next();
        String[] id = identification.split(": ");
        x.setFullName(new HeroName(id[0], id[1]));



        int[] stats = new int[5];

        for (int i=0; i<stats.length; i++)
            stats[i] = Integer.parseInt(input.next());

        x.setStats(stats);



        input.next(); //total lv1 stats
    }
    private static void processGrowthRates(HeroConstructor x, Iterator<String> input) {
        String[] id = input.next().split(": ");

        if (!id[0].equals(x.getName()))
            System.out.println("GrR: misalignment detected for unit "+HERO_INDEX+" ("+id[0]+")");



        String typing = input.next();
        x.setWeaponType(typing);



        String moveType = input.next();
        x.setMoveType(moveType);



        input.next(); //total lv1 stats
        input.next(); //total stat growths
        input.next(); //total lv1 stats and stat growths



        int[] statGrowths = new int[5];

        for (int i=0; i<statGrowths.length; i++) {
            String growth = input.next().replace("%", "");
            statGrowths[i] = Integer.parseInt(growth);
        }

        x.setGrowths(statGrowths);



        GregorianCalendar releaseDate = parseDate(input.next());
        x.setDateReleased(releaseDate);
    }
    private static void processListOfHeroes(HeroConstructor x, Iterator<String> input) {
        String[] id = input.next().split(": ");
        String name = id[0];
        if (id.length<2) throw new Error("improper name detected for unit "+HERO_INDEX);
        if (!id[0].equals(x.getName()))
            System.out.println("LoH: misalignment detected for unit "+HERO_INDEX+" ("+id[0]+")");



        String origin = input.next();
        x.setOrigin(origin);



        String rarity = input.next();
        int lowerRarityBound;
        try {
            lowerRarityBound = Integer.parseInt(rarity);
        } catch (NumberFormatException g) {
            throw new Error("error for character #"+HERO_INDEX+" ("+name+")\n" +
                    "attempted rarity: \""+rarity+"\"");
        }
        x.setRarity(lowerRarityBound);



        String indeterminate = input.next();

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
            indeterminate = input.next();
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

            input.next(); //release date
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
