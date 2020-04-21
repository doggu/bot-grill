package feh.characters.unit;

import feh.characters.hero.Hero;
import feh.characters.skills.analysis.StatModifier;
import feh.characters.skills.skillTypes.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Unit extends Hero {
    private String nickname = null;
    private final int rarity, boon, bane; //I refuse to call it asset/flaw
    private int level; //can only be 1 or 40 right now
    private int supportLevels;
    private int merges, dragonflowers;

    //skills in superclass becomes repository for all skills
    //todo: learned and unlearned skills
    // inherited (expensive) and natural skills
    private final ArrayList<Skill> allSkills;
    //todo: create ActiveKit class (with specific slots n shit)
    private final ArrayList<Skill> activeKit;

    //private final Summoner owner
    //todo: this is somewhat redundant to the user's barracks
    // this should probably be moved to the Barracks or Summoner class
    // as an ArrayList of Units or Pairs for past summoner supports and
    // current ally supports
    // however, a reference to this would be necessary to invoke support status

    private Blessing blessing;

    private int sp;
    private int hm;



    //TODO: create character class
    // how do i check things off 40%

    //TODO: add nickname param to a unit constructor for persistence

    /**
     * creates a new unit based on a hero;
     * accepts IVs. NOTE: IVs must not be of the same stat
     *
     * @param hero - hero which corresponds with the summoned hero.
     * @param boon - positive variance of specific unit.
     * @param bane - negative variance of specific unit.
     */
    public Unit(Hero hero, int rarity, int boon, int bane,
                int level, int supportLevels, int merges, int dragonflowers,
                Blessing blessing, int sp, int hm, 
                ArrayList<Skill> allSkills, ArrayList<Skill> activeKit) {
        super(hero);
        this.rarity = rarity;
        this.boon = boon;
        this.bane = bane;
        this.level = level;
        this.supportLevels = supportLevels;
        this.merges = merges;
        this.dragonflowers = dragonflowers;

        this.blessing = blessing;

        this.sp = sp;
        this.hm = hm;

        this.allSkills = allSkills;
        this.activeKit = activeKit;
    }
    public Unit(Hero hero, int rarity, int boon, int bane,
                int level, int supportLevels, int merges, int dragonflowers,
                Blessing blessing, int sp, int hm) {
        this(hero, rarity, boon, bane,
                level, supportLevels, merges, dragonflowers,
                blessing, sp, hm, hero.getBaseKit(), hero.getBaseKit());
    }
    public Unit(Hero hero, int rarity, int boon, int bane,
                int level, int supportLevels, int merges, int dragonflowers) {
        this(hero, rarity, boon, bane, 
                level, supportLevels, merges, dragonflowers, 
                null, 0, 0);
    }
    public Unit(Hero hero, int rarity, int boon, int bane, 
                int level, int supportLevels) {
        this(hero, rarity, boon, bane, 
                level, supportLevels, 
                0, 0);
    }
    public Unit(Hero hero, int rarity, int boon, int bane, 
                int level, int supportLevels, int merges) {
        this(hero, rarity, boon, bane, 
                level, supportLevels, merges, 
                0);
    }
    public Unit(Hero hero, int rarity, int boon, int bane, int level) {
        this(hero, rarity, boon, bane, 
                level, 
                -1);
    }
    //experimental, for creating FieldedUnits which extend this
    public Unit(Unit unit) {
        this(unit, unit.rarity, unit.boon, unit.bane,
                unit.level, unit.supportLevels, unit.merges, unit.dragonflowers,
                unit.blessing, unit.sp, unit.hm, unit.allSkills, unit.activeKit);
    }

    //todo: where the fuck do i put this
    private static final int
            N = -1,
            HP = 0,
            ATK = 1,
            SPD = 2,
            DEF = 3,
            RES = 4;

    /**
     * creates a new unit based on a character;
     * generates IVs randomly
     * no summoner support, as this is a "new" instance (subject to change)
     *
     * @param unit - name of hero. must follow EXACT format:
     *             "[name]: [epithet]"
     * @param rarity - rarity of hero.
     */
    public Unit(Hero unit, int rarity) {
        super(unit);
        this.rarity = rarity;

        int boon, bane;
        int individuality = (int)(Math.random()*21);
        //damn this is fuckin retarded
        switch (individuality) {
            case 0:  boon = N;   bane = N;   break;
            case 1:  boon = HP;  bane = ATK; break;
            case 2:  boon = HP;  bane = SPD; break;
            case 3:  boon = HP;  bane = DEF; break;
            case 4:  boon = HP;  bane = RES; break;
            case 5:  boon = ATK; bane = HP;  break;
            case 6:  boon = ATK; bane = SPD; break;
            case 7:  boon = ATK; bane = DEF; break;
            case 8:  boon = ATK; bane = RES; break;
            case 9:  boon = SPD; bane = HP;  break;
            case 10: boon = SPD; bane = ATK; break;
            case 11: boon = SPD; bane = DEF; break;
            case 12: boon = SPD; bane = RES; break;
            case 13: boon = DEF; bane = HP;  break;
            case 14: boon = DEF; bane = ATK; break;
            case 15: boon = DEF; bane = SPD; break;
            case 16: boon = DEF; bane = RES; break;
            case 17: boon = RES; bane = HP;  break;
            case 18: boon = RES; bane = ATK; break;
            case 19: boon = RES; bane = SPD; break;
            case 20: boon = RES; bane = DEF; break;
            default:
                System.out.println("something hella broke");
                throw new Error();
        }

        this.boon = boon;
        this.bane = bane;
        this.level = 1;
        supportLevels = -1;
        this.merges = 0;
        this.dragonflowers = 0;

        allSkills = super.getBaseKit();
        activeKit = null; //super.getBaseKit();
    }



    //todo: set up unit manager for security?

    public void addMerge(Unit u) {
        if (u.matches(this)) {
            switch (Integer.compare(rarity, u.rarity)) {
                case -1:
                case 0:
                case 1:
            }
            merges+= 1+u.merges;
        }
    }
    public void addDF(int dragonflowers) { 
        this.dragonflowers+= dragonflowers; }
    public void giveSkill(Skill skill) { 
        allSkills.add(skill); }
    public void equip(ArrayList<Skill> skills) {
        allSkills.addAll(skills); }
    public void setBlessing(Blessing blessing) {
        this.blessing = blessing; }
    public void setNickname(String nickname) {
        this.nickname = nickname; }



    public int getRarity() {
        return rarity; }
    public int getBoon() {
        return boon; }
    public int getBane() {
        return bane; }
    public int getLevel() {
        return level; }
    public int getMerges() {
        return merges; }
    public int getDragonflowers() {
        return dragonflowers; }
    public Blessing getBlessing() {
        return blessing; }

    public ArrayList<Skill> getAllSkills() {
        return new ArrayList<>(allSkills); }
    public ArrayList<Skill> getActiveKit() {
        return new ArrayList<>(activeKit); }

    public int getHM() {
        return hm; }
    public int getSP() {
        return sp; }

    private boolean hasNickname() {
        return nickname!=null; }
    public String getNickname() {
        return hasNickname()?nickname:getFullName().toString(); }

    //todo: should a dedicated support refresh button be added?
    // it would be less intuitive but also
    // remind me to regrab supports for each map
    private static final int
            LEVEL_C = 0,
            LEVEL_B = 8,
            LEVEL_A = 32,
            LEVEL_S = 80,
            R_LEVEL_C = 0,
            R_LEVEL_B = 4,
            R_LEVEL_A = 16,
            R_LEVEL_S = 40;
    public char getSupportStatus() {
        char supportStatus;
        //if (owner.hasSupported(this)) {
            if (supportLevels>=LEVEL_S) {
                supportStatus = 's';
            } else if (supportLevels>=LEVEL_A) {
                supportStatus = 'a';
            } else if (supportLevels>=LEVEL_B) {
                supportStatus = 'b';
            } else  if (supportLevels>=LEVEL_C) {
                supportStatus = 'c';
            } else { //-1, uninitiated
                supportStatus = 'd';
            }
        /*} else {
            if (supportLevels >= LEVEL_S) {
                supportStatus = 's';
            } else if (supportLevels >= LEVEL_A) {
                supportStatus = 'a';
            } else if (supportLevels >= LEVEL_B) {
                supportStatus = 'b';
            } else if (supportLevels >= LEVEL_C) {
                supportStatus = 'c';
            }
        }
         */
        return supportStatus;
    }


    //todo: produces the stats even when circumstances are impossible
    // may want to change this later with exceptions near HeroRetriever
    public int[] getStatsArr() {
        //duplicate
        int[][] rawStats = getAllStats(level==1, rarity);

        //i can't believe i failed java 101
        int[] finalStats = Arrays.copyOf(rawStats[1],5);
        if (boon>=0) {
            finalStats[boon] = rawStats[2][boon];
            if (bane>=0)
                finalStats[bane] = rawStats[0][bane];
        }

        int[] statsSorted;
        if (level!=1) {
            int[][] rawStatsLv1 = getAllStats(true, rarity);
            int[] statsLv1 = rawStatsLv1[1];
            if (boon>=0) {
                statsLv1[boon] = rawStatsLv1[2][boon];
                if (bane>=0)
                    statsLv1[bane] = rawStatsLv1[0][bane];
            }

            statsSorted = getStatsSorted(statsLv1);
        } else {
            statsSorted = getStatsSorted(finalStats);
        }

        //this could be simpler in the finalStats creation
        // but it's easier to read like this imo
        if (merges > 0) {
            if (boon == -1 && bane == -1) {  //add to top neutral stats
                finalStats[statsSorted[0]]++;
                finalStats[statsSorted[1]]++;
                finalStats[statsSorted[2]]++;
            } else if (bane>=0) {  //neutralize the bane
                finalStats[bane] = rawStats[1][bane];
            }
        }

        for (int i = 0; i < merges * 2; i++)
            finalStats[statsSorted[i % 5]]++;

        for (int i = 0; i < dragonflowers; i++)
            finalStats[statsSorted[i % 5]]++;

        switch (getSupportStatus()) {
            case 's':
                finalStats[0]++;
                finalStats[1] += 2;
            case 'a':
                finalStats[2] += 2;
            case 'b':
                finalStats[0]++;
                finalStats[3] += 2;
            case 'c':
                finalStats[0] += 3;
                finalStats[4] += 2;
                break;
            case 'd':
            default:
        }


        if (activeKit != null) {
            for (Skill x : activeKit) {
                if (!(x instanceof StatModifier)) continue;

                int[] modifiers = ((StatModifier) x).getStatModifiers();
                for (int i = 0; i < 5; i++)
                    finalStats[i] += modifiers[i];
            }
        }


        return finalStats;
    }

    private static int[] getStatsSorted(int[] stats) {
        int[] statsSorted = {0, 1, 2, 3, 4};

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (stats[statsSorted[j]] < stats[statsSorted[j + 1]]) {
                    int t = statsSorted[j + 1];
                    statsSorted[j + 1] = statsSorted[j];
                    statsSorted[j] = t;
                }
            }
        }
        return statsSorted;
    }



    public boolean matches(Unit unit) {
        return unit.getFullName().equals(this.getFullName());
    }
}
