package feh.characters.hero;

public enum WeaponClass implements HeroClass {
    SWORD       (1, true, "Sword"),
    LANCE       (1, true, "Lance"),
    AXE         (1, true, "Axe"),
    TOME        (2, false, "Tome"),
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

    public static WeaponClass getClass(String name) {
        switch(name) {
            case "Sword":
                return SWORD;
            case "Lance":
                return LANCE;
            case "Axe":
                return AXE;
            case "Red Tome":
                return RED_TOME;
            case "Blue Tome":
                return BLUE_TOME;
            case "Green Tome":
                return GREEN_TOME;
            case "Staff":
                return STAFF;
            case "Beast":
                return BEAST;
            case "Breath":
                return BREATH;
            case "Dagger":
                return DAGGER;
            case "Bow":
                return BOW;

            case "Tome":
                System.out.println("ambiguous weapon name");
            default:
                System.out.println("unknown weapon type: "+name);
                throw new Error();
        }
    }

    public boolean matches(Object o) {
        if (!(o instanceof WeaponClass)) return false;

        //is this a workaround or a solution? find out next time
        return o==this||(this==TOME&&(o==RED_TOME||o==BLUE_TOME||o==GREEN_TOME))||
                (o==TOME&&(this==RED_TOME||this==BLUE_TOME||this==GREEN_TOME));
    }
}