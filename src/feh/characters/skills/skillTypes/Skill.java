package feh.characters.skills.skillTypes;

import feh.characters.hero.Hero;
import feh.characters.hero.HeroClass;
import feh.characters.hero.MovementClass;
import feh.characters.hero.WeaponClass;
import feh.characters.skills.analysis.SkillAnalysis;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static feh.characters.hero.MovementClass.*;
import static feh.characters.hero.WeaponClass.*;

public abstract class Skill {
    private final String name, description;
    private final URL link;
    private final Color color;
    //0 = weapon, 1 = assist, 2 = special,
    //3 = a passive, 4 = b passive, 5 = c passive, 6 = seal
    protected final int slot;
    protected final int cost;
    protected final boolean exclusive;

    protected final ArrayList<HeroClass> canUse;



    public Skill(String name, String description,
                 URL link,
                 Color color, char slot,
                 int cost, boolean exclusive,
                 ArrayList<HeroClass> canUse) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.color = color;

        //probably a convoluted system
        switch(slot) {
            case 'W': this.slot = 0; break;
            case 'A': this.slot = 1; break;
            case 'S': this.slot = 2; break;
            case 'a': this.slot = 3; break;
            case 'b': this.slot = 4; break;
            case 'c': this.slot = 5; break;
            case 's': this.slot = 6; break;
            default:
                System.out.println("this skill has an undefined slot");
                throw new Error();
        }
        this.cost = cost;
        this.exclusive = exclusive;
        this.canUse = canUse;

        this.analysis = new SkillAnalysis(this);
    }



    public String getName() { return name; }
    public String getDescription() { return description; }
    public URL getLink() { return link; }
    public Color getColor() { return color; }
    public int getCost() { return cost; }
    public int getSlot() { return slot; }
    public boolean isExclusive() { return exclusive; }
    public SkillAnalysis getAnalysis() { return analysis; }

    public boolean canUse(Hero x) {
        MovementClass heroMove = x.getMoveType();
        WeaponClass heroWeapon = x.getWeaponType();

        return canUse.contains(heroMove) && canUse.contains(heroWeapon);
    }

    public ArrayList<HeroClass> canUse() {
        return canUse;
    }
    private static final HeroClass[] FULL_LIST = {
            INFANTRY,
            ARMORED,
            CAVALRY,
            FLYING,
            RED_SWORD,
            BLUE_LANCE,
            GREEN_AXE,
            RED_TOME,
            BLUE_TOME,
            GREEN_TOME,
            COLORLESS_TOME,
            COLORLESS_STAFF,
            RED_BEAST,
            BLUE_BEAST,
            GREEN_BEAST,
            COLORLESS_BEAST,
            RED_BREATH,
            BLUE_BREATH,
            GREEN_BREATH,
            COLORLESS_BREATH,
            RED_DAGGER,
            BLUE_DAGGER,
            GREEN_DAGGER,
            COLORLESS_DAGGER,
            RED_BOW,
            BLUE_BOW,
            GREEN_BOW,
            COLORLESS_BOW,
    };
    public ArrayList<HeroClass> canNotUse() {
        ArrayList<HeroClass> full = new ArrayList<>(Arrays.asList(FULL_LIST));

        for (HeroClass heroClass:canUse) {
            full.remove(heroClass);
        }

        return full;
    }


    public String toString() {
        return name;
    }


    private final SkillAnalysis analysis;
}
