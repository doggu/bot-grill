package utilities.feh.skills;

public class Assist extends Skill implements ActionSkill {
    private final int rng;



    public Assist(String name, String description, int cost, boolean exclusive,
                int rng) {
        super(name, description, 'A', cost, exclusive);

        this.rng = rng;
    }



    public int getRng() { return rng; }
}
