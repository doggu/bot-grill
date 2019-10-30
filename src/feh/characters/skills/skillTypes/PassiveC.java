package feh.characters.skills.skillTypes;

import java.awt.*;
import java.net.URL;

public class PassiveC extends Passive {
    public PassiveC(String name, String description, URL icon, URL link, int cost, boolean exclusive) {
        super (name, description, icon, link, new Color(0x09C639), 'c', cost, exclusive);
    }
}
