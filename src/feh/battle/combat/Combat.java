// https://github.com/Andu2/FEH-Mass-Simulator
// structure for this class was almost entirely stolen from Andu2's Mass Duel Simulator.
// i'm not sure what the rules are for stealing the structure of someone else's work,
// but i'm going to just put this here and hope it's good enough.
// """"inspired""""

package feh.battle.combat;

import feh.battle.Action;
import feh.battle.FieldedUnit;

public class Combat extends Action {
    private FieldedUnit lastAttacker = null; //declared after first doDamage is called

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
     * @param initiatorAttack whether the initiator or the defender
     *                  of the combat sequence was the cause of this specific attack.
     */
                                //initiator's attack or not
    private void doDamage(boolean initiatorAttack) {
        FieldedUnit
                attacker = (initiatorAttack?initiator:receiver),
                defender = (initiatorAttack?receiver:initiator);

        //for deflects and shit
        lastAttacker = attacker;

        //declare variables which would affect damage calculation
        double  enemyDefModifier = 0,
                effectiveBonus = 1.0,
                dmgMultiplier = 1.0,
                dmgBoost = 0,
                dmgBoostFlat = 0,
                absorbPct = 0;

        //declare output of battle sequence (not implemented here, but void can be String maybe)

        //get defensive stat based on attacker's weapon
        int relevantDefensiveStat = (attacker.getWeaponType().isPhysical()?defender.getDef():defender.getRes());
        //todo: implement adaptive damage



    }
    /*
     * this method is originally contained within the "activeHero" function (analogous to a FieldedUnit constructor).
     * this shouldn't affect functionality (as i'm rewriting the whole thing anyway) but may be important to remember.
     */
}
