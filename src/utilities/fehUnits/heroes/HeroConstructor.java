package utilities.fehUnits.heroes;

import java.util.GregorianCalendar;

/**
 * a class which allows for the construction of an object that stores information for a hero without requiring all of
 * said components to be produced at once.
 *
 */
class HeroConstructor {
    private String name, epithet, origin, color, weaponType, moveType;
    //these stats are 1* lv1 (regardless of obtainable rarities)
    private int[] stats, statGrowths;
    private Integer rarity;
    private Boolean summonable, isInNormalPool;

    private GregorianCalendar dateReleased;

    void setName(String name) { this.name = name; }
    void setEpithet(String epithet) { this.epithet = epithet; }
    void setOrigin(String origin) { this.origin = origin; }
    void setColor(String color) { this.color = color; }
    void setWeaponType(String weaponType) { this.weaponType = weaponType; }
    void setMoveType(String moveType) { this.moveType = moveType; }
    void setStats(int[] stats) { this.stats = stats; }
    void setStatGrowths(int[] statGrowths) { this.statGrowths = statGrowths; }
    void setRarity(int rarity) { this.rarity = rarity; }
    void setSummonable(boolean summonable) { this.summonable = summonable; }
    void setInNormalPool(boolean isInNormalPool) { this.isInNormalPool = isInNormalPool; }
    void setDateReleased(GregorianCalendar dateReleased) { this.dateReleased = dateReleased; }

    String getName() { return name; }
    String getEpithet() { return epithet; }
    String getOrigin() { return origin; }
    String getColor() { return color; }
    String getWeaponType() { return weaponType; }
    String getMoveType() { return moveType; }
    int[] getStats() { return stats; }
    int[] getStatGrowths() { return statGrowths; }
    int getRarity() { return rarity; }
    boolean getSummonable() { return summonable; }
    boolean getInNormalPool() { return isInNormalPool; }
    GregorianCalendar getDateReleased() { return dateReleased; }

    Hero createHero() {
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