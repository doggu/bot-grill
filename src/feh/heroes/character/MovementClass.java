package feh.heroes.character;

import java.util.Calendar;
import java.util.GregorianCalendar;

public enum MovementClass implements HeroClass {
    INFANTRY(2, false, false, false, "Infantry"),
    ARMORED(1, false, false, false, "Armor"),
    CAVALRY(3, false, true, true, "Cavalry"),
    FLYING(2, true, false, false, "Flier");



    private final int range;
    private final boolean
            ignoreTerrain,
            stoppedByTrees,
            slowedByTrenches;
    private final String name;



    MovementClass(int range, boolean ignoreTerrain, boolean stoppedByTrees, boolean slowedByTrenches,
                  String name) {
        this.range = range;
        this.ignoreTerrain = ignoreTerrain;
        //technically stoppedByTrees and slowedByTrenches can be one boolean
        this.stoppedByTrees = stoppedByTrees;
        this.slowedByTrenches = slowedByTrenches;
        this.name = name;
    }



    public int getRange() { return range; }
    public boolean ignoresTerrain() { return ignoreTerrain; }
    public boolean stoppedByTrees() { return stoppedByTrees; }
    public boolean slowedByTrenches() { return slowedByTrenches; }
    //prolly goin unused since toString is exactly the same thing
    public String getName() { return name; }

    private static final GregorianCalendar UPDATE_3_2
            = new GregorianCalendar(2019, Calendar.FEBRUARY, 7);
    public int getMaxDF(GregorianCalendar releaseDate) {
        if (this==INFANTRY)
            if (UPDATE_3_2.compareTo(releaseDate)>=0)
                return 10;
        return 5;
    }



    public static MovementClass getClass(String input) {
        switch (input) {
            case "Infantry":
                return INFANTRY;
            case "Armored":
                return ARMORED;
            case "Cavalry":
                return CAVALRY;
            case "Flying":
                return FLYING;
            default:
                return null;
        }
    }



    public String toString() { return name; }
}
