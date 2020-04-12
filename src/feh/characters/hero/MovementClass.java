package feh.characters.hero;

import feh.battle.map.Tile;

import java.util.Calendar;
import java.util.GregorianCalendar;
                                            //TIL enums are naturally comparable
public enum MovementClass implements HeroClass/*, Comparable<MovementClass>*/ {
    INFANTRY("Infantry",
            2,
            false,
            true,
            false,
            false),
    ARMORED("Armor",
            1,
            false,
            false,
            false,
            false),
    CAVALRY("Cavalry",
            3,
            false,
            true,
            true,
            true),
    FLYING("Flier",
            2,
            true,
            false,
            false,
            false);


    private final String name;
    private final int range;
    private final boolean
            ignoreTerrain,
            slowedByTrees,
            stoppedByTrees,
            slowedByTrenches;

    MovementClass(String name, int range,
                  boolean ignoreTerrain, boolean slowedByTrees,
                  boolean stoppedByTrees, boolean slowedByTrenches) {
        this.range = range;
        this.ignoreTerrain = ignoreTerrain;
        //technically stoppedByTrees and slowedByTrenches can be one boolean
        this.slowedByTrees = slowedByTrees;
        this.stoppedByTrees = stoppedByTrees;
        this.slowedByTrenches = slowedByTrenches;
        this.name = name;
    }


    public int getRange() { return range; }
    //public boolean ignoresTerrain() { return ignoreTerrain; }
    //public boolean stoppedByTrees() { return stoppedByTrees; }
    //public boolean slowedByTrenches() { return slowedByTrenches; }
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

    public int getWeight(Tile t) {
        //if (t.isStopped(this)) return -1;
        //if (t.isSlowed(this)) return 2;
        switch (t) {
            case PLAINS:
            case PLAINS_D:
                return 1;
            case FOREST:
            case FOREST_D:
                if (this.stoppedByTrees) return -1;
                return (this.slowedByTrees?2:1);
            case POND:
            case LAVA:
            case RIVER:
            case WATER:
            case MOUNTAIN:
            case CLIFF:
                return (this.ignoreTerrain?1:-1);
            case TRENCHES:
            case TRENCHES_D:
                //horses must be adjacent to trenches to even move on top of em
                return (this.slowedByTrenches?3:1);
            case WALL:
                return -1;
            default:
                return Integer.MAX_VALUE;
        }
    }
}
