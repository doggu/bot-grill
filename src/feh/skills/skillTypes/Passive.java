package feh.skills.skillTypes;

import java.awt.*;

public abstract class Passive extends Skill {
    private final String icon;



    public Passive(String name, String description, Color color, char slot, int cost, boolean exclusive, String icon) {
        super(name, description, color, slot, cost, exclusive);
        this.icon = icon;
    }



    public String getIcon() { return icon; }
}
