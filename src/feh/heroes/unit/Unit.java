package feh.heroes.unit;

import feh.heroes.character.Hero;
import feh.heroes.skills.skillTypes.Skill;

import java.util.ArrayList;

public class Unit extends Hero {
    private final int rarity, boon, bane; //I refuse to call it asset/flaw
    private int supportLevels;
    private final int merges, dragonflowers;

    //skills in superclass becomes repository for all skills
    private final ArrayList<Skill> allSkills;
    private final ArrayList<Skill> activeKit;

    //private final Summoner owner
    //todo: this is somewhat redundant to the user's barracks
    // this should probably be moved to the Barracks or Summoner class
    // as an ArrayList of Units or Pairs for past summoner supports and
    // current ally supports



    private Blessing blessing = null;

    private int sp;
    private int hm;



    //TODO: create character class
    // how do i check things off 40%




    /**
     * creates a new unit based on a hero;
     * accepts IVs. NOTE: IVs must not be of the same stat
     *
     * @param hero - hero which corresponds with the summoned hero.
     * @param boon - positive variance of specific unit.
     * @param bane - negative variance of specific unit.
     */
    public Unit(Hero hero, int rarity, int boon, int bane, int supportLevels, int merges, int dragonflowers) {
        super(hero);
        this.rarity = rarity;
        this.boon = boon;
        this.bane = bane;
        this.supportLevels = supportLevels;
        this.merges = merges;
        this.dragonflowers = dragonflowers;

        allSkills = super.getBaseKit();
        activeKit = super.getBaseKit();
    }
    public Unit(Hero hero, int rarity, int boon, int bane, int supportLevels) {
        this(hero, rarity, boon, bane, supportLevels, 0, 0);
    }
    public Unit(Hero hero, int rarity, int boon, int bane, int supportLevels, int merges) {
        this(hero, rarity, boon, bane, supportLevels, merges, 0);
    }
    public Unit(Hero hero, int rarity, int boon, int bane) {
        this(hero, rarity, boon, bane, 'd');
    }

    public static final int
            NEUTRAL = -1,
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
            case 0: boon = NEUTRAL; bane = NEUTRAL; break;
            case 1: boon = HP; bane = ATK; break;
            case 2: boon = HP; bane = SPD; break;
            case 3: boon = HP; bane = DEF; break;
            case 4: boon = HP; bane = RES; break;
            case 5: boon = ATK; bane = HP; break;
            case 6: boon = ATK; bane = SPD; break;
            case 7: boon = ATK; bane = DEF; break;
            case 8: boon = ATK; bane = RES; break;
            case 9: boon = SPD; bane = HP; break;
            case 10: boon = SPD; bane = ATK; break;
            case 11: boon = SPD; bane = DEF; break;
            case 12: boon = SPD; bane = RES; break;
            case 13: boon = DEF; bane = HP; break;
            case 14: boon = DEF; bane = ATK; break;
            case 15: boon = DEF; bane = SPD; break;
            case 16: boon = DEF; bane = RES; break;
            case 17: boon = RES; bane = HP; break;
            case 18: boon = RES; bane = ATK; break;
            case 19: boon = RES; bane = SPD; break;
            case 20: boon = RES; bane = DEF; break;
            default:
                System.out.println("something hella broke");
                throw new Error();
        }

        this.boon = boon;
        this.bane = bane;
        supportLevels = 0;
        this.merges = 0;
        this.dragonflowers = 0;

        allSkills = super.getBaseKit();
        activeKit = super.getBaseKit();
    }



    //todo: set up unit manager for security?
    public void giveSkill(Skill skill) { allSkills.add(skill); }
    public void setBlessing(Blessing blessing) { this.blessing = blessing; }



    public int getBoon() { return boon; }
    public int getBane() { return bane; }
    public int getRarity() { return rarity; }
    public int getMerges() { return merges; }
    public int getDragonflowers() { return dragonflowers; }
    public Blessing getBlessing() { return blessing; }

    public ArrayList<Skill> getAllSkills() { return new ArrayList<>(allSkills); }
    public ArrayList<Skill> getActiveKit() { return new ArrayList<>(activeKit); }
    //todo: should a dedicated support refresh button be added?
    // it would be less intuitive but also remind me to regrab supports for each map
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
            if (supportLevels>=R_LEVEL_S) {
                supportStatus = 's';
            } else if (supportLevels>=R_LEVEL_A) {
                supportStatus = 'a';
            } else if (supportLevels>=R_LEVEL_B) {
                supportStatus = 'b';
            } else  if (supportLevels>=R_LEVEL_C) {
                supportStatus = 'c';
            } else {
                throw new Error("negative support level detected! integer overflows already!?");
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
    public int[] getIVs() {
        int[] stats = super.getStats().getStatsAsArray();

        for (int i=0; i<stats.length; i++) {
            if (i==boon)
                stats[i]++;
            if (i==bane)
                stats[i]--;
        }

        return stats;
    }
    public int[] getStatsArr() {
        int[] stats = super.getStats(false, rarity, boon, bane);

        switch (getSupportStatus()) {
            case 's':
                stats[0]++;
                stats[1]+= 2;
            case 'a':
                stats[2]+= 2;
            case 'b':
                stats[0]++;
                stats[3]+= 2;
            case 'c':
                stats[0]+= 3;
                stats[4]+= 2;
            case 'd':
                break;
            default:
                //technically shouldn't happen but it's okay I guess
                System.out.println("just letting you know that I didn't get a support status for "+this.getFullName());
                break;
        }

        return stats;
    }
}
