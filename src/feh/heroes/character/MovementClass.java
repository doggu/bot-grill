package feh.heroes.character;

import events.gameroom.mapTest.Tile;

import java.util.Calendar;
import java.util.GregorianCalendar;

public enum MovementClass implements HeroClass {
    INFANTRY(2, false, true, false, false, "Infantry"),
    ARMORED(1, false, false, false, false, "Armor"),
    CAVALRY(3, false, true, true, true, "Cavalry"),
    FLYING(2, true, false, false, false, "Flier");



    private final int range;
    private final boolean
            ignoreTerrain,
            slowedByTrees,
            stoppedByTrees,
            slowedByTrenches;
    private final String name;



    MovementClass(int range, boolean ignoreTerrain, boolean slowedByTrees, boolean stoppedByTrees, boolean slowedByTrenches,
                  String name) {
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
        //if (t.isSlowed(this)) return 2;

        System.out.println(this);


        switch (t) {
            case PLAINS:
            case PLAINS_DEFENSIVE:
                return 1;
            case FOREST:
            case FOREST_DEFENSIVE:
                if (this.stoppedByTrees) return -1;
                if (this.slowedByTrees)
                    return 2;
                return 1;
            case POND:
            case LAVA:
            case RIVER:
            case WATER:
            case MOUNTAIN:
            case CLIFF:
                return (this.ignoreTerrain?1:-1);
            case TRENCHES:
            case TRENCHES_DEFENSIVE:
                return (this.slowedByTrenches?3:1); //horses must be adjacent to trenches to even move on top of them
            case WALL:
                return -1;
            default:
                return Integer.MAX_VALUE;
        }
    }



    public static void main(String[] args) {
        System.out.println(CAVALRY.getWeight(Tile.FOREST));
    }
}
