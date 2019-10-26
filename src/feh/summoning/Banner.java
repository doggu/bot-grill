package feh.summoning;

import feh.heroes.character.Availability;
import feh.heroes.character.Hero;
import feh.heroes.HeroDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Banner {
    private final String name;

    private final List<Hero>
            rarityFPool,
            rarity5Pool,
            rarity4Pool,
            rarity3Pool;

    private double
            rarityFrate,
            rarity5rate,
            rarity4rate,
            rarity3rate;

    private final GregorianCalendar startDate, endDate;



    //TODO: generate banner type based on name or request info?
    public Banner(String name, List<Hero> focusUnits, GregorianCalendar startDate, GregorianCalendar endDate) {
        this.name = name;
        this.rarityFPool = focusUnits;
        this.startDate = startDate;
        this.endDate = endDate;

        List<List<Hero>> pools = generatePools();
        rarity5Pool = pools.get(0);
        rarity4Pool = pools.get(1);
        rarity3Pool = pools.get(2);
        generateRates();
    }



    /**
     * generates the summoning pools of a banner based on when the banner was released,
     * determining which units were part of the normal summoning pool at that time
     *
     * directly modifies rarity pool fields
     */
    private List<List<Hero>> generatePools() {
        //System.out.println("\nGenerating pools for: "+this.getName());

        List<Hero> rarity5Pool = new ArrayList<>();
        List<Hero> rarity4Pool = new ArrayList<>();
        List<Hero> rarity3Pool = new ArrayList<>();
        List<List<Hero>> pools = new ArrayList<>();
        pools.add(rarity5Pool);
        pools.add(rarity4Pool);
        pools.add(rarity3Pool);

        //must have this date to accurately represent summoning pools for historical banners
        GregorianCalendar poolCutoff = (GregorianCalendar) startDate.clone();

        //creates a list of HEROES that could be summoned in normal pools
        for (Hero x: HeroDatabase.HEROES) {
            GregorianCalendar characterReleaseDate = (GregorianCalendar) x.getReleaseDate().clone();
            //TODO: this value is not always correct
            // can be calculated by finding the release date of the hero (banners that do this begin with "New Heroes")
            // however, BannerDatabase itself uses a completed hero list to create the banners and pools
            // brain damage
            characterReleaseDate.add(GregorianCalendar.DAY_OF_MONTH, 20);
            //if character is summonable
            if (!x.isInNormalPool()) {
                //System.out.println("skipped (not normal pool): " + x.getName()+": " + x.getEpithet());
                continue;
            }

            // compareTo returns:
            // if this is the same date as arg, 0
            // if this is before arg, -1
            // if this is after arg, 1
            // if character's banner ended after this banner's start
            GregorianCalendar gameRelease =
                    new GregorianCalendar(2017,Calendar.FEBRUARY,2);
            gameRelease.add(GregorianCalendar.DAY_OF_MONTH, 20);
            if (characterReleaseDate.compareTo(gameRelease)==0) {
                //it's k
            } else {
                if (characterReleaseDate.compareTo(poolCutoff)>=0) {
                    //System.out.println("skipped (too new): " + x.getName()+": " + x.getEpithet());
                    continue;
                }
            }

            //System.out.println("added (" + x.getRarity() + "): " + x.getName() + ": " + x.getEpithet());

            int bannerRarity = x.getSummonableRarity();

            GregorianCalendar rarityChangeDate =
                    new GregorianCalendar(2018, GregorianCalendar.APRIL, 10);
            if (x.getAvailability()==Availability.NORMAL_RARITY_CHANGED &&
                    startDate.getTimeInMillis() <= rarityChangeDate.getTimeInMillis())
                bannerRarity++;

            switch (bannerRarity) {
                case 1:
                case 2:
                case 3:
                    rarity3Pool.add(x);
                    rarity4Pool.add(x);
                    break;
                case 4:
                    rarity4Pool.add(x);
                    rarity5Pool.add(x);
                    break;
                case 5:
                    rarity5Pool.add(x);
                    break;
                default:
                    System.out.println("ahHHhHHhhhHHh");
                    throw new Error();
            }
        }

        return pools;
    }

    private void generateRates() {
        String[] args = getName().split(" ");
        switch(args[0]) {
            default:
            case "Focus:":
                rarityFrate = 3;
                rarity5rate = 3;
                rarity4rate = 58;
                rarity3rate = 36;
                //they just had to do it to me huh
                if (args[1].equals("Weekly")) {
                    rarityFrate++;
                    rarity5rate--;
                }
                break;
            //the banner feat. marth, lucina, robin, tiki doesn't register due to "Legendary" being args[1]
            case "Legendary":
                rarityFrate = 8;
                rarity5rate = 0;
                rarity4rate = 58;
                rarity3rate = 34;
                break;
            //"Hero" works for hero fest since it's the only banner starting with "Hero"
            case "Hero":
                rarityFrate = 5;
                rarity5rate = 3;
                rarity4rate = 58;
                rarity3rate = 34;
                break;
            //surprisingly, the guaranteed summon events are the only banners with args[0] being "Special"
            case "Special":
                rarityFrate = 100;
                rarity5rate = 0;
                rarity4rate = 0;
                rarity3rate = 0;
        }

        //this switch statement should probably be converted to a more absolute system for future-proofing

        //TODO: create new class for banner instances? (for storing pity, summoner data, etc.)

        //TODO: KEEP NOTE: pity rates are added based on the ratio between focus and normal 5* pool (always 0.50% total)
        //TODO: e.x. Hero Fest (the only real example here): 5%, 3% ---> 5.3125%, 3.1875% ---> 5.625%, 3.375%, etc.
    }



    public String getName() { return name; }
    public GregorianCalendar getStartDate() { return startDate; }
    public GregorianCalendar getEndDate() { return endDate; }

    public List<Hero> getRarityFPool() { return new ArrayList<>(rarityFPool); }
    public List<Hero> getRarity5Pool() { return new ArrayList<>(rarity5Pool); }
    public List<Hero> getRarity4Pool() { return new ArrayList<>(rarity4Pool); }
    public List<Hero> getRarity3Pool() { return new ArrayList<>(rarity3Pool); }

    public double getRarityFRate() { return rarityFrate; }
    public double getRarity5Rate() { return rarity5rate; }
    public double getRarity4Rate() { return rarity4rate; }
    public double getRarity3Rate() { return rarity3rate; }



    public static void main(String[] args) {
        String name = "New Heroes: Farfetched Heroes";
        List<Hero> focuses = new ArrayList<>();
        focuses.add(new Hero("Mia: Lady of Blades"));
        focuses.add(new Hero("Lute: Prodigy"));
        focuses.add(new Hero("Dorcas: Serene Warrior"));
        /*
        GregorianCalendar start = new GregorianCalendar(2017,10,15),
                end = new GregorianCalendar(2017,11,4);
        */
        //NOTE: MONTH IS ZERO BASED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        GregorianCalendar start = new GregorianCalendar(2018,8,14),
                end = new GregorianCalendar(2018,9,10);


        Banner g = new Banner(name, focuses, start, end);

        int i;
        for (i=0; end.compareTo(start)<=0; i++) {
            System.out.println(end.compareTo(start));
            start.add(Calendar.DAY_OF_MONTH, 1);
        }

        System.out.println(i);
    }
}