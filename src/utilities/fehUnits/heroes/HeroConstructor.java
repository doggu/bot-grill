package utilities.fehUnits.heroes;

import java.util.GregorianCalendar;

/**
 * a class which allows for the construction of an object that stores information for a hero without requiring all of
 * said components to be produced at once.
 *
 */
public class HeroConstructor {
    private String name, epithet, origin, color, weaponType, moveType;
    //these stats are 1* lv1 (regardless of obtainable rarities)
    private int[] stats, statGrowths;
    private Integer rarity;
    private Boolean summonable, isInNormalPool;

    private GregorianCalendar dateReleased;

    public void setName(String name) { this.name = name; }
    public void setEpithet(String epithet) { this.epithet = epithet; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setColor(String color) { this.color = color; }
    public void setWeaponType(String weaponType) { this.weaponType = weaponType; }
    public void setMoveType(String moveType) { this.weaponType = weaponType; }
    public void setStats(int[] stats) { this.stats = stats; }
    public void setStatGrowths(int[] statGrowths) { this.statGrowths = statGrowths; }
    public void setRarity(int rarity) { this.rarity = rarity; }
    public void setSummonable(boolean summonable) { this.summonable = summonable; }
    public void setInNormalPool(boolean isInNormalPool) { this.isInNormalPool = isInNormalPool; }
    public void setDateReleased(GregorianCalendar dateReleased) { this.dateReleased = dateReleased; }

    public String getName() { return name; }
    public String getEpithet() { return epithet; }
    public String getOrigin() { return origin; }
    public String getColor() { return color; }
    public String getWeaponType() { return weaponType; }
    public String getMoveType() { return moveType; }
    public int[] getStats() { return stats; }
    public int[] getStatGrowths() { return statGrowths; }
    public int getRarity() { return rarity; }
    public boolean getSummonable() { return summonable; }
    public boolean getInNormalPool() { return isInNormalPool; }
    public GregorianCalendar getDateReleased() { return dateReleased; }

    public Hero createHero() throws Error {
        if (name==null) {
            System.out.println("missing name!");
            throw new Error();
        }
        if (epithet==null) {
            System.out.println("missing epithet!");
            throw new Error();
        }
        if (origin==null) {
            System.out.println("missing origin!");
            throw new Error();
        }
        if (color==null) {
            System.out.println("missing color!");
            throw new Error();
        }
        if (weaponType==null) {
            System.out.println("missing weaponType!");
            throw new Error();
        }
        if (moveType==null) {
            System.out.println("missing moveType!");
            throw new Error();
        }
        if (stats==null) {
            System.out.println("missing stats!");
            throw new Error();
        }
        if (statGrowths==null) {
            System.out.println("missing statGrowths!");
            throw new Error();
        }
        if (rarity==null) {
            System.out.println("missing rarity!");
            throw new Error();
        }
        if (summonable==null) {
            System.out.println("missing summonable!");
            throw new Error();
        }
        if (isInNormalPool==null) {
            System.out.println("missing isInNormalPool!");
            throw new Error();
        }
        if (dateReleased==null) {
            System.out.println("missing dateReleased!");
            throw new Error();
        }

        return new Hero(name, epithet, origin,
                color, weaponType, moveType, rarity,
                summonable, isInNormalPool, dateReleased,
                stats, statGrowths);
    }
}