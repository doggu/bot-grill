package feh.characters.skills.skillTypes;

import feh.characters.hero.HeroClass;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class Assist extends ActionSkill {
    public Assist(String name, String description, URL link,
                  int cost, boolean exclusive,
                  ArrayList<HeroClass> canUse,
                  int rng) {
        super(name, description, link,
                new Color(0x00EDB3), 'A',
                cost, exclusive, canUse, rng);
    }
}
