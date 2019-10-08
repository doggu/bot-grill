package feh.heroes.unit;

import feh.heroes.skills.skillTypes.*;

import java.util.ArrayList;

public class SkillSet {
    private Weapon weapon;
    private Assist assist;
    private Special special;
    private PassiveA passiveA;
    private PassiveB passiveB;
    private PassiveC passiveC;
    private PassiveS passiveS; //not sure if this should be here but whatever



    public SkillSet(Weapon weapon,
                    Assist assist,
                    Special special,
                    PassiveA passiveA,
                    PassiveB passiveB,
                    PassiveC passiveC,
                    PassiveS passiveS) {
        this.weapon = weapon;
        this.assist = assist;
        this.special = special;
        this.passiveA = passiveA;
        this.passiveB = passiveB;
        this.passiveC = passiveC;
        this.passiveS = passiveS;
    }
    public SkillSet(ArrayList<Skill> skills) {
        this.weapon = null;
        this.assist = null;
        this.special = null;
        this.passiveA = null;
        this.passiveB = null;
        this.passiveC = null;
        this.passiveS = null;

        for (int i=skills.size()-1; i>=0; i--) { //loop through backwards to only use the first item of each type
            Skill s = skills.get(i);
            if (s instanceof Weapon) {
                weapon = (Weapon) s;
            }
            if (s instanceof Assist) {
                assist = (Assist) s;
            }
            if (s instanceof Special) {
                special = (Special) s;
            }
            if (s instanceof PassiveA) {
                passiveA = (PassiveA) s;
            }
            if (s instanceof PassiveB) {
                passiveB = (PassiveB) s;
            }
            if (s instanceof PassiveC) {
                passiveC = (PassiveC) s;
            }
            if (s instanceof PassiveS) {
                passiveS = (PassiveS) s;
            }
        }
    }



    public boolean hasWeapon() {
        return weapon!=null;
    }

    public boolean hasAssist() {
        return assist!=null;
    }
}
