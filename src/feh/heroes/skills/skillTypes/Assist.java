package feh.heroes.skills.skillTypes;

import feh.heroes.skills.analysis.ActionSkill;

import java.awt.*;
import java.net.URL;

public class Assist extends Skill implements ActionSkill {
    private final int rng;



    public Assist(String name, String description, URL link, int cost, boolean exclusive,
                  int rng) {
        super(name, description, link, new Color(0x00EDB3), 'A', cost, exclusive);

        this.rng = rng;
    }



    public int getRng() { return rng; }
}
