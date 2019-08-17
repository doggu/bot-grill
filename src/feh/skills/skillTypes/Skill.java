package feh.skills.skillTypes;

import feh.skills.SkillDatabase;
import feh.skills.analysis.CooldownModifier;
import feh.skills.analysis.StatModifier;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Skill {
    final String name, description;
    private final String[] args;

    //0 = weapon, 1 = assist, 2 = special,
    //3 = a passive, 4 = b passive, 5 = c passive, 6 = seal
    final int slot;

    final int cost;
    final boolean exclusive;



    public Skill(String name, String description, char slot, int cost, boolean exclusive) {
        this.name = name;
        this.description = description;

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



        //remove last period and split up by sentences
        try {
            if (description.length()==0) // stoopit silve r swor d
                args = new String[0];
            else
                args = description.substring(0, description.length() - 1).split("\\. ");
        } catch (IndexOutOfBoundsException ioobe) {
            ioobe.printStackTrace();
            throw new Error(description);
        }



        generateProperties();
    }



    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCost() { return cost; }
    public int getSlot() { return slot; }
    public boolean isExclusive() { return exclusive; }

    private void generateProperties() {
        //System.out.println(name);
        //for (String x:args) System.out.println(x);
        //System.out.println("\n\n");

        if (this instanceof StatModifier) {

        }

        if (this instanceof CooldownModifier) {

        }
    }



    public String toString() {
        /*
        String info = name+"\n"
                + "```\n"
                + "Type: ";
        String skillType;
        switch(slot) {
            //TODO: should differentiate between weapon types later
            case 0:
                skillType = "Weapon";
                break;
            case 1:
                skillType = "Assist";
                break;
            case 2:
                skillType = "Special";
                break;
            case 3:
                skillType = "A Passive";
                break;
            case 4:
                skillType = "B Passive";
                break;
            case 5:
                skillType = "C Passive";
                break;
            case 6:
                skillType = "Sacred Seal";
                break;
            default:
                throw new Error("this literally shouldn't happen");
        }
        info+= skillType+"\n"
                + "Cost: "+cost+"\n"
                + "Exclusive: "+(exclusive?"Yes":"No")+"\n\n"
                + description+"\n"
                + "```";
         */
        return name;
    }



    public static void main(String[] args) {
        ArrayList<Skill> skills = SkillDatabase.SKILLS;
    }
}
