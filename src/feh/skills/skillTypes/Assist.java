package feh.skills.skillTypes;

import feh.skills.analysis.ActionSkill;

import java.awt.*;

public class Assist extends Skill implements ActionSkill {
    private final int rng;



    public Assist(String name, String description, int cost, boolean exclusive,
                int rng) {
        super(name, description, new Color(0x00EDB3), 'A', cost, exclusive);

        this.rng = rng;
    }



    public int getRng() { return rng; }
}
