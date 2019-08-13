package feh.skills.skillTypes;

public abstract class Passive extends Skill {
    private final String icon;



    public Passive(String name, String description, char slot, int cost, boolean exclusive, String icon) {
        super(name, description, slot, cost, exclusive);
        this.icon = icon;
    }



    public String getIcon() { return icon; }
}
