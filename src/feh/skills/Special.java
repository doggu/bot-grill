package feh.skills;

public class Special extends Skill {
    private final int cd;



    public Special(String name, String description, int cost, boolean exclusive,
                  int cd) {
        super(name, description, 'S', cost, exclusive);

        this.cd = cd;
    }



    public int getCooldown() { return cd; }
}
