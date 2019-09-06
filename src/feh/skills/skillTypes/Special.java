package feh.skills.skillTypes;

import java.awt.*;

public class Special extends Skill {
    private final int cd;



    public Special(String name, String description, int cost, boolean exclusive,
                  int cd) {
        super(name, description, new Color(0xF400E5), 'S', cost, exclusive);

        this.cd = cd;
    }



    public int getCooldown() { return cd; }
}
