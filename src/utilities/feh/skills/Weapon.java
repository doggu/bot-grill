package utilities.feh.skills;

public class Weapon extends Skill implements ActionSkill {
    private final int mt, rng;
    private final String type;



    public Weapon(String name, String description, int cost, boolean exclusive,
                  int mt, int rng, String type) {
        super(name, description, 'W', cost, exclusive);
        this.mt = mt;
        this.rng = rng;
        this.type = type;
    }



    public int getMt() { return mt; }
    public int getRng() { return rng; }
    public String getType() { return type; }



    public String toString() {
        String info = name+"\n"
                + "```\n"
                + "Type: "+type+"\n"
                + "Range: "+rng+"\n"
                + "Cost: "+cost+"\n"
                + "Exclusive: "+(exclusive?"Yes":"No")+"\n\n"
                + description+"\n"
                + "```";

        return info;
    }
}
