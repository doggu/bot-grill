package utilities.fehUnits.skills;

public class Weapon extends Skill {
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
                + "Type: ";
        String skillType;
        switch(slot) {
            //TODO: should differentiate between weapon types later
            case 0:
                skillType = "Weapon";
                break;
            case 1:
                skillType = "Assist";
                break;
            case 2:
                skillType = "Special";
                break;
            case 3:
                skillType = "A Passive";
                break;
            case 4:
                skillType = "B Passive";
                break;
            case 5:
                skillType = "C Passive";
                break;
            case 6:
                skillType = "Sacred Seal";
                break;
            default:
                System.out.println("this literally shouldn't happen");
                throw new Error();
        }
        info+= skillType+"\n"
                + "Range: "+rng+"\n"
                + "Cost: "+cost+"\n"
                + "Exclusive: "+(exclusive?"Yes":"No")+"\n\n"
                + description+"\n"
                + "```";

        return info;
    }
}
