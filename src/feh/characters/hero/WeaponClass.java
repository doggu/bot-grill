package feh.characters.hero;

public enum WeaponClass implements HeroClass {
    SWORD           (1, true, "Sword"),
    LANCE           (1, true, "Lance"),
    AXE             (1, true, "Axe"),
    TOME            (2, false, "Tome"),
    RED_TOME        (2, false, "Tome"),
    BLUE_TOME       (2, false, "Tome"),
    GREEN_TOME      (2, false, "Tome"),
    COLORLESS_TOME  (2, false, "Tome"),
    COLORLESS_STAFF (2, false, "Staff"),
    BEAST           (1, true, "Beast"),
    BREATH          (1, false, "Breath"),
    DAGGER          (2, true, "Dagger"),
    BOW             (2, true, "Bow");



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
            case "Red Sword":
                return SWORD;
            case "Blue Lance":
                return LANCE;
            case "Green Axe":
                return AXE;
            case "Red Tome":
                return RED_TOME;
            case "Blue Tome":
                return BLUE_TOME;
            case "Green Tome":
                return GREEN_TOME;
            case "Colorless Tome":
                return COLORLESS_TOME;
            case "Staff":
            case "Colorless Staff":
                return COLORLESS_STAFF;
            case "Red Beast":
            case "Blue Beast":
            case "Green Beast":
            case "Colorless Beast":
            case "Beast":
                return BEAST;
            case "Red Breath":
            case "Blue Breath":
            case "Green Breath":
            case "Colorless Breath":
            case "Breath":
                return BREATH;
            case "Red Dagger":
            case "Blue Dagger":
            case "Green Dagger":
            case "Colorless Dagger":
//            case "Dagger":
                return DAGGER;
            case "Red Bow":
            case "Blue Bow":
            case "Green Bow":
            case "Colorless Bow":
//            case "Bow":
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
        return o==this ||
                (this==TOME&&(o==RED_TOME||o==BLUE_TOME||o==GREEN_TOME)) ||
                (o==TOME&&(this==RED_TOME||this==BLUE_TOME||this==GREEN_TOME));
    }
}