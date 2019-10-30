package feh.characters.unit;

import feh.characters.hero.HeroClass;
import feh.characters.skills.SkillDatabase;
import feh.characters.skills.analysis.StatModifier;
import feh.characters.skills.skillTypes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillSet extends ArrayList<Skill> implements List<Skill> {
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

        if (weapon!=null) {
            this.add(weapon);
        }
        if (assist!=null) {
            this.add(assist);
        }
        if (special!=null) {
            this.add(special);
        }
        if (passiveA!=null) {
            this.add(passiveA);
        }
        if (passiveB!=null) {
            this.add(passiveB);
        }
        if (passiveC!=null) {
            this.add(passiveC);
        }
        if (passiveS!=null) {
            this.add(passiveS);
        }
    }
    public SkillSet(Skill ... skills) {
        super(Arrays.asList(skills));
    }
    public SkillSet(ArrayList<Skill> skills) {
        super(skills);
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
            } else if (s instanceof Assist) {
                assist = (Assist) s;
            } else if (s instanceof Special) {
                special = (Special) s;
            } else if (s instanceof PassiveA) {
                passiveA = (PassiveA) s;
            } else if (s instanceof PassiveB) {
                passiveB = (PassiveB) s;
            } else if (s instanceof PassiveC) {
                passiveC = (PassiveC) s;
            } else if (s instanceof PassiveS) {
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

    public boolean hasSpecial() {
        return special!=null;
    }

    int[] getStatModifiers() {
        int[] modifiers = new int[5];
        for (Skill x:this) {
            if (x instanceof StatModifier) {
                int[] xModifiers = ((StatModifier) x).getStatModifiers();

                for (int i=0; i<modifiers.length; i++) modifiers[i]+= xModifiers[i];
            }
        }

        return modifiers;
    }

    //todo: implement SkillAnalysis features (should probably merge into Skill before this happens)
    public boolean hasTriangleAdept() {
        for (Skill x:this) {
            if (x.getAnalysis().getTriangleAdept()) {
                return true;
            }
        }

        return false;
    }
    public boolean hasCancelAffinity() {
        return false;
    }
    public boolean effectiveAgainst(HeroClass type) {
        for (Skill x:this) {
            for (HeroClass effective:x.getAnalysis().getEffective()) {
                if (type==effective) return true;
            }
        }

        return false;
    }



    public static void main(String[] args) {
        SkillDatabase d = SkillDatabase.DATABASE;
        SkillSet f = new SkillSet(d.find("Falchion"), d.find("Fort. Def/Res 3"));

        System.out.println("hp\tatk\tspd\tdef\tres");
        for (int m:f.getStatModifiers())
            System.out.print(m+"\t");

        System.out.println();
    }
}
