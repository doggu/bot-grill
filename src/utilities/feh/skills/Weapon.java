package utilities.feh.skills;

public class Weapon extends Skill implements ActionSkill, StatModifier {
    private final int mt, rng;
    private final String type;
    private final int[] statModifiers;



    public Weapon(String name, String description, int cost, boolean exclusive,
                  int mt, int rng, String type) {
        super(name, description, 'W', cost, exclusive);
        this.mt = mt;
        this.rng = rng;
        this.type = type;
        int[] statModifiers = StatModifier.parseStatModifiers(description);
        statModifiers[1]+= mt;
        this.statModifiers = statModifiers;
    }



    public int getMt() { return mt; }
    public int getRng() { return rng; }
    public String getType() { return type; }
    public int[] getStatModifiers() { return statModifiers; }

    public String toString() {
        return name+"\n"
                + "```\n"
                + "Type: "+type+"\n"
                + "Range: "+rng+"\n"
                + "Cost: "+cost+"\n"
                + "Exclusive: "+(exclusive?"Yes":"No")+"\n\n"
                + description+"\n"
                + "```";
    }
}
