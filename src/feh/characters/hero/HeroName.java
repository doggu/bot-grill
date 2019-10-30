package feh.characters.hero;

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

    public HeroName(String fullName) {
        if (!fullName.contains(": "))
            throw new Error("improper full name received: "+fullName);

        name = fullName.substring(0, fullName.indexOf(": "));
        epithet = fullName.substring(fullName.indexOf(": ")+2);

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

    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof HeroName)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        HeroName c = (HeroName) o;

        // Compare the data members and return accordingly
        return name.equals(c.getName())
                && epithet.equals(c.getEpithet());
    }
}
