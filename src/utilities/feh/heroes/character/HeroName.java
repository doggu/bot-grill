package utilities.feh.heroes.character;

public class HeroName {
    private final String name, epithet;



    public HeroName(String name, String epithet) {
        this.name = name;
        this.epithet = epithet;
    }



    public String getName() { return name; }
    public String getEpithet() { return epithet; }



    public String toString() {
        return name+": "+epithet;
    }
}
