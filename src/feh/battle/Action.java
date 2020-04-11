package feh.battle;

public abstract class Action {
    /*
    a single interaction that takes place between any two units during a map.

    these interactions always have an initiator, who invokes the action, and
    a receiver, who receives buffs/healing/attacks.


     */



    protected final FieldedUnit initiator, receiver;
    protected final FieldedUnit backupInitiator, backupReceiver;



    protected Action(FieldedUnit initiator, FieldedUnit receiver) {
        this.initiator = initiator;
        this.receiver = receiver;
        this.backupInitiator = initiator.duplicate();
        this.backupReceiver = receiver.duplicate();

        preview();
    }



    public void preview() {
        commit();
    }

    public abstract void commit();
}
