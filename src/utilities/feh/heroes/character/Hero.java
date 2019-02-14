package utilities.feh.heroes.character;

import utilities.feh.heroes.UnitDatabase;
import utilities.feh.skills.Skill;

import java.util.*;

//TODO: include base skills

public class Hero {
    private final HeroName fullName;
    private final String origin, color;
    private final WeaponClass weaponType;
    private final MovementClass moveType;

    //these stats are 1* lv1 (regardless of obtainable rarities)
    private final HeroStats stats;
    private final int rarity;

    private final Availability availability;
    private final GregorianCalendar dateReleased;

    private final ArrayList<Skill> baseKit;



    public Hero(HeroName fullName, String origin, String color,
                WeaponClass weaponType, MovementClass moveType,
                int rarity, Availability availability,
                GregorianCalendar dateReleased,
                HeroStats stats, ArrayList<Skill> baseKit) {
        this.fullName = fullName;
        this.origin = origin;
        this.color = color;
        this.weaponType = weaponType;
        this.moveType = moveType;
        this.rarity = rarity;
        this.availability = availability;
        this.dateReleased = dateReleased;
        this.stats = stats;
        this.baseKit = baseKit;
    }

    /**
     * creates a Hero according to the heroes currently in Fire Emblem Heroes
     * @param name - name of hero; MUST be in exact format: "[name]: [epithet]" (e.x. "Bartre: Fearless Warrior")
     */
    public Hero(String name) {
        try {
            name.substring(name.indexOf(':')+2);
        } catch (StringIndexOutOfBoundsException f) {
            System.out.println("not in correct format");
            throw new Error();
        }
        ArrayList<Hero> list = UnitDatabase.HEROES;
        ArrayList<Hero> correctName = new ArrayList<>();

        for (Hero j:list)
            if (j.getFullName().toString().equals(name))
                correctName.add(j);

        if (correctName.size()==0) {
            System.out.println("could not find "+name+".");
            throw new Error();
        }

        //highly improbable
        if (correctName.size()>1) {
            System.out.println("ambiguity detected; BIG issue");
            throw new Error();
        }

        //there's probably a way to clone this, my brain is just too small
        Hero j = correctName.get(0);
        this.fullName = j.getFullName();
        this.origin = j.getOrigin();
        this.color = j.getColor();
        this.weaponType = j.getWeaponType();
        this.moveType = j.getMoveType();
        this.stats = j.getStats();
        this.rarity = j.getRarity();
        this.availability = j.getAvailability();
        this.dateReleased = j.getReleaseDate();
        this.baseKit = j.getBaseKit();
    }

    public Hero(Hero j) {
        this.fullName = j.getFullName();
        this.origin = j.getOrigin();
        this.color = j.getColor();
        this.weaponType = j.getWeaponType();
        this.moveType = j.getMoveType();
        this.stats = j.getStats();
        this.rarity = j.getRarity();
        this.availability = j.getAvailability();
        this.dateReleased = j.getReleaseDate();
        this.baseKit = j.getBaseKit();
    }



    public HeroName getFullName() { return fullName; }
    public String getOrigin() { return origin; }

    public String getColor() { return color; }
    public WeaponClass getWeaponType() { return weaponType; }
    public MovementClass getMoveType() { return moveType; }

    // TODO: change to lv40 stats using lv1 stats and growths
    public HeroStats getStats() { return stats; }
    public int[] getStatsArr() { return stats.getStatsAsArray(); }
    public int getHP() { return stats.getHp(); }
    public int getAtk() { return stats.getAtk(); }
    public int getSpd() { return stats.getSpd(); }
    public int getDef() { return stats.getDef(); }
    public int getRes() { return stats.getRes(); }

    public int getRarity() { return rarity; }
    public Availability getAvailability() { return availability; }
    public boolean isSummonable() { return availability.isSummonable(); }
    public boolean isInNormalPool() { return availability.isInNormalPool(); }
    public GregorianCalendar getReleaseDate() { return dateReleased; }
    public ArrayList<Skill> getBaseKit() { return baseKit; }

    public boolean hasSuperBoon() { return hasEccentricStat(true); }
    public boolean hasSuperBane() { return hasEccentricStat(false); }
    public boolean hasEccentricStat(boolean boon) {
        int[][] stats = getAllStats(false, 5);
        int row = (boon?0:2);
        for (int i=0; i<stats[2].length; i++)
            if (Math.abs(stats[1][row]-stats[2][row])>3)
                return true;
        return false;
    }



    public String toString() { return this.getFullName().toString(); }

    public int[] getStats(boolean lv1, int rarity, char boon, char bane) {
        int boonN = 0, baneN = 0;
        switch (boon) {
            case 'r': boonN++;
            case 'd': boonN++;
            case 's': boonN++;
            case 'a': boonN++;
            case 'h': break;
            default: throw new Error();
        }
        switch (bane) {
            case 'r': baneN++;
            case 'd': baneN++;
            case 's': baneN++;
            case 'a': baneN++;
            case 'h': break;
            default: throw new Error();
        }

        return getStats(lv1, rarity, boonN, baneN);
    }
    public int[] getStats(boolean lv1, int rarity, int boon, int bane) { return getStats(lv1, rarity, boon, bane, 0, 0); }
    public int[] getStats(boolean lv1, int rarity, int boon, int bane, int merges, int dragonflowers) {
        //duplicate
        int[][] rawStats = getAllStats(lv1, rarity, merges);
        int[] finalStats = new int[5];
        for (int i=0; i<5; i++) {
            if (i==boon) {
                finalStats[i] = rawStats[2][i];
            } else if (i==bane) {
                finalStats[i] = rawStats[0][i];
            } else {
                finalStats[i] = rawStats[1][i];
            }
        }

        return finalStats;

        //#obsoleted lmao i'm fuckin dumb
        /*
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
                if (i==boon)
                    finalStats[i]+= (int) (0.39 * (int) ((this.statGrowths[i]+5) * rarityFactor / 100.0));
                else if (i==bane)
                    finalStats[i]+= (int) (0.39 * (int) ((this.statGrowths[i]-5) * rarityFactor / 100.0));
                else
                    finalStats[i]+= (int) (0.39 * (int) (this.statGrowths[i] * rarityFactor / 100.0));
            }
        }

        return finalStats;
        */
    }

    public int[][] getAllStats(boolean lv1, int rarity) { return getAllStats(lv1, rarity, 0); }
    public int[][] getAllStats(boolean lv1, int rarity, int merges) {
        int[][] finalStats = {
                { stats.getHp(), stats.getAtk(), stats.getSpd(), stats.getDef(), stats.getRes() },
                { stats.getHp(), stats.getAtk(), stats.getSpd(), stats.getDef(), stats.getRes() },
                { stats.getHp(), stats.getAtk(), stats.getSpd(), stats.getDef(), stats.getRes() },
        };

        for (int i=0; i<3; i++) {
            finalStats[i][0]+= (i-1);
            finalStats[i][1]+= (i-1);
            finalStats[i][2]+= (i-1);
            finalStats[i][3]+= (i-1);
            finalStats[i][4]+= (i-1);
        }

        int[] statsSorted = {0, 1, 2, 3, 4};

        for (int i=0; i<5; i++) {
            for (int j = 0; j < 4; j++) {
                if (stats.getStatsAsArray()[statsSorted[j]]<stats.getStatsAsArray()[statsSorted[j+1]]) {
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
                    finalStats[i][statsSorted[1]]--;
                    finalStats[i][statsSorted[2]]--;
                }
            case 2:
                //hp & two lowest stats
                for (int i=0; i<3; i++) {
                    finalStats[i][statsSorted[0]]--;
                    finalStats[i][statsSorted[3]]--;
                    finalStats[i][statsSorted[4]]--;
                }
            case 3:
                //two highest non-hp stats
                for (int i=0; i<3; i++) {
                    finalStats[i][statsSorted[1]]--;
                    finalStats[i][statsSorted[2]]--;
                }
            case 4:
                //hp & two lowest stats
                for (int i=0; i<3; i++) {
                    finalStats[i][statsSorted[0]]--;
                    finalStats[i][statsSorted[3]]--;
                    finalStats[i][statsSorted[4]]--;
                }
            case 5:
                //nothin'
                break;
        }

        for (int i=0; i<merges*2; i++) {
            for (int j=0; j<3; j++) {
                finalStats[j][statsSorted[i%5]]++;
            }
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
                    finalStats[i][j]+= (int)(0.39*(int)((this.stats.getGrowthsAsArray()[j]+5*(i-1))*rarityFactor/100.0));
                }
            }
        }

        return finalStats;
    }



    public static void main(String[] args) {
        //TODO: fix name-based initializer
        Scanner console = new Scanner(System.in);

        //test creating heroes
        String name = console.nextLine();
        while (!name.equals("quit")) {
            Hero x;
            try {
                x = new Hero(name);
            } catch (Exception g) {
                System.out.println("invalid character name; try again:");
                name = console.nextLine();
                continue;
            }

            for (int i=1; i<=5; i++) {
                for (int j = 0; j < 5; j++)
                    System.out.println(x.getStats(false, i, 1, 1)[j]);
                System.out.println();
            }
            //System.out.println(x);
            System.out.println();
            System.out.println();
            System.out.println();

            name = console.nextLine();
        }
    }
}