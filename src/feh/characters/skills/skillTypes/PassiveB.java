package feh.characters.skills.skillTypes;



import feh.characters.hero.HeroClass;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class PassiveB extends Passive {
    public PassiveB(String name, String description,
                    URL icon, URL link,
                    int cost, boolean exclusive,
                    ArrayList<HeroClass> canUse) {
        super (name, description,
                icon, link,
                new Color(0x003ED3), 'b',
                cost, exclusive, canUse);
    }
}
