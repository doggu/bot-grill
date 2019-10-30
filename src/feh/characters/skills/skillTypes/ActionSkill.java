package feh.characters.skills.skillTypes;

import java.awt.*;
import java.net.URL;

// action skills are only weapons and assists: they allow a unit to perform an action on another unit (enemy or not).
public abstract class ActionSkill extends Skill {
    protected final int rng;

    protected ActionSkill(String name, String description, URL link, Color color,
                          char slot, int cost, boolean exclusive, int rng) {
        super(name, description, link, color, slot, cost, exclusive);
        this.rng = rng;
    }



    public int getRng() { return rng; }
}
