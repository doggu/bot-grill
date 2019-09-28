package feh.battle;

public class Combat extends Action {
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
}
