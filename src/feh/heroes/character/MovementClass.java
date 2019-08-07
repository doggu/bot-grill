package feh.heroes.character;

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
