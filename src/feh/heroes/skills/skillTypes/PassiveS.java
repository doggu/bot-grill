package feh.heroes.skills.skillTypes;

import feh.heroes.skills.analysis.StatModifier;

import java.awt.*;
import java.net.URL;

public class PassiveS extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveS(String name, String description, URL icon, URL link, int cost, boolean exclusive) {
        super (name, description, icon, link, new Color(0xEDE500), 's', cost, exclusive);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() { return statModifiers; }
}
