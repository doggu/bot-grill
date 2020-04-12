package feh.characters.skills.skillTypes;

import feh.characters.hero.WeaponClass;
import feh.characters.skills.analysis.StatModifier;

import java.awt.*;
import java.net.URL;

public class Weapon extends ActionSkill implements StatModifier {
    private final int mt;
    private final WeaponClass type;
    private final int[] statModifiers;

    private final WeaponRefine refine;
    //private final Evolution evolution;
    //i'd write a todo here but i dont really want to ugh it's already formatting



    public Weapon(String name, String description,
                  URL link,
                  int cost, boolean exclusive,
                  int mt, int rng, WeaponClass type, WeaponRefine refine) {
        super(name, description,
                link, new Color(0xDE1336), 'W',
                cost, exclusive, rng);
        this.mt = mt;
        this.type = type;
        int[] statModifiers = StatModifier.parseStatModifiers(description);
        statModifiers[1]+= mt;
        this.statModifiers = statModifiers;
        this.refine = refine;
    }



    public int getMt() { return mt; }
    public WeaponClass getType() { return type; }
    public int[] getStatModifiers() { return statModifiers; }
    public boolean refineable() { return hasRefine()||hasRefine(); }
    //public boolean canEvolve() { return evolution!=null; }
    public boolean hasRefine() { return refine!=null; }
    public WeaponRefine getRefine() { return refine; }
}
