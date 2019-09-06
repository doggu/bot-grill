package feh.heroes.skills.skillTypes;

import feh.heroes.skills.analysis.StatModifier;

import java.awt.*;

public class PassiveA extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveA(String name, String description, int cost, boolean exclusive, String icon) {
        super (name, description, new Color(0xFF2A2A), 'a', cost, exclusive, icon);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() {
        return statModifiers;
    }
}
