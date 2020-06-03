// https://github.com/Andu2/FEH-Mass-Simulator
// structure for this class was almost entirely stolen from Andu2's Mass Duel Simulator.
// i'm not sure what the rules are for stealing the structure of someone else's work,
// but i'm going to just put this here and hope it's good enough.
// """"inspired""""

package feh.battle.combat;

import feh.battle.Action;
import feh.battle.FieldedUnit;
import feh.characters.hero.WeaponClass;

public class Combat extends Action {
    //declared after first doDamage is called
    private FieldedUnit lastAttacker = null;

    private CombatPerformance
            initiatorStats = new CombatPerformance(super.initiator),
            defenderStats = new CombatPerformance(super.receiver);

    /*
    a single interaction between two foes.
     */



    public Combat(FieldedUnit initiator, FieldedUnit defender) {
        super(initiator, defender);
    }


    //when should AoEs be handled?
    public void commit() {
        //initiator
        //receiver
    }

    private void attack() {

    }

    /**
     * calculates the damage inflicted by a unit's attack.
     *
     * @param initiatorAttack whether the initiator or the defender of the
     *                        combat sequence was the cause of this specific
     *                        attack.
     */
                                //initiator's attack or not
    private void doDamage(boolean initiatorAttack) {
        FieldedUnit
                attacker = (initiatorAttack?initiator:receiver),
                defender = (initiatorAttack?receiver:initiator);

        //for deflects and shit
        lastAttacker = attacker;


        //declare variables which would affect damage calculation
        //integer indicating weight of advantage
        //1 == normal advantage (20%)
        //2 == TA advantage (40%)
        //0 == no advantage
        //negatives as well
        int weaponTriangleAdvantage = 0;
        //from code.js
                //changes defense by a multiplier (e.g. Luna is 0.5)
        double  enemyDefMultiplier = 1.0,
                //effectiveBonus = 1.0, //goes unused in original code
                //multiplies calculated damage
                // (not from special damage bonuses such as wo dao)
                // (for astra and stuff)
                dmgMultiplier = 1.0,
                //provides bonuses for damage
                // (e.g. fire emblem = attacker.combatRes()*0.3)
                dmgBoost = 0,
                //adds arbitrary bonuses together (e.g. wo dao, wrath)
                dmgBoostFlat = 0,
                absorbPct = 0;


        //declare output of battle sequence
        // (not implemented here, but void can be String maybe)

        //get defensive stat based on attacker's weapon
        int relevantDefensiveStat =
                attacker.getWeaponType().isPhysical()
                        ? defender.getDef() : defender.getRes();
        //todo: implement adaptive damage


////////OFFENSIVE SPECIAL MODIFIERS APPLICATION
        boolean offensiveSpecialActivated = false;

        if (attacker.specialReady()) {
            //check name to manually calculate extra damage
            //todo: ask SkillSet for a Function object
            // that is applied in damage calculation?
            // i'm saying do this later but adding an
            // idea to not feel completely useless
            enemyDefMultiplier = 2;
            dmgMultiplier = 2;
            dmgBoost = 10;
            dmgBoostFlat = 10;
            absorbPct = 3;

            offensiveSpecialActivated = true;
        }


////////WEAPON TRIANGLE ADVANTAGE CALCULATION
        int attackerColor = 0;
        switch (attacker.getColor()) {
            case 'g':
                attackerColor++;
            case 'b':
                attackerColor++;
            //case 'r': 0
                break;
            case 'c':
                attackerColor--;
        }
        int defenderColor = 0;
        switch (defender.getColor()) {
            case 'g':
                defenderColor++;
            case 'b':
                defenderColor++;
            //case 'r': 0
                break;
            case 'c':
                defenderColor--;
        }

        //red        0
        //blue       1
        //green      2
        //colorless -1
        if (defenderColor<0||attackerColor<0) {
            //weaponTriangleAdvantage = 0; //already 0
            //todo: check for raventomes
        } else {
            switch (defenderColor - attackerColor) {
                case 1:
                case -2:
                    weaponTriangleAdvantage = -1;
                    break;
                case -1:
                case 2:
                    weaponTriangleAdvantage = 1;
                    break;
                case 0:
                    //weaponTriangleAdvantage = 0; //already 0
            }
        }

        /* this is all definitely necessary, but i won't say why
        if (attacker.getActiveKit().hasTA()) {
            if (attacker.getActiveKit().hasCancelAffinity()) {

            } else {

            }
        }

        if (defender.getActiveKit().hasTA()) {
            if (defender.getActiveKit().hasCancelAffinity()) {

            } else {

            }
        }
         */



////////EFFECTIVE DAMAGE MODIFIER APPLICATION

        /*
        if (attacker.getActiveKit().effectiveAgainst(defender.getMoveType())) {
            if (neutralizing shield or something) {
                don't multiply
            } else {
                begone thot
            }

          //currently impossible to be effective against multiple characteristics of a defender
        } else if (attacker.getActiveKit().effectiveAgainst(defender.getWeaponType())) {
            //no neutralizers for weapon type ('magic foes' effectivity)
            begone thot
        }

         */



////////COLORLESS_STAFF DAMAGE SHIT

        float weaponModifier = 1;
        if (attacker.getWeaponType()== WeaponClass.COLORLESS_STAFF) {
            weaponModifier = 0.5f;

            //todo: add wrathful staff in skill analysis
        }



////////OFFENSIVE SPECIAL MODIFIERS APPLICATION

        boolean defensiveSpecialActivated = false;
        float damageReduction = 1.0f;
        int damageReductionFlat = 0;
        boolean miracle = false; //i guess



////////THANI AND SHIT

        /*
        if (initiatorStats.didAttack) {
            if (tyrfing&&(defender.getWeaponType().matches(WeaponClass.TOME))) {
                damageReduction/= 2;
            }
            if (thani&&(defender.getMoveType()==MovementClass.CAVALRY||defender.getMoveType()==MovementClass.ARMORED)) {
                damageReduction*= 0.7;
            }

            //todo: refines
            //todo: offload all this to the skills themselves somehow
        }
         */



        if (/*has brave weapon||*/defender.getCurrentHP()>0) {
            //doDamage(initiatorAttack); need to figure out brave weapon attacks
        }
    }
    /*
     * this method is originally contained within the "activeHero" function (analogous to a FieldedUnit constructor).
     * this shouldn't affect functionality (as i'm rewriting the whole thing anyway) but may be important to remember.
     */
}
