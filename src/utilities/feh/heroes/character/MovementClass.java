package utilities.feh.heroes.character;

public enum MovementClass {
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
    public boolean isIgnoreTerrain() { return ignoreTerrain; }
    public boolean isStoppedByTrees() { return stoppedByTrees; }
    public boolean isSlowedByTrenches() { return slowedByTrenches; }
    //prolly goin unused since toString is exactly the same thing
    public String getName() { return name; }



    public String toString() { return name; }
}
