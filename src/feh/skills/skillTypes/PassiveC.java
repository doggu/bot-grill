package feh.skills.skillTypes;

import java.awt.*;

public class PassiveC extends Passive {
    public PassiveC(String name, String description, int cost, boolean exclusive, String icon) {
        super (name, description, new Color(0x09C639), 'c', cost, exclusive, icon);
    }
}
