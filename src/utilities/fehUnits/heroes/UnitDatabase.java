package utilities.fehUnits.heroes;

import utilities.ScannerUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class UnitDatabase {
    public static final ArrayList<Hero> HEROES = getList();

    /*
     * one hero for each document:
     *
     *
     */
    private static ArrayList<Hero> getList() {
        ArrayList<HeroConstructor> heroConstructors = new ArrayList<>();


        //code goes here...


        ArrayList<Hero> heroes = new ArrayList<>();
        for (HeroConstructor x:heroConstructors)
            heroes.add(x.createHero());
        return heroes;
    }

    public static void main(String[] args) {
        File lv1Stats = new File(".\\src\\utilities\\fehUnits\\heroes\\sources\\LV1Stats.txt");

        lv1Stats.setLastModified(0);
    }
}
