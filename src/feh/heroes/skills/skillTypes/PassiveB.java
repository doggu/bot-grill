package feh.heroes.skills.skillTypes;



import java.awt.*;

public class PassiveB extends Passive {
    public PassiveB(String name, String description, int cost, boolean exclusive, String icon) {
        super (name, description, new Color(0x003ED3), 'b', cost, exclusive, icon);
    }
}
