package feh.characters.skills.skillTypes;

import java.awt.*;
import java.net.URL;

public class Assist extends ActionSkill {
    public Assist(String name, String description, URL link, int cost, boolean exclusive,
                  int rng) {
        super(name, description, link, new Color(0x00EDB3), 'A', cost, exclusive, rng);
    }
}
