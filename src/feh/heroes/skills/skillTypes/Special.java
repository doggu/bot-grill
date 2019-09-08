package feh.heroes.skills.skillTypes;

import java.awt.*;
import java.net.URL;

public class Special extends Skill {
    private final int cd;



    public Special(String name, String description, URL link, int cost, boolean exclusive,
                   int cd) {
        super(name, description, link, new Color(0xF400E5), 'S', cost, exclusive);

        this.cd = cd;
    }



    public int getCooldown() { return cd; }
}
