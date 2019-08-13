package feh.skills.skillTypes;

import feh.skills.analysis.StatModifier;

public class PassiveA extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveA(String name, String description, int cost, boolean exclusive, String icon) {
        super (name, description, 'a', cost, exclusive, icon);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() {
        return statModifiers;
    }
}
