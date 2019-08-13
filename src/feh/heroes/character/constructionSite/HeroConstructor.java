package feh.heroes.character.constructionSite;

import feh.heroes.character.*;
import feh.skills.skillTypes.Skill;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * a class which allows for the construction of an object that
 * stores information for a hero without requiring all of
 * said components to be produced at once.
 *
 */
public class HeroConstructor {
    private HeroName fullName;
    private String origin;
    private String portraitLink;
    private Character gender;
    private Character color;
    private WeaponClass weaponType;
    private MovementClass moveType;
    //these stats are 1* lv1 (regardless of obtainable rarities)
    private int[] statsArr;
    private int[] growthsArr;
    private HeroStats stats;
    private Integer rarity;
    private Availability availability;
    private GregorianCalendar dateReleased;
    private ArrayList<Skill> baseKit;



    public void setFullName(HeroName name) { this.fullName = name; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setPortraitLink(String portraitLink) { this.portraitLink = portraitLink; }
    public void setGender(Character gender) { this.gender = gender; }
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
                throw new Error("weaponType wasn't correct: "+weaponType);
        }

        color = weaponType.toLowerCase().charAt(0);
    }
    public void setWeaponType(WeaponClass weaponType) { this.weaponType = weaponType; }
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
    public void setMoveType(MovementClass moveType) { this.moveType = moveType; }
    public void setColor(Character color) { this.color = color; }
    public void setStats(HeroStats stats) { this.stats = stats; }
    public void setStats(int[] stats) { this.statsArr = stats; }
    public void setGrowths(int[] growths) { this.growthsArr = growths; }
    public void setRarity(Integer rarity) { this.rarity = rarity; }
    public void setAvailability(Availability availability) { this.availability = availability; }
    public void setDateReleased(GregorianCalendar dateReleased) { this.dateReleased = dateReleased; }
    public void setBaseKit(ArrayList<Skill> baseKit) { this.baseKit = baseKit; }



    public HeroName getFullName() { return fullName; }
    public String getName() { return fullName.getName(); }
    public String getEpithet() { return fullName.getEpithet(); }
    public String getOrigin() { return origin; }
    public String getPortraitLink() { return portraitLink; }
    public Character getGender() { return gender; }
    public Character getColor() { return color; }
    public WeaponClass getWeaponType() { return weaponType; }
    public MovementClass getMoveType() { return moveType; }
    public HeroStats getStats() { return stats; }
    public int getRarity() { return rarity; }
    public Availability getAvailability() { return availability; }
    public GregorianCalendar getDateReleased() { return dateReleased; }
    public ArrayList<Skill> getBaseKit() { return baseKit; }



    public Hero createHero() throws Error {
        if (fullName==null) {
            throw new Error("missing name! (like seriously wtf)");
        }
        if (origin==null) {
            throw new Error("missing origin!");
        }
        if (portraitLink==null) {
            throw new Error("missing portraitLink!");
        }
        if (gender==null) {
            throw new Error("missing gender!");
        }
        if (weaponType==null) {
            throw new Error("missing weaponType!");
        }
        if (moveType==null) {
            throw new Error("missing moveType!");
        }
        if (color==null) {
            throw new Error("missing color!");
        }
        if (statsArr!=null) {
            if (growthsArr!=null) {
                stats = new HeroStats(statsArr, growthsArr);
            } else {
                throw new Error("missing growths!");
            }
        } else {
            throw new Error("missing stats!");
        }
        if (rarity==null) {
            throw new Error("missing rarity!");
        }
        if (availability==null) {
            throw new Error("missing availability!");
        }
        if (dateReleased==null) {
            throw new Error("missing dateReleased!");
        }
        if (baseKit==null) {
            //System.out.println("missing base kit!");
            //throw new Error();
            //ah whatever
            baseKit = new ArrayList<>();
        }

        return new Hero(fullName,
                        origin,
                        portraitLink,
                        gender,
                        color,
                        weaponType,
                        moveType,
                        rarity,
                        availability,
                        dateReleased,
                        stats,
                        baseKit);
    }

    public static HeroConstructor merge(HeroConstructor h1, HeroConstructor h2)
            throws MismatchedInputException /*, NullInputException*/ {
        HeroConstructor merge = new HeroConstructor();

        Merger<HeroName> fullName = new Merger<>(h1.fullName, h2.fullName);
        Merger<String> origin = new Merger<>(h1.origin, h2.origin);
        Merger<String> portraitLink = new Merger<>(h1.portraitLink, h2.portraitLink);
        Merger<WeaponClass> weaponType = new Merger<>(h1.weaponType, h2.weaponType);
        Merger<MovementClass> moveType = new Merger<>(h1.moveType, h2.moveType);
        Merger<Character> color = new Merger<>(h1.color, h2.color);
        Merger<Integer> rarity = new Merger<>(h1.rarity, h2.rarity);
        Merger<Availability> availability = new Merger<>(h1.availability, h2.availability);
        Merger<GregorianCalendar> dateReleased = new Merger<>(h1.dateReleased, h2.dateReleased);
        Merger<int[]> stats = new Merger<>(h1.statsArr, h2.statsArr);
        Merger<int[]> growths = new Merger<>(h1.growthsArr, h2.growthsArr);
        Merger<ArrayList<Skill>> baseKit = new Merger<>(h1.baseKit, h2.baseKit);

        merge.setFullName(fullName.merge());
        merge.setFullName(fullName.merge());
        merge.setPortraitLink(portraitLink.merge());
        merge.setOrigin(origin.merge());
        merge.setWeaponType(weaponType.merge());
        merge.setMoveType(moveType.merge());
        merge.setColor(color.merge());
        merge.setRarity(rarity.merge());
        merge.setAvailability(availability.merge());
        merge.setDateReleased(dateReleased.merge());
        merge.setStats(stats.merge());
        merge.setGrowths(growths.merge());
        merge.setBaseKit(baseKit.merge());

        return merge;
    }
}