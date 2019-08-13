package feh.skills.skillTypes;

import feh.skills.analysis.StatModifier;

public class PassiveS extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveS(String name, String description, int cost, boolean exclusive, String icon) {
        super (name, description, 's', cost, exclusive, icon);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() { return statModifiers; }
}
