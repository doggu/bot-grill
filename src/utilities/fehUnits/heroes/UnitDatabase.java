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
             lv1Stats = WebScalper.readWebsite("https://feheroes.gamepedia.com/Level_1_stats_table");
        } catch (IOException g) { System.out.println("lv1Stats had an issue"); throw new Error(); }
        try {
             growthRates = WebScalper.readWebsite("https://feheroes.gamepedia.com/Growth_rate_table");
        } catch (IOException g) { System.out.println("growthRates had an issue"); throw new Error(); }
        try {
             heroList = WebScalper.readWebsite("https://feheroes.gamepedia.com/Hero_list");
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

        //remove initial junk data
        for (int i=0; i<7; i++) lv1StatsTable.;
        for (int i=0; i<12; i++) growthRates.readLine();
        for (int i=0; i<7; i++) heroList.readLine();

        ArrayList<Hero> heroes = new ArrayList<>();
        for (HeroConstructor x:heroConstructors)
            heroes.add(x.createHero());
        return heroes;
    }

    public static void main(String[] args) {
        try {
            getList();
        } catch (IOException g) { System.out.println("d"); }
    }
}
