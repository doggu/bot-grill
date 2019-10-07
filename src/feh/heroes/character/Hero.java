package feh.heroes.character;

import feh.heroes.UnitDatabase;
import feh.heroes.skills.analysis.StatModifier;
import feh.heroes.skills.skillTypes.Skill;

import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Hero {
    private final HeroName fullName;
    private final Origin origin;
    private final URL gamepediaLink, portraitLink;
    private final String artist;
    private final char gender;

    private final char color;
    private final WeaponClass weaponType;
    private final MovementClass moveType;

    //these stats are 3* lv1 (regardless of obtainable rarities)
    private final HeroStats stats;
    private final int summonableRarity; //TODO: summonable rarity is very complicated these days

    private final Availability availability;
    private final GregorianCalendar dateReleased;
    //private final String artist;

    private final ArrayList<Skill> baseKit;


    public Hero(HeroName fullName, Origin origin, URL gamepediaLink, URL portraitLink, String artist, char gender,
                char color, WeaponClass weaponType, MovementClass moveType,
                int summonableRarity, Availability availability,
                GregorianCalendar dateReleased,
                HeroStats stats, ArrayList<Skill> baseKit) {
        this.fullName = fullName;
        this.origin = origin;
        this.gamepediaLink = gamepediaLink;
        this.portraitLink = portraitLink;
        this.artist = artist;
        this.gender = gender;
        this.color = color;
        this.weaponType = weaponType;
        this.moveType = moveType;
        this.summonableRarity = summonableRarity;
        this.availability = availability;
        this.dateReleased = dateReleased;
        this.stats = stats;
        this.baseKit = baseKit;
    }

    /**
     * creates a Hero according to the heroes currently in Fire Emblem Heroes
     *
     * @param name - name of hero; MUST be in exact format: "[name]: [epithet]" (e.x. "Bartre: Fearless Warrior")
     */
    public Hero(String name) {
        if (name.indexOf(':') < 0) throw new Error("incorrect name format");
        ArrayList<Hero> list = UnitDatabase.HEROES;
        ArrayList<Hero> correctName = new ArrayList<>();

        for (Hero j : list)
            if (j.getFullName().toString().equals(name))
                correctName.add(j);

        if (correctName.size() == 0) {
            System.out.println("could not find " + name + ".");
            throw new Error();
        }

        //highly improbable
        if (correctName.size() > 1) {
            System.out.println("ambiguity detected; BIG issue");
            throw new Error();
        }

        //there's probably a way to clone this, my brain is just too small
        //stack overflow doesn't know so a better solution doesn't exist
        Hero j = correctName.get(0);
        this.fullName = j.fullName;
        this.origin = j.origin;
        this.gamepediaLink = j.gamepediaLink;
        this.portraitLink = j.portraitLink;
        this.artist = j.artist;
        this.gender = j.gender;
        this.color = j.color;
        this.weaponType = j.weaponType;
        this.moveType = j.moveType;
        this.stats = j.stats;
        this.summonableRarity = j.summonableRarity;
        this.availability = j.availability;
        this.dateReleased = j.dateReleased;
        this.baseKit = j.baseKit;
    }

    public Hero(Hero j) {
        this.fullName = j.fullName;
        this.origin = j.origin;
        this.gamepediaLink = j.gamepediaLink;
        this.portraitLink = j.portraitLink;
        this.artist = j.artist;
        this.gender = j.gender;
        this.color = j.color;
        this.weaponType = j.weaponType;
        this.moveType = j.moveType;
        this.stats = j.stats;
        this.summonableRarity = j.summonableRarity;
        this.availability = j.availability;
        this.dateReleased = j.dateReleased;
        this.baseKit = j.baseKit;
    }


    public HeroName getFullName() { return fullName; }
    public Origin getOrigin() { return origin; }
    public URL getGamepediaLink() { return gamepediaLink; } //can technically be constructed from hero name
    public URL getPortraitLink() { return portraitLink; }
    public String getArtist() { return artist; }
    public char getGender() { return gender; }
    public char getColor() { return color; }
    public WeaponClass getWeaponType() { return weaponType; }
    public MovementClass getMoveType() { return moveType; }
    public boolean is(HeroClass type) { return weaponType == type || moveType == type; }
    // TODO: change to lv40 stats using lv1 stats and growths
    public HeroStats getStats() { return stats; }
    public int getHP() { return stats.getHp(); }
    public int getAtk() { return stats.getAtk(); }
    public int getSpd() { return stats.getSpd(); }
    public int getDef() { return stats.getDef(); }
    public int getRes() { return stats.getRes(); }
    public int getSummonableRarity() { return summonableRarity; }
    public Availability getAvailability() { return availability; }
    public boolean isSummonable() { return availability.isSummonable(); }
    public boolean isInNormalPool() { return availability.isInNormalPool(); }
    public GregorianCalendar getReleaseDate() { return dateReleased; }
    public ArrayList<Skill> getBaseKit() { return baseKit; }


    public String toString() {
        return this.getFullName().toString();
    }



    public int[][] getAllStats(boolean lv1, int rarity) {
        return getAllStats(lv1, rarity, null);
    }
    //todo: swap the trickle down here (skills param should be an offshoot of two-parameter call)
    public int[][] getAllStats(boolean lv1, int rarity, ArrayList<Skill> skills) {
        int[][] finalStats = {
                { stats.getHp(), stats.getAtk(), stats.getSpd(), stats.getDef(), stats.getRes() },
                { stats.getHp(), stats.getAtk(), stats.getSpd(), stats.getDef(), stats.getRes() },
                { stats.getHp(), stats.getAtk(), stats.getSpd(), stats.getDef(), stats.getRes() },
        };

        for (int i = 0; i < 3; i++) { //technically (if i==1) return; would be more efficient here
            //TeChnicAlLY thIS iS SHiT
            for (int j=0; j<5; j++) {
                finalStats[i][j] += (i - 1);
            }
        }

        int[] statsSorted = getStatsSorted(stats);


        switch (rarity) {
            case 1:
                //two highest non-hp stats
                for (int i = 0; i < 3; i++) {
                    finalStats[i][statsSorted[1]]--;
                    finalStats[i][statsSorted[2]]--;
                }
            case 2:
                //hp & two lowest stats
                for (int i = 0; i < 3; i++) {
                    finalStats[i][statsSorted[0]]--;
                    finalStats[i][statsSorted[3]]--;
                    finalStats[i][statsSorted[4]]--;
                }
            case 3:
                //two highest non-hp stats
                for (int i = 0; i < 3; i++) {
                    finalStats[i][statsSorted[1]]--;
                    finalStats[i][statsSorted[2]]--;
                }
            case 4:
                //hp & two lowest stats
                for (int i = 0; i < 3; i++) {
                    finalStats[i][statsSorted[0]]--;
                    finalStats[i][statsSorted[3]]--;
                    finalStats[i][statsSorted[4]]--;
                }
            case 5:
                //nothin'
                break;
        }


        if (!lv1) {
            int rarityFactor = 79; //0* base (hypothetically)

            for (int i=0; i<rarity; i++) {
                rarityFactor+= 7; //add 7% per rarity increase
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    //TODO: expand with steps
                    finalStats[i][j]+= (int)(0.39*((this.stats.getGrowthsAsArray()[j]+5*(i-1))*rarityFactor/100));
                }
            }
        }

        if (skills!=null) {
            for (Skill skill:skills) {
                if (!(skill instanceof StatModifier)) continue;
                int[] modifiers = ((StatModifier) skill).getStatModifiers();
                for (int i=0; i<finalStats.length; i++) {
                    for (int j=0; j<finalStats[i].length; j++) {
                        finalStats[i][j]+= modifiers[j];
                    }
                }
            }
        }

        return finalStats;
    }

    private static int[] getStatsSorted(HeroStats stats) {
        int[] statsSorted = {0, 1, 2, 3, 4};

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (stats.getStatsAsArray()[statsSorted[j]] < stats.getStatsAsArray()[statsSorted[j + 1]]) {
                    int t = statsSorted[j + 1];
                    statsSorted[j + 1] = statsSorted[j];
                    statsSorted[j] = t;
                }
            }
        }
        return statsSorted;
    }
}