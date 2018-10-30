package utilities.fehUnits.heroes;

import net.dv8tion.jda.core.entities.Emote;

import java.util.*;

//TODO: create basic layout for a general character
//Include base skills

public class Character {
    private final String name, epithet, origin, color, weaponType, moveType;
    //these stats are 1* lv1 (regardless of obtainable rarities)
    private final int[] stats, statGrowths;
    private final int rarity;
    private final boolean summonable, isInNormalPool;

    private final GregorianCalendar dateReleased;


    public Character(String name, String epithet, String origin,
                     String color, String weaponType, String moveType,
                     int rarity, boolean summonable, boolean isInNormalPool,
                     GregorianCalendar dateReleased,
                     int[] stats, int[] statGrowths) {
        this.name = name;
        this.epithet = epithet;
        this.origin = origin;
        this.color = color;
        this.weaponType = weaponType;
        this.moveType = moveType;
        this.rarity = rarity;
        this.summonable = summonable;
        this.isInNormalPool = isInNormalPool;
        this.dateReleased = dateReleased;
        this.stats = stats;
        this.statGrowths = statGrowths;
    }

    /**
     * creates a Character according to the heroes currently in Fire Emblem Heroes
     * @param name - name of hero; MUST be in exact format: "[name]: [epithet]" (e.x. "Bartre: Fearless Warrior")
     */
    public Character(String name) {
        String epithet;
        try {
            epithet = name.substring(name.indexOf(':')+2);
        } catch (StringIndexOutOfBoundsException f) {
            System.out.println("not in correct format");
            throw new Error();
        }
        name = name.substring(0, name.indexOf(':'));
        ArrayList<Character> list = UnitDatabase.characters;
        ArrayList<Character> correctName = new ArrayList<>();

        for (Character j:list)
            if (j.getName().equals(name)&&j.getEpithet().equals(epithet))
                correctName.add(j);

        if (correctName.size()==0) {
            System.out.println("could not find "+name+": "+epithet+".");
            throw new Error();
        }

        //highly improbable
        if (correctName.size()>1) {
            System.out.println("ambiguity detected; BIG issue");
            throw new Error();
        }

        //there's probably a way to clone this, my brain is just too small
        Character j = list.get(0);
        this.name = j.getName();
        this.epithet = j.getEpithet();
        this.origin = j.getOrigin();
        this.color = j.getColor();
        this.weaponType = j.getWeaponType();
        this.moveType = j.getMoveType();
        this.stats = j.getStats();
        this.statGrowths = j.getStatGrowths();
        this.rarity = j.getRarity();
        this.summonable = j.isSummonable();
        this.isInNormalPool = j.isInNormalPool();
        this.dateReleased = j.getReleaseDate();
    }

    public Character(Character j) {
        this.name = j.getName();
        this.epithet = j.getEpithet();
        this.origin = j.getOrigin();
        this.color = j.getColor();
        this.weaponType = j.getWeaponType();
        this.moveType = j.getMoveType();
        this.stats = j.getStats();
        this.statGrowths = j.getStatGrowths();
        this.rarity = j.getRarity();
        this.summonable = j.isSummonable();
        this.isInNormalPool = j.isInNormalPool();
        this.dateReleased = j.getReleaseDate();
    }



    public String getName() { return name; }
    public String getEpithet() { return epithet; }
    public String getOrigin() { return origin; }
    public String getColor() { return color; }
    public String getWeaponType() { return weaponType; }
    public String getMoveType() { return moveType; }
    // TODO: change to lv40 stats using lv1 stats and growths
    public int[] getStats() { return stats; }
    public int[] getStatGrowths() { return statGrowths; }
    public int getHP() { return stats[0]; }
    public int getAtk() { return stats[1]; }
    public int getSpd() { return stats[2]; }
    public int getDef() { return stats[3]; }
    public int getRes() { return stats[4]; }
    public int getBST() {
        int bst = 0;
        for (int i:stats) bst+=i;
        return bst;
    }

    public int getRarity() { return rarity; }
    public boolean isSummonable() { return summonable; }
    public boolean isInNormalPool() { return isInNormalPool; }
    public GregorianCalendar getReleaseDate() { return dateReleased; }

    //TODO: fix somehow
    //public String toString() { return getAllStats(false, 5); }

    public int[][] getAllStats(boolean lv1, int rarity) {
        int[][] finalStats = {
                stats.clone(),
                stats.clone(),
                stats.clone(),
        };

        for (int i=0; i<3; i++) {
            finalStats[i][0]+= (i-1);
            finalStats[i][1]+= (i-1);
            finalStats[i][2]+= (i-1);
            finalStats[i][3]+= (i-1);
            finalStats[i][4]+= (i-1);
        }

        int[] statsSorted = {1, 2, 3, 4};

        for (int i=0; i<4; i++) {
            for (int j = 0; j < 3; j++) {
                if (stats[statsSorted[j]]<stats[statsSorted[j+1]]) {
                    int t = statsSorted[j+1];
                    statsSorted[j+1] = statsSorted[j];
                    statsSorted[j] = t;
                }
            }
        }



        //for (int i:statsSorted) System.out.println(i);



        switch (rarity) {
            case 1:
                //two highest non-hp stats
                for (int i=0; i<3; i++) {
                    finalStats[i][statsSorted[0]]--;
                    finalStats[i][statsSorted[1]]--;
                }
            case 2:
                //hp & two lowest stats
                for (int i=0; i<3; i++) {
                    finalStats[i][0]--;
                    finalStats[i][statsSorted[2]]--;
                    finalStats[i][statsSorted[3]]--;
                }
            case 3:
                //two highest non-hp stats
                for (int i=0; i<3; i++) {
                    finalStats[i][statsSorted[0]]--;
                    finalStats[i][statsSorted[1]]--;
                }
            case 4:
                //hp & two lowest stats
                for (int i=0; i<3; i++) {
                    finalStats[i][0]--;
                    finalStats[i][statsSorted[2]]--;
                    finalStats[i][statsSorted[3]]--;
                }
            case 5:
                //nothin'
                break;
        }

        if (!lv1) {
            int rarityFactor;
            switch (rarity) {
                case 1: rarityFactor = 86; break;
                case 2: rarityFactor = 93; break;
                case 3: rarityFactor = 100; break;
                case 4: rarityFactor = 107; break;
                case 5: rarityFactor = 114; break;
                default:
                    System.out.println("rarity is not within bounds");
                    return null;
            }

            for (int i=0; i<3; i++) {
                for (int j=0; j<5; j++) {
                    //TODO: expand with steps
                    finalStats[i][j]+= (int)(0.39*(int)((this.statGrowths[j]+5*(i-1))*rarityFactor/100.0));
                }
            }
        }

        return finalStats;
    }



    public int[] getStats(boolean lv1, int rarity, int boon, int bane) {
        //duplicate
        int[] finalStats = stats.clone();

        int[] statsSorted = {1, 2, 3, 4};

        for (int i=0; i<4; i++) {
            for (int j = 0; j < 3; j++) {
                if (stats[statsSorted[j]]<stats[statsSorted[j+1]]) {
                    int t = statsSorted[j+1];
                    statsSorted[j+1] = statsSorted[j];
                    statsSorted[j] = t;
                }
            }
        }



        //for (int i:statsSorted) System.out.println(i);

        //boon/bane lv1 changing
        if (boon>=0&&bane>=0) {
            finalStats[boon]++;
            finalStats[bane]--;
        }

        switch (rarity) {
            case 1:
                //two highest non-hp stats
                finalStats[statsSorted[0]]--;
                finalStats[statsSorted[1]]--;
            case 2:
                //hp & two lowest stats
                finalStats[0]--;
                finalStats[statsSorted[2]]--;
                finalStats[statsSorted[3]]--;
            case 3:
                //two highest non-hp stats
                finalStats[statsSorted[0]]--;
                finalStats[statsSorted[1]]--;
            case 4:
                //hp & two lowest stats
                finalStats[0]--;
                finalStats[statsSorted[2]]--;
                finalStats[statsSorted[3]]--;
            case 5:
                //nothin'
                break;
        }

        if (!lv1) {
            int rarityFactor;
            switch (rarity) {
                case 1: rarityFactor = 86; break;
                case 2: rarityFactor = 93; break;
                case 3: rarityFactor = 100; break;
                case 4: rarityFactor = 107; break;
                case 5: rarityFactor = 114; break;
                default:
                    System.out.println("rarity is not within bounds");
                    return null;
            }


            //incomplete
            for (int i=0; i<finalStats.length; i++) {
                //TODO: simplify with steps
                if (i==boon)
                    finalStats[i]+= (int) (0.39 * (int) ((this.statGrowths[i]+5) * rarityFactor / 100.0));
                else if (i==bane)
                    finalStats[i]+= (int) (0.39 * (int) ((this.statGrowths[i]-5) * rarityFactor / 100.0));
                else
                    finalStats[i]+= (int) (0.39 * (int) (this.statGrowths[i] * rarityFactor / 100.0));
            }
        }

        return finalStats;
    }





    public static void main(String[] args) {
        //TODO: fix name-based initializer
        /*
        Scanner console = new Scanner(System.in);

        //test creating characters
        String name = console.nextLine();
        while (!name.equals("quit")) {
            Character x;
            try {
                x = new Character(name);
            } catch (Exception g) {
                System.out.println("invalid character name; try again:");
                name = console.nextLine();
                continue;
            }

            for (int i=1; i<=5; i++)
                System.out.println(x.getStats(false, i, 1, 1));
            //System.out.println(x);
            System.out.println();

            name = console.nextLine();
        }
        */
    }
}



//h = hp, a = atk, s = spd, d = def, r = res












