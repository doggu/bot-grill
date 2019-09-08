package feh.heroes.skills.skillTypes;

import feh.heroes.skills.analysis.StatModifier;

import java.awt.*;
import java.net.URL;

public class PassiveA extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveA(String name, String description, URL icon, URL link, int cost, boolean exclusive) {
        super (name, description, icon, link, new Color(0xFF2A2A), 'a', cost, exclusive);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() {
        return statModifiers;
    }
}
