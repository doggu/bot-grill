package feh.characters.hero;

//is this worth?
public enum Origin {
    FE1 ("Fire Emblem: Shadow Dragon and the Blade of Light"),
    FE2 ("Fire Emblem: Gaiden"),
    FE3 ("Fire Emblem: Mystery of the Emblem"),
    FE4 ("Fire Emblem: Genealogy of the Holy War"),
    FE5 ("Fire Emblem: Thracia 776"),
    FE6 ("Fire Emblem: The Binding Blade"),
    FE7 ("Fire Emblem: The Blazing Blade"),
    FE8 ("Fire Emblem: The Sacred Stones"),
    FE9 ("Fire Emblem: Path of Radiance"),
    FE10("Fire Emblem: Radiant Dawn"),
    FE11("Fire Emblem: Shadow Dragon"),
    FE12("Fire Emblem: New Mystery of the Emblem"),
    FE13("Fire Emblem Awakening"),
    FE14("Fire Emblem Fates"),
    FE15("Fire Emblem Echoes: Shadows of Valentia"),
    FE16("Fire Emblem: Three Houses"),
    HEROES("Fire Emblem Heroes"),
    ENCORE("Tokyo Mirage Sessions ♯FE Encore");



    private final String value;



    Origin(String value) {
        this.value = value;
    }



    public static Origin getOrigin(String origin) {
        for (Origin o:Origin.values())
            if (origin.equals(o.value))
                return o;

        new Error("an unknown origin was discovered! \""+origin+"\"")
                .printStackTrace();
        return null;

        /*
        switch (origin) {
            case "Fire Emblem: Shadow Dragon and the Blade of Light":
                return FE1;
            case "Fire Emblem: Gaiden":
                return FE2;
            case "Fire Emblem: Mystery of the Emblem":
                return FE3;
            case "Fire Emblem: Genealogy of the Holy War":
                return FE4;
            case "Fire Emblem: Thracia 776":
                return FE5;
            case "Fire Emblem: The Binding Blade":
                return FE6;
            case "Fire Emblem: The Blazing Blade":
                return FE7;
            case "Fire Emblem: The Sacred Stones":
                return FE8;
            case "Fire Emblem: Path of Radiance":
                return FE9;
            case "Fire Emblem: Radiant Dawn":
                return FE10;
            case "Fire Emblem: Shadow Dragon":
                return FE11;
            case "Fire Emblem: New Mystery of the Emblem":
                return FE12;
            case "Fire Emblem Awakening":
                return FE13;
            case "Fire Emblem Fates":
                return FE14;
            case "Fire Emblem Echoes: Shadows of Valentia":
                return FE15;
            case "Fire Emblem: Three Houses":
                return FE16;
            case "Fire Emblem Heroes":
                return HEROES;
            case "Tokyo Mirage Sessions ♯FE Encore":
                return ENCORE;
        }
         */
    }

    public String toString() { return value; }

}
