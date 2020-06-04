package feh.characters.hero.constructionSite;

import feh.characters.hero.*;
import feh.characters.skills.skillTypes.Skill;

import java.net.URL;
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
    private Origin origin;
    private String description;
    private URL gamepediaLink;
    private URL portraitLink;
    private String artist;
    private Character gender;
    private Character color;
    private WeaponClass weaponType;
    private MovementClass moveType;
    //these stats are 1* lv1 (regardless of obtainable rarities)
    private int[] statsArr;
    private int[] growthsArr;
    private HeroStats stats;
    private Integer summonableRarity;
    private Availability availability;
    private GregorianCalendar dateReleased;
    private ArrayList<Skill> baseKit;



    public void setFullName(HeroName name) {
        this.fullName = name; }
    public void setOrigin(Origin origin) {
        this.origin = origin; }
    public void setDescription(String description) {
        this.description = description; }
    public void setGamepediaLink(URL gamepediaLink) {
        this.gamepediaLink = gamepediaLink; }
    public void setPortraitLink(URL portraitLink) {
        this.portraitLink = portraitLink; }
    public void setArtist(String artist) {
        this.artist = artist; }
    public void setGender(Character gender) {
        this.gender = gender; }
    public void setWeaponType(String weaponType) {
        this.weaponType = WeaponClass.getClass(weaponType);

        color = weaponType.toLowerCase().charAt(0);
    }
    public void setWeaponType(WeaponClass weaponType) {
        this.weaponType = weaponType; }
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
                System.out.println("HeroConstructor got " +
                        "an unidentified move type: "+moveType);
                throw new Error();
        }
    }
    public void setMoveType(MovementClass moveType) {
        this.moveType = moveType; }
    public void setColor(Character color) {
        this.color = color; }
    public void setStats(HeroStats stats) {
        this.stats = stats; }
    public void setStats(int[] stats) {
        this.statsArr = stats; }
    public void setGrowths(int[] growths) {
        this.growthsArr = growths; }
    public void setSummonableRarity(Integer summonableRarity) {
        this.summonableRarity = summonableRarity; }
    public void setAvailability(Availability availability) {
        this.availability = availability; }
    public void setDateReleased(GregorianCalendar dateReleased) {
        this.dateReleased = dateReleased; }
    public void setBaseKit(ArrayList<Skill> baseKit) {
        this.baseKit = baseKit; }



    public HeroName getFullName() { return fullName; }
    public String getName() { return fullName.getName(); }
    @SuppressWarnings("unused")
    public String getEpithet() { return fullName.getEpithet(); }
    public Origin getOrigin() { return origin; }
    public String getDescription() { return description; }
    @SuppressWarnings("unused")
    public URL getPortraitLink() { return portraitLink; }
    @SuppressWarnings("unused")
    public URL getGamepediaLink() { return gamepediaLink; }
    public String getArtist() { return artist; }
    public Character getGender() { return gender; }
    public Character getColor() { return color; }
    @SuppressWarnings("unused")
    public WeaponClass getWeaponType() { return weaponType; }
    public MovementClass getMoveType() { return moveType; }
    public HeroStats getStats() { return stats; }
    @SuppressWarnings("unused")
    public int getSummonableRarity() { return summonableRarity; }
    public Availability getAvailability() { return availability; }
    @SuppressWarnings("unused")
    public GregorianCalendar getDateReleased() { return dateReleased; }
    @SuppressWarnings("unused")
    public ArrayList<Skill> getBaseKit() { return baseKit; }



    public Hero createHero() throws Error {
        if (fullName==null) {
            throw new Error("missing name! (like seriously wtf)");
        }
        if (origin==null) {
            throw new Error("missing origin: "+fullName);
        }
        if (description==null) {
            throw new Error("missing description: "+fullName);
        }
        if (gamepediaLink==null) {
            throw new Error("missing gamepediaLink: "+fullName);
        }
        if (portraitLink==null) {
            throw new Error("missing portraitLink: "+fullName);
        }
        if (artist==null) {
            throw new Error("missing artist: "+fullName);
        }
        if (gender==null) {
            throw new Error("missing gender: "+fullName);
        }
        if (weaponType==null) {
            throw new Error("missing weaponType: "+fullName);
        }
        if (moveType==null) {
            throw new Error("missing moveType: "+fullName);
        }
        if (color==null) {
            throw new Error("missing color: "+fullName);
        }
        if (statsArr!=null) {
            if (growthsArr!=null) {
                stats = new HeroStats(statsArr, growthsArr);
            } else {
                throw new Error("missing growths: "+fullName);
            }
        } else {
            throw new Error("missing stats: "+fullName);
        }
        if (summonableRarity==null) {
            throw new Error("missing rarity: "+fullName);
        }
        if (availability==null) {
            throw new Error("missing availability: "+fullName);
        }
        if (dateReleased==null) {
            throw new Error("missing dateReleased: "+fullName);
        }
        if (baseKit==null) {
            //System.out.println("missing base kit: "+fullName);
            //throw new Error();
            //ah whatever
            baseKit = new ArrayList<>();
        }

        return new Hero(fullName,
                        origin,
                        description,
                        gamepediaLink,
                        portraitLink,
                        artist,
                        gender,
                        color,
                        weaponType,
                        moveType,
                        summonableRarity,
                        availability,
                        dateReleased,
                        stats,
                        baseKit);
    }

    public static HeroConstructor merge(HeroConstructor h1, HeroConstructor h2)
            throws MismatchedInputException /*, NullInputException*/ {
        HeroConstructor merge = new HeroConstructor();

        Merger<HeroName> fullName = 
                new Merger<>(h1.fullName, h2.fullName);
        Merger<Origin> origin =
                new Merger<>(h1.origin, h2.origin);
        Merger<String> description =
                new Merger<>(h1.description, h2.description);
        Merger<URL> gamepediaLink =
                new Merger<>(h1.gamepediaLink, h2.gamepediaLink);
        Merger<URL> portraitLink =
                new Merger<>(h1.portraitLink, h2.portraitLink);
        Merger<String> artist =
                new Merger<>(h1.artist, h2.artist);
        Merger<Character> gender =
                new Merger<>(h1.gender, h2.gender);
        Merger<WeaponClass> weaponType =
                new Merger<>(h1.weaponType, h2.weaponType);
        Merger<MovementClass> moveType =
                new Merger<>(h1.moveType, h2.moveType);
        Merger<Character> color =
                new Merger<>(h1.color, h2.color);
        Merger<Integer> rarity =
                new Merger<>(h1.summonableRarity, h2.summonableRarity);
        Merger<Availability> availability =
                new Merger<>(h1.availability, h2.availability);
        Merger<GregorianCalendar> dateReleased =
                new Merger<>(h1.dateReleased, h2.dateReleased);
        Merger<int[]> stats =
                new Merger<>(h1.statsArr, h2.statsArr);
        Merger<int[]> growths =
                new Merger<>(h1.growthsArr, h2.growthsArr);
        Merger<ArrayList<Skill>> baseKit =
                new Merger<>(h1.baseKit, h2.baseKit);

        merge.setFullName(fullName.merge());
        merge.setGamepediaLink(gamepediaLink.merge());
        merge.setDescription(description.merge());
        merge.setPortraitLink(portraitLink.merge());
        merge.setArtist(artist.merge());
        merge.setGender(gender.merge());
        merge.setOrigin(origin.merge());
        merge.setWeaponType(weaponType.merge());
        merge.setMoveType(moveType.merge());
        merge.setColor(color.merge());
        merge.setSummonableRarity(rarity.merge());
        merge.setAvailability(availability.merge());
        merge.setDateReleased(dateReleased.merge());
        merge.setStats(stats.merge());
        merge.setGrowths(growths.merge());
        merge.setBaseKit(baseKit.merge());

        return merge;
    }
}