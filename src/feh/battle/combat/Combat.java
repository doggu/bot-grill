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
     * @param initiator whether the initiator or the defender
     *                  of the combat sequence was the cause of this specific attack.
     */
                                //initiator's attack or not
    private void doDamage(boolean initiator) {
        lastAttacker = (initiator?this.initiator:receiver);

    }
    /*
     * this method is originally contained within the "activeHero" function (analogous to a FieldedUnit constructor).
     * this shouldn't affect functionality (as i'm rewriting the whole thing anyway) but may be important to remember.
     */
}
