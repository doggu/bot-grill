package feh.skills.skillTypes;

public class WeaponRefine {
    private final String name, description, specialEff;
    private final int cost, mt, rng;



    public WeaponRefine(String name, String description, String specialEff, int cost, int mt, int rng) {
        this.name = name;
        this.description = description;
        this.specialEff = specialEff;
        this.cost = cost;
        this.mt = mt;
        this.rng = rng;
    }



    public String getName() { return name; }
    public String getDescription() { return description+'\n'+getSpecialEff(); }
    public String getSpecialEff() { return specialEff; }
    public int getCost() { return cost; }
    public int getMt() { return mt; }
    public int getRng() { return rng; }
}
