package feh.characters.hero;

public enum WeaponClass implements HeroClass {
    RED_SWORD       (1, true, "Red Sword", "physical"),
    BLUE_LANCE      (1, true, "Blue Lance", "physical"),
    GREEN_AXE       (1, true, "Green Axe", "physical"),
    TOME            (2, false, "Tome", "tome"),
    RED_TOME        (2, false, "Red Tome", "tome"),
    BLUE_TOME       (2, false, "Blue Tome", "tome"),
    GREEN_TOME      (2, false, "Green Tome", "tome"),
    COLORLESS_TOME  (2, false, "Colorless Tome", "tome"),
    COLORLESS_STAFF (2, false, "Colorless Staff", "staff"),
    BEAST           (1, true, "Beast", "beast"),
    RED_BEAST       (1, true, "Red Beast", "beast"),
    BLUE_BEAST      (1, true, "Blue Beast", "beast"),
    GREEN_BEAST     (1, true, "Green Beast", "beast"),
    COLORLESS_BEAST (1, true, "Colorless Beast", "beast"),
    BREATH          (1, false, "Breath", "breath"),
    RED_BREATH      (1, false, "Red Breath", "breath"),
    BLUE_BREATH     (1, false, "Blue Breath", "breath"),
    GREEN_BREATH    (1, false, "Green Breath", "breath"),
    COLORLESS_BREATH(1, false, "Colorless Breath", "breath"),
    DAGGER          (2, true, "Dagger", "dagger"),
    RED_DAGGER      (2, true, "Red Dagger", "dagger"),
    BLUE_DAGGER     (2, true, "Blue Dagger", "dagger"),
    GREEN_DAGGER    (2, true, "Green Dagger", "dagger"),
    COLORLESS_DAGGER(2, true, "Colorless Dagger", "dagger"),
    BOW             (2, true, "Bow", "bow"),
    RED_BOW         (2, true, "Red Bow", "bow"),
    BLUE_BOW        (2, true, "Blue Bow", "bow"),
    GREEN_BOW       (2, true, "Green Bow", "bow"),
    COLORLESS_BOW   (2, true, "Colorless Bow", "bow");



    private int range;
    private boolean physical;
    private String name;
    //physical, tome, beast, breath, dagger, bow
    private String supertype;



    WeaponClass(int range, boolean physical, String name, String supertype) {
        this.range = range;
        this.physical = physical;
        this.name = name;
        this.supertype = supertype;
    }



    public int getRange() { return range; }
    public boolean isPhysical() { return physical; }
    //somewhat useless
    public String getName() { return name; }



    public String toString() { return name; }

    public static WeaponClass getClass(String name) {
        switch(name) {
            case "Red Sword":
                return RED_SWORD;
            case "Blue Lance":
                return BLUE_LANCE;
            case "Green Axe":
                return GREEN_AXE;
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
                return RED_BEAST;
            case "Blue Beast":
                return BLUE_BEAST;
            case "Green Beast":
                return GREEN_BEAST;
            case "Colorless Beast":
                return COLORLESS_BEAST;
            case "Beast":
                return BEAST;
            case "Red Breath":
                return RED_BREATH;
            case "Blue Breath":
                return BLUE_BREATH;
            case "Green Breath":
                return GREEN_BREATH;
            case "Colorless Breath":
                return COLORLESS_BREATH;
            case "Breath":
                return BREATH;
            case "Red Dagger":
                return RED_DAGGER;
            case "Blue Dagger":
                return BLUE_DAGGER;
            case "Green Dagger":
                return GREEN_DAGGER;
            case "Colorless Dagger":
                return COLORLESS_DAGGER;
            case "Dagger":
                return DAGGER;
            case "Red Bow":
                return RED_BOW;
            case "Blue Bow":
                return BLUE_BOW;
            case "Green Bow":
                return GREEN_BOW;
            case "Colorless Bow":
                return COLORLESS_BOW;
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
        return o==this ||
                (this==TOME&&(o==RED_TOME||o==BLUE_TOME||o==GREEN_TOME)) ||
                (o==TOME&&(this==RED_TOME||this==BLUE_TOME||this==GREEN_TOME));
    }
}