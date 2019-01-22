package utilities.fehUnits.heroes;

import utilities.ScannerUtil;
import utilities.WebScalper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class UnitDatabase extends WebScalper {
    public static final ArrayList<Hero> HEROES = new ArrayList<>();

    /*
     * one hero for each document:
     *
     * lv1Stats
     *
     * [7 lines of useless stuff]
     * [name]: [epithet]
     * [hp]
     * [atk]
     * [spd]
     * [def]
     * [res]
     * [total]
     * [name]: [epithet]
     * [hp]
     * [atk]
     * ...
     *
     *
     *
     * growths
     *
     * [12 lines of useless stuff]
     * [name]: [epithet]
     * [color] [weapon]
     * [movement]
     * [lv1 stat total]
     * [growth total]%
     * [lv1 stat total], [growth total]%
     * [hp growth]
     * [atk]
     * [spd]
     * [def]
     * [res]
     * [release date]
     * [name]: [epithet]
     * [color] [weapon]
     * ...
     *
     *
     *
     * heroList
     *
     * [7 lines of useless stuff]
     * [name]: [epithet]
     * [origin]
     * [rarity - lower bound]
     * -[rarity - upper bound]
     * [special indicator (*|Story|Grand Hero Battle|Tempest Trials|Legendary)]
     * [release date]
     * [name]: [epithet]
     * [origin]
     * [rarity - lower bound]
     * -[rarity - upper bound]
     * ...
     *
     */
    private static ArrayList<Hero> getList() throws IOException {
        ArrayList<HeroConstructor> heroConstructors = new ArrayList<>();

        BufferedReader lv1Stats, growthRates, heroList;
        try {
             lv1Stats = readWebsite("https://feheroes.gamepedia.com/Level_1_stats_table");
        } catch (IOException g) { System.out.println("lv1Stats had an issue"); throw new Error(); }
        try {
             growthRates = readWebsite("https://feheroes.gamepedia.com/Growth_rate_table");
        } catch (IOException g) { System.out.println("growthRates had an issue"); throw new Error(); }
        try {
             heroList = readWebsite("https://feheroes.gamepedia.com/Hero_list");
        } catch (IOException g) { System.out.println("heroList had an issue"); throw new Error(); }

        IntStream lv1StatsTable = null, growthRatesTable = null, heroListTable = null;

        String line;
        while ((line = lv1Stats.readLine())!=null) {
            //the entire fucking table is on one line...
            if (line.contains("<table class=\"wikitable sortable\"")) lv1StatsTable = line.chars();
        }
        if (lv1StatsTable==null) {
            System.out.println("lv1StatsTable got some issues");
            throw new Error();
        }

        while ((line = growthRates.readLine())!=null) {
            //same for all of em
            if (line.contains("<table class=\"wikitable default sortable\"")) growthRatesTable = line.chars();
        }
        if (growthRatesTable==null) {
            System.out.println("growthRatesTable got some issues");
            throw new Error();
        }

        while ((line = heroList.readLine())!=null) {
            //ugh
            if (line.contains("<table class=\"wikitable default sortable\"")) heroListTable = line.chars();
        }
        if (heroListTable==null) {
            System.out.println("heroListTable got some issues");
            throw new Error();
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

        while (lv1StatsData.hasNext()&&growthRatesData.hasNext()&&heroListData.hasNext()) {
            HeroConstructor x = new HeroConstructor();

            processLv1Stats(x, lv1StatsData);
            processGrowthRates(x, growthRatesData);
            processHeroList(x, heroListData);

            heroConstructors.add(x);
        }

        ArrayList<Hero> heroes = new ArrayList<>();
        for (HeroConstructor z:heroConstructors)
            heroes.add(z.createHero());
        return heroes;
    }

    private static void processLv1Stats(HeroConstructor x, Iterator<String> input) {
        String identifier = input.next();

        String name = identifier.substring(0, identifier.indexOf(": "));
        String epithet = identifier.substring(identifier.indexOf(": ")+2);

        x.setName(name);
        x.setEpithet(epithet);



        int[] stats = new int[5];

        for (int i=0; i<stats.length; i++)
            stats[i] = Integer.parseInt(input.next());

        x.setStats(stats);



        input.next(); //total lv1 stats
    }

    private static void processGrowthRates(HeroConstructor x, Iterator<String> input) {
        String identifier = input.next();

        String name = identifier.substring(0, identifier.indexOf(": "));
        String epithet = identifier.substring(identifier.indexOf(": ")+2);

        if (!name.equals(x.getName())) System.out.println("misalignment detected for "+name);



        String typing = input.next();

        String color = typing.substring(0, typing.indexOf(" "));
        String weaponType = typing.substring(typing.indexOf(" ")+1);

        x.setColor(color);
        x.setWeaponType(weaponType);



        String moveType = input.next();
        x.setMoveType(moveType);



        input.next(); //total lv1 stats
        input.next(); //total stat growths
        input.next(); //total lv1 stats and stat growths



        int[] statGrowths = new int[5];

        for (int i=0; i<statGrowths.length; i++) {
            String growth = input.next();
            statGrowths[i] = Integer.parseInt(growth.substring(0, growth.length()-1));
        }

        x.setStatGrowths(statGrowths);



        String releaseDateData = input.next();
        String[] values = releaseDateData.split("-");

        int     year = Integer.parseInt(values[0]),
                month = Integer.parseInt(values[1])-1, //month is zero-based
                day = Integer.parseInt(values[2]);

        GregorianCalendar releaseDate = new GregorianCalendar(year, month, day);
        x.setDateReleased(releaseDate);
    }

    private static void processHeroList(HeroConstructor x, Iterator<String> input) {
        String identifier = input.next();
        String name, epithet;
        try {
            name = identifier.substring(0, identifier.indexOf(": "));
            epithet = identifier.substring(identifier.indexOf(": ") + 2);
        } catch (StringIndexOutOfBoundsException g) {
            System.out.println(identifier);
            throw new Error();
        }
        if (!name.equals(x.getName())||!epithet.equals(x.getEpithet()))
            System.out.println("misalignment detected for "+name);



        String origin = input.next();
        x.setOrigin(origin);



        String rarity = input.next();
        int lowerRarityBound = Integer.parseInt(rarity);
        x.setRarity(lowerRarityBound);



        boolean summonable = true, isInNormalPool = true;
        GregorianCalendar releaseDate;
        String indeterminate = input.next();

        /*
         * parsing these next few lines:
         *
         * line 1
         * if "-" index = 0, upper rarity bound
         *      next
         * if "-" is contained, release date
         * if "-" is not contained, availability note
         *      next
         *      parse release date (or not)
         */

        if (indeterminate.indexOf("–")==0) {
            int upperRarityBound = Integer.parseInt(indeterminate.substring(1));
            indeterminate = input.next();
        }                         //this is an em dash btw
        if (indeterminate.contains("-")) {
            //release date
        } else {
            String obtainType = indeterminate;
            switch (obtainType) { //reassigns to the same value a lot but whatever
                case "*":
                    summonable = true;
                    isInNormalPool = true;
                    break;
                case "Story":
                case "Grand Hero Battle":
                case "Tempest Trials":
                    summonable = false;
                    isInNormalPool = false;
                    break;
                    //seasonal/legendary/MyThIC heroes are not part of normal pools
                case "Special":
                case "Legendary":
                    summonable = true;
                    isInNormalPool = false;
                    break;
                default:
                    System.out.println("obtaining method wasn't accounted for: "+obtainType);
                    throw new Error();
            }

            input.next(); //release date
        }

        x.setSummonable(summonable);
        x.setInNormalPool(isInNormalPool);
    }



    public static void main(String[] args) {
        try {
            getList();
        } catch (IOException g) { System.out.println("d"); }
    }
}
