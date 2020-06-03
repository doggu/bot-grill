package feh.characters.skills.skillTypes;

import feh.characters.hero.HeroClass;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public abstract class Passive extends Skill {
    private final URL icon;



    public Passive(String name, String description, URL icon, URL link,
                   Color color, char slot, int cost, boolean exclusive,
                   ArrayList<HeroClass> canUse) {
        super(name, description, link, color, slot, cost, exclusive, canUse);
        this.icon = icon;
    }



    public URL getIcon() { return icon; }
}
