package feh.characters.skills;

import feh.characters.skills.skillTypes.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Skills extends ArrayList<Skill> {
    public class ActiveKit {
        private Weapon weapon = null;
        private Assist assist = null;
        private Special special = null;
        private PassiveA passiveA = null;
        private PassiveB passiveB = null;
        private PassiveC passiveC = null;
        private PassiveS passiveS = null;
        ActiveKit() {

        }


        public Weapon getWeapon() { return weapon; };
        public Assist getAssist() { return assist; };
        public Special getSpecial() { return special; };
        public PassiveA getPassiveA() { return passiveA; };
        public PassiveB getPassiveB() { return passiveB; };
        public PassiveC getPassiveC() { return passiveC; };
        public PassiveS getPassiveS() { return passiveS; };

        public int[] getStatModifiers() {
            int[] modifiers = new int[5],
                    weapon = this.weapon.getStatModifiers(),
                    passiveA = this.passiveA.getStatModifiers(),
                    passiveS = this.passiveS.getStatModifiers();

            for (int i=0; i<modifiers.length; i++) {
                modifiers[i] = weapon[i]+passiveA[i]+passiveS[i];
            }

            return modifiers;
        }
    }
    private ActiveKit activeKit;

    public Skills(ArrayList<Skill> skills) {
        super(skills);
    }
}
