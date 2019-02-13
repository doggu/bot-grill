package utilities.feh.heroes.character;

import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * a class which allows for the construction of an object that stores information for a hero without requiring all of
 * said components to be produced at once.
 *
 */
public class HeroConstructor {
    private HeroName fullName;
    private String origin, color;
    private WeaponClass weaponType;
    private MovementClass moveType;
    //these stats are 1* lv1 (regardless of obtainable rarities)
    private int[] statsArr;
    private int[] growthsArr;
    private HeroStats stats;
    private Integer rarity;
    private Availability availability;
    private GregorianCalendar dateReleased;



    public void setFullName(HeroName name) { this.fullName = name; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setColor(String color) { this.color = color; }
    public void setWeaponType(String weaponType) {
        switch (weaponType) {
            case "Red Sword":
                this.weaponType = WeaponClass.SWORD;
                break;
            case "Blue Lance":
                this.weaponType = WeaponClass.LANCE;
                break;
            case "Green Axe":
                this.weaponType = WeaponClass.AXE;
                break;
            case "Red Tome":
                this.weaponType = WeaponClass.RED_TOME;
                break;
            case "Blue Tome":
                this.weaponType = WeaponClass.BLUE_TOME;
                break;
            case "Green Tome":
                this.weaponType = WeaponClass.GREEN_TOME;
                break;
            case "Colorless Staff":
                this.weaponType = WeaponClass.STAFF;
                break;
            case "Red Beast":
            case "Blue Beast":
            case "Green Beast":
            case "Colorless Beast":
                this.weaponType = WeaponClass.BEAST;
                break;
            case "Red Breath":
            case "Blue Breath":
            case "Green Breath":
            case "Colorless Breath":
                this.weaponType = WeaponClass.BREATH;
                break;
            case "Red Bow":
            case "Blue Bow":
            case "Green Bow":
            case "Colorless Bow":
                this.weaponType = WeaponClass.BOW;
                break;
            case "Red Dagger":
            case "Blue Dagger":
            case "Green Dagger":
            case "Colorless Dagger":
                this.weaponType = WeaponClass.DAGGER;
                break;
            default:
                System.out.println("weaponType wasn't correct: "+weaponType);
                throw new Error();
        }
    }
    public void setMoveType(String moveType) {
        switch (moveType) {
            case "Infantry":
                this.moveType = MovementClass.INFANTRY;
                break;
            case "Armored":
                this.moveType = MovementClass.ARMORED;
                break;
            case "Cavalry":
                this.moveType = MovementClass.CAVALRY;
                break;
            case "Flying":
                this.moveType = MovementClass.FLYING;
                break;
            default:
                System.out.println("HeroConstructor got an unidentified move type: "+moveType);
                throw new Error();
        }
    }
    public void setStats(HeroStats stats) { this.stats = stats; }
    public void setStats(int[] stats) { this.statsArr = stats.clone(); }
    public void setGrowths(int[] growths) { this.growthsArr = growths.clone(); }
    public void setRarity(int rarity) { this.rarity = rarity; }
    public void setAvailability(Availability availability) { this.availability = availability; }
    public void setDateReleased(GregorianCalendar dateReleased) { this.dateReleased = dateReleased; }



    public HeroName getFullName() { return fullName; }
    public String getName() { return fullName.getName(); }
    public String getEpithet() { return fullName.getEpithet(); }
    public String getOrigin() { return origin; }
    public String getColor() { return color; }
    public WeaponClass getWeaponType() { return weaponType; }
    public MovementClass getMoveType() { return moveType; }
    public HeroStats getStats() { return stats; }
    public int getRarity() { return rarity; }
    public Availability getAvailability() { return availability; }
    public GregorianCalendar getDateReleased() { return dateReleased; }



    public Hero createHero() throws Error {
        if (fullName==null) {
            System.out.println("missing name!");
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
        if (statsArr!=null) {
            if (growthsArr!=null) {
                stats = new HeroStats(statsArr[0], statsArr[1], statsArr[2], statsArr[3], statsArr[4],
                        growthsArr[0], growthsArr[1], growthsArr[2], growthsArr[3], growthsArr[4]);
            } else {
                System.out.println("missing growths!");
                throw new Error();
            }
        } else {
            System.out.println("missing stats!");
            throw new Error();
        }
        if (rarity==null) {
            System.out.println("missing rarity!");
            throw new Error();
        }
        if (availability==null) {
            System.out.println("missing availability!");
            throw new Error();
        }
        if (dateReleased==null) {
            System.out.println("missing dateReleased!");
            throw new Error();
        }

        return new Hero(fullName, origin,
                color, weaponType, moveType, rarity,
                availability, dateReleased,
                stats);
    }
}