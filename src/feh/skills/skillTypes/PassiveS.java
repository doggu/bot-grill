package feh.skills.skillTypes;

import feh.skills.analysis.StatModifier;

import java.awt.*;

public class PassiveS extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveS(String name, String description, int cost, boolean exclusive, String icon) {
        super (name, description, new Color(0xEDE500), 's', cost, exclusive, icon);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() { return statModifiers; }
}
