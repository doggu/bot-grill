package feh.characters;

public enum Stats {
    HP(0),
    ATK(1),
    SPD(2),
    DEF(3),
    RES(4);

    private final int val;

    Stats(int val) {
        this.val = val;
    }

    public int valueOf() { return val; }
}
