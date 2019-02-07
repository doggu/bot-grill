package utilities.feh.heroes.character;

import java.util.ArrayList;

public class HeroName {
    private final String name, epithet;
    private boolean ambiguousName;
    private static ArrayList<HeroName> fullNames = new ArrayList<>();
    private static ArrayList<String> names = new ArrayList<>();



    public HeroName(String name, String epithet) {
        this.name = name;
        this.epithet = epithet;

        if (names.contains(name)) {
            HeroName original = fullNames.get(names.indexOf(name));
            original.ambiguousName = true;
            ambiguousName = true;
        } else {
            ambiguousName = false;
        }

        fullNames.add(this);
        names.add(name);
    }



    public String getName() { return name; }
    public String getEpithet() { return epithet; }
    public boolean isAmbiguousName() { return ambiguousName; }

    public String toString() {
        return name+": "+epithet;
    }
}
