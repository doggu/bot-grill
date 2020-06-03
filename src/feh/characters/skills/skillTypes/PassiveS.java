package feh.characters.skills.skillTypes;

import feh.characters.hero.HeroClass;
import feh.characters.skills.analysis.StatModifier;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class PassiveS extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveS(String name, String description,
                    URL icon, URL link,
                    int cost, boolean exclusive,
                    ArrayList<HeroClass> canUse) {
        super (name, description,
                icon, link,
                new Color(0xEDE500), 's',
                cost, exclusive, canUse);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() { return statModifiers; }
}
