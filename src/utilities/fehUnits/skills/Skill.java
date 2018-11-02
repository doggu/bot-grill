package utilities.fehUnits.skills;

public abstract class Skill {
    //0 = weapon, 1 = assist, 2 = special,
    //3 = a passive, 4 = b passive, 5 = c passive, 6 = seal
    final String name, description;
    final int slot, cost;
    final boolean exclusive;

    public Skill(String name, String description, char slot, int cost, boolean exclusive) {
        this.name = name;
        this.description = description;

        switch(slot) {
            case 'W':
                this.slot = 0;
                break;
            case 'A':
                this.slot = 1;
                break;
            case 'S':
                this.slot = 2;
                break;
            case 'a':
                this.slot = 3;
                break;
            case 'b':
                this.slot = 4;
                break;
            case 'c':
                this.slot = 5;
                break;
            case 's':
                this.slot = 6;
                break;
            default:
                System.out.println("this skill has an undefined slot");
                throw new Error();
        }
        this.cost = cost;
        this.exclusive = exclusive;
    }

    public Skill() {
        this.name = "";
        this.description = "";
        this.slot = 0;
        this.cost = 0;
        this.exclusive = false;
    }



    public String getName() { return name; }
    public String getDescription() { return description; }
    //TODO: literally type the rest later it's fuckin late



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
                + "Cost: "+cost+"\n"
                + "Exclusive: "+(exclusive?"Yes":"No")+"\n\n"
                + description+"\n"
                + "```";

        return info;
    }




    public static void main(String[] args) {

    }
}
