package feh.heroes.skills.skillTypes;

import feh.heroes.character.WeaponClass;
import feh.heroes.skills.analysis.StatModifier;

import java.awt.*;
import java.net.URL;

public class Weapon extends ActionSkill implements StatModifier {
    private final int mt;
    private final WeaponClass type;
    private final int[] statModifiers;

    private final WeaponRefine refine;



    public Weapon(String name, String description, URL link, int cost, boolean exclusive,
                  int mt, int rng, WeaponClass type, WeaponRefine refine) {
        super(name, description, link, new Color(0xDE1336), 'W', cost, exclusive, rng);
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
    public boolean hasRefine() { return refine!=null; }
    public WeaponRefine getRefine() { return refine; }
}
