package utilities.feh.heroes.character;

public enum WeaponClass {
    SWORD       (1, true, "Sword"),
    LANCE       (1, true, "Lance"),
    AXE         (1, true, "Axe"),
    RED_TOME    (2, false, "Tome"),
    BLUE_TOME   (2, false, "Tome"),
    GREEN_TOME  (2, false, "Tome"),
    STAFF       (2, false, "Staff"),
    BEAST       (1, true, "Beast"),
    BREATH      (1, false, "Breath"),
    DAGGER      (2, true, "Dagger"),
    BOW         (2, true, "Bow");



    private int range;
    private boolean physical;
    private String name;



    WeaponClass(int range, boolean physical, String name) {
        this.range = range;
        this.physical = physical;
        this.name = name;
    }



    public int getRange() { return range; }
    public boolean isPhysical() { return physical; }
    //somewhat useless
    public String getName() { return name; }



    public String toString() { return name; }
}