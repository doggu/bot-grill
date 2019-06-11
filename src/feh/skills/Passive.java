package feh.skills;

public abstract class Passive extends Skill {
    public Passive(String name, String description, char slot, int cost, boolean exclusive) {
        super(name, description, slot, cost, exclusive);
    }
}
