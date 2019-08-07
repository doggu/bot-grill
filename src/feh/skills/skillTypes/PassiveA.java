package feh.skills.skillTypes;

import feh.skills.analysis.StatModifier;

public class PassiveA extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveA(String name, String description, int cost, boolean exclusive) {
        super (name, description, 'a', cost, exclusive);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() {
        return statModifiers;
    }
}
