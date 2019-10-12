package feh.heroes.skills.skillTypes;

import feh.heroes.skills.analysis.SkillAnalysis;

import java.awt.*;
import java.net.URL;

public abstract class Skill {
    private final String name, description;
    private final URL link;
    private final Color color;
    //0 = weapon, 1 = assist, 2 = special,
    //3 = a passive, 4 = b passive, 5 = c passive, 6 = seal
    protected final int slot;
    protected final int cost;
    protected final boolean exclusive;



    public Skill(String name, String description, URL link, Color color, char slot, int cost, boolean exclusive) {
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



    public String toString() {
        return name;
    }



    private final SkillAnalysis analysis;
}
