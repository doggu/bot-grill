package feh.battle;

public abstract class Action {
    /*
    a single interaction that takes place between any two units during a map.

    these interactions always have an initiator, who invokes the action, and
    a receiver, who receives buffs/healing/attacks.


     */



    protected FieldedUnit initiator, receiver;



    Action(FieldedUnit initiator, FieldedUnit receiver) {
        this.initiator = initiator;
        this.receiver = receiver;
    }



    public void preview() {
        FieldedUnit pAttack = initiator.clone();
        FieldedUnit pDefend = receiver.clone();

        commit();
    }

    public abstract void commit();
}
