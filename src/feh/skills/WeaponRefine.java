package feh.skills;

public class WeaponRefine {
    private final String name, description;
    private final int cost, mt, rng;



    public WeaponRefine(String name, String description, int cost, int mt, int rng) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.mt = mt;
        this.rng = rng;
    }



    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCost() { return cost; }
    public int getMt() { return mt; }
    public int getRng() { return rng; }
}
