package utilities.feh.heroes.character;

public enum WeaponClass {
    SWORD       (1, true, ""),
    LANCE       (1, true, ""),
    AXE         (1, true, ""),
    RED_TOME    (2, false, ""),
    BLUE_TOME   (2, false, ""),
    GREEN_TOME  (2, false, ""),
    STAFF       (2, false, ""),
    BEAST       (1, true, ""),
    BREATH      (1, false, ""),
    DAGGER      (2, true, ""),
    BOW         (2, true, "");



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