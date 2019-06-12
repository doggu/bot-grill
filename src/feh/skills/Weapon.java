package feh.skills;

import feh.heroes.character.WeaponClass;
import feh.skills.analysis.ActionSkill;
import feh.skills.analysis.StatModifier;

public class Weapon extends Skill implements ActionSkill, StatModifier {
    private final int mt, rng;
    private final WeaponClass type;
    private final int[] statModifiers;

    private final WeaponRefine refine;



    public Weapon(String name, String description, int cost, boolean exclusive,
                  int mt, int rng, WeaponClass type, WeaponRefine refine) {
        super(name, description, 'W', cost, exclusive);
        this.mt = mt;
        this.rng = rng;
        this.type = type;
        int[] statModifiers = StatModifier.parseStatModifiers(description);
        statModifiers[1]+= mt;
        this.statModifiers = statModifiers;
        this.refine = refine;
    }



    public int getMt() { return mt; }
    public int getRng() { return rng; }
    public WeaponClass getType() { return type; }
    public int[] getStatModifiers() { return statModifiers; }
    public boolean hasRefine() { return refine!=null; }
    public WeaponRefine getRefine() { return refine; }
}
