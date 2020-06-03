package feh.characters.skills.skillTypes;

import feh.characters.hero.HeroClass;

import java.net.URL;
import java.util.ArrayList;

public class WeaponRefine extends Weapon {
    private final String specialEff;
    private final URL iconURL;
    private final int[] statModifiers;
    private final int cost, mt, rng;



    public WeaponRefine(String name, String description, String specialEff,
                        URL link, URL iconURL, ArrayList<HeroClass> canUse,
                        int[] statModifiers, int cost, int mt, int rng) {
        super(name, description,
                link,
                cost, true, canUse,
                mt, rng, null, null);
        this.specialEff = specialEff;
        this.iconURL = iconURL;
        this.statModifiers = statModifiers;
        this.cost = cost;
        this.mt = mt;
        this.rng = rng;
    }



    //public String getName() { return name; }
    public String getDescription() { return super.getDescription()+'\n'+getSpecialEff(); }
    /*public*/ private String getSpecialEff() { return specialEff; }
    public URL getIconURL() { return iconURL; }
    public int getCost() { return cost; }
    public int getMt() { return mt; }
    public int getRng() { return rng; }
    public int[] getStatModifiers() {
        return super.getStatModifiers();
    }
}
