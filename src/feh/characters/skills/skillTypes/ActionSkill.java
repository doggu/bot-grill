package feh.characters.skills.skillTypes;

import feh.characters.hero.HeroClass;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

// action skills are only weapons and assists:
// they allow a unit to perform an action on another unit (enemy or not).
public abstract class ActionSkill extends Skill {
    protected final int rng;

    //todo: i dont like the order of these params
    protected ActionSkill(String name, String description,
                          URL link, Color color,
                          char slot, int cost, boolean exclusive,
                          ArrayList<HeroClass> canUse,
                          int rng) {
        super(name, description, link, color, slot, cost, exclusive, canUse);
        this.rng = rng;
    }



    public int getRng() { return rng; }
}
