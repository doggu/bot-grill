package feh.heroes.skills.skillTypes;

import java.awt.*;
import java.net.URL;

public abstract class Passive extends Skill {
    private final URL icon;



    public Passive(String name, String description, URL icon, URL link,
                   Color color, char slot, int cost, boolean exclusive) {
        super(name, description, link, color, slot, cost, exclusive);
        this.icon = icon;
    }



    public URL getIcon() { return icon; }
}
