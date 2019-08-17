package feh.skills.skillTypes;

import feh.skills.analysis.StatModifier;

public class WeaponRefine extends Weapon {
    private final String specialEff, iconURL;
    private final int[] statModifiers;
    private final int cost, mt, rng;



    public WeaponRefine(String name, String description, String specialEff, String iconURL,
                        int[] statModifiers, int cost, int mt, int rng) {
        super(name, description, cost, true, mt, rng, null, null);
        this.specialEff = specialEff;
        this.iconURL = iconURL;
        this.statModifiers = statModifiers;
        this.cost = cost;
        this.mt = mt;
        this.rng = rng;
    }



    public String getName() { return name; }
    public String getDescription() { return description+'\n'+getSpecialEff(); }
    public String getSpecialEff() { return specialEff; }
    public String getIconURL() { return iconURL; }
    public int getCost() { return cost; }
    public int getMt() { return mt; }
    public int getRng() { return rng; }
    public int[] getStatModifiers() {
        return super.getStatModifiers();
    }
}
